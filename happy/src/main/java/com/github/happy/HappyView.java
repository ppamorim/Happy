package com.github.happy;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ImageView;
import java.util.Collection;

public class HappyView extends ViewGroup {

  private static final int DRAG_MAX_DISTANCE = 120;
  private static final float DRAG_RATE = .5f;
  private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
  public static final int STYLE_SUN = 0;
  public static final int MAX_OFFSET_ANIMATION_DURATION = 700;
  private static final int INVALID_POINTER = -1;

  private View target;
  private ImageView refreshView;
  private Interpolator decelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
  private int touchSlop;
  private int totalDragDistance;
  private RefreshView sunRefreshView;
  private float currentDragPercent;
  private int currentOffsetTop;
  private boolean refreshing;
  private int activePointerId;
  private boolean isBeingDragged;
  private float initialMotionY;
  private int from;
  private float fromDragPercent;
  private boolean notify;
  private OnRefreshListener listener;

  public HappyView(Context context) {
    this(context, null);
  }

  public HappyView(Context context, AttributeSet attrs) {
    super(context, attrs);
    configAttributes(context);
    sunRefreshView = new RefreshView(getContext(), this);
    addView(refreshView);
    setWillNotDraw(false);
    ViewCompat.setChildrenDrawingOrderEnabled(this, true);
  }

  public void start() {

    refreshView.setImageDrawable(sunRefreshView);
  }

  public void setLayers(Collection<Layer> layers) {
    sunRefreshView.setLayers(layers);
  }

  private void configAttributes(Context context) {
    touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    totalDragDistance = Utils.convertDpToPixel(context, DRAG_MAX_DISTANCE);
    refreshView = new ImageView(context);
  }

  public int getTotalDragDistance() {
    return totalDragDistance;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    ensureTarget();
    if (target == null) {
      return;
    }
    widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
    heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
    target.measure(widthMeasureSpec, heightMeasureSpec);
    refreshView.measure(widthMeasureSpec, heightMeasureSpec);
  }

  private void ensureTarget() {
    if (target != null)
      return;
    if (getChildCount() > 0) {
      for (int i = 0; i < getChildCount(); i++) {
        View child = getChildAt(i);
        if (child != refreshView)
          target = child;
      }
    }
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {

    if (!isEnabled() || canChildScrollUp() || refreshing) {
      return false;
    }

    final int action = MotionEventCompat.getActionMasked(ev);

    switch (action) {
      case MotionEvent.ACTION_DOWN:
        setTargetOffsetTop(0, true);
        activePointerId = MotionEventCompat.getPointerId(ev, 0);
        isBeingDragged = false;
        final float initialMotionY = getMotionEventY(ev, activePointerId);
        if (initialMotionY == -1) {
          return false;
        }
        this.initialMotionY = initialMotionY;
        break;
      case MotionEvent.ACTION_MOVE:
        if (activePointerId == INVALID_POINTER) {
          return false;
        }
        final float y = getMotionEventY(ev, activePointerId);
        if (y == -1) {
          return false;
        }
        final float yDiff = y - this.initialMotionY;
        if (yDiff > touchSlop && !isBeingDragged) {
          isBeingDragged = true;
        }
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        isBeingDragged = false;
        activePointerId = INVALID_POINTER;
        break;
      case MotionEventCompat.ACTION_POINTER_UP:
        onSecondaryPointerUp(ev);
        break;
    }

    return isBeingDragged;
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {

    if (!isBeingDragged) {
      return super.onTouchEvent(ev);
    }

    final int action = MotionEventCompat.getActionMasked(ev);

    switch (action) {
      case MotionEvent.ACTION_MOVE: {
        final int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (pointerIndex < 0) {
          return false;
        }

        final float y = MotionEventCompat.getY(ev, pointerIndex);
        final float yDiff = y - initialMotionY;
        final float scrollTop = yDiff * DRAG_RATE;
        currentDragPercent = scrollTop / totalDragDistance;
        if (currentDragPercent < 0) {
          return false;
        }
        float boundedDragPercent = Math.min(1f, Math.abs(currentDragPercent));
        float extraOS = Math.abs(scrollTop) - totalDragDistance;
        float slingshotDist = totalDragDistance;
        float tensionSlingshotPercent = Math.max(0,
            Math.min(extraOS, slingshotDist * 2) / slingshotDist);
        float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
            (tensionSlingshotPercent / 4), 2)) * 2f;
        float extraMove = (slingshotDist) * tensionPercent / 2;
        int targetY = (int) ((slingshotDist * boundedDragPercent) + extraMove);

        sunRefreshView.setPercent(currentDragPercent, true);
        setTargetOffsetTop(targetY - currentOffsetTop, true);
        break;
      }
      case MotionEventCompat.ACTION_POINTER_DOWN:
        final int index = MotionEventCompat.getActionIndex(ev);
        activePointerId = MotionEventCompat.getPointerId(ev, index);
        break;
      case MotionEventCompat.ACTION_POINTER_UP:
        onSecondaryPointerUp(ev);
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL: {
        if (activePointerId == INVALID_POINTER) {
          return false;
        }
        final int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
        final float y = MotionEventCompat.getY(ev, pointerIndex);
        final float overScrollTop = (y - initialMotionY) * DRAG_RATE;
        isBeingDragged = false;
        if (overScrollTop > totalDragDistance) {
          setRefreshing(true, true);
        } else {
          refreshing = false;
          animateOffsetToStartPosition();
        }
        activePointerId = INVALID_POINTER;
        return false;
      }
    }

    return true;
  }

  private void animateOffsetToStartPosition() {
    from = currentOffsetTop;
    fromDragPercent = currentDragPercent;
    long animationDuration = Math.abs((long) (MAX_OFFSET_ANIMATION_DURATION * fromDragPercent));

    mAnimateToStartPosition.reset();
    mAnimateToStartPosition.setDuration(animationDuration);
    mAnimateToStartPosition.setInterpolator(decelerateInterpolator);
    mAnimateToStartPosition.setAnimationListener(mToStartListener);
    refreshView.clearAnimation();
    refreshView.startAnimation(mAnimateToStartPosition);
  }

  private void animateOffsetToCorrectPosition() {
    from = currentOffsetTop;
    fromDragPercent = currentDragPercent;

    mAnimateToCorrectPosition.reset();
    mAnimateToCorrectPosition.setDuration(MAX_OFFSET_ANIMATION_DURATION);
    mAnimateToCorrectPosition.setInterpolator(decelerateInterpolator);
    refreshView.clearAnimation();
    refreshView.startAnimation(mAnimateToCorrectPosition);

    if (refreshing) {
      sunRefreshView.start();
      if (notify) {
        if (listener != null) {
          listener.onRefresh();
        }
      }
    } else {
      sunRefreshView.stop();
      animateOffsetToStartPosition();
    }
    currentOffsetTop = target.getTop();
    target.setPadding(0, 0, 0, totalDragDistance);
  }

  private final Animation mAnimateToStartPosition = new Animation() {
    @Override
    public void applyTransformation(float interpolatedTime, Transformation t) {
      moveToStart(interpolatedTime);
    }
  };

  private final Animation mAnimateToCorrectPosition = new Animation() {
    @Override
    public void applyTransformation(float interpolatedTime, Transformation t) {
      int targetTop;
      int endTarget = totalDragDistance;
      targetTop = (from + (int) ((endTarget - from) * interpolatedTime));
      int offset = targetTop - target.getTop();

      currentDragPercent = fromDragPercent - (fromDragPercent - 1.0f) * interpolatedTime;
      sunRefreshView.setPercent(currentDragPercent, false);

      setTargetOffsetTop(offset, false /* requires update */);
    }
  };

  private void moveToStart(float interpolatedTime) {
    int targetTop = from - (int) (from * interpolatedTime);
    float targetPercent = fromDragPercent * (1.0f - interpolatedTime);
    int offset = targetTop - target.getTop();

    currentDragPercent = targetPercent;
    sunRefreshView.setPercent(currentDragPercent, true);
    target.setPadding(0, 0, 0, targetTop);
    setTargetOffsetTop(offset, false);
  }

  public void setRefreshing(boolean refreshing) {
    if (this.refreshing != refreshing) {
      setRefreshing(refreshing, false);
    }
  }

  private void setRefreshing(boolean refreshing, final boolean notify) {
    if (this.refreshing != refreshing) {
      this.notify = notify;
      ensureTarget();
      this.refreshing = refreshing;
      if (this.refreshing) {
        sunRefreshView.setPercent(1f, true);
        animateOffsetToCorrectPosition();
      } else {
        animateOffsetToStartPosition();
      }
    }
  }

  private Animation.AnimationListener mToStartListener = new Animation.AnimationListener() {
    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
      sunRefreshView.stop();
      currentOffsetTop = target.getTop();
    }
  };

  private void onSecondaryPointerUp(MotionEvent ev) {
    final int pointerIndex = MotionEventCompat.getActionIndex(ev);
    final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
    if (pointerId == activePointerId) {
      final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
      activePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
    }
  }

  private float getMotionEventY(MotionEvent ev, int activePointerId) {
    final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
    if (index < 0) {
      return -1;
    }
    return MotionEventCompat.getY(ev, index);
  }

  private void setTargetOffsetTop(int offset, boolean requiresUpdate) {
    target.offsetTopAndBottom(offset);
    sunRefreshView.offsetTopAndBottom(offset);
    currentOffsetTop = target.getTop();
    if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
      invalidate();
    }
  }

  private boolean canChildScrollUp() {
    if (android.os.Build.VERSION.SDK_INT < 14) {
      if (target instanceof AbsListView) {
        final AbsListView absListView = (AbsListView) target;
        return absListView.getChildCount() > 0
            && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
            .getTop() < absListView.getPaddingTop());
      } else {
        return target.getScrollY() > 0;
      }
    } else {
      return ViewCompat.canScrollVertically(target, -1);
    }
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {

    ensureTarget();
    if (target == null)
      return;

    int height = getMeasuredHeight();
    int width = getMeasuredWidth();
    int left = getPaddingLeft();
    int top = getPaddingTop();
    int right = getPaddingRight();
    int bottom = getPaddingBottom();

    target.layout(left, top + currentOffsetTop, left + width - right,
        top + height - bottom + currentOffsetTop);
    refreshView.layout(left, top, left + width - right, top + height - bottom);
  }

  public void setOnRefreshListener(OnRefreshListener listener) {
    this.listener = listener;
  }

}
