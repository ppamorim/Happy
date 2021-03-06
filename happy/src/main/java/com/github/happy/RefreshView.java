package com.github.happy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import java.util.Collection;

public class RefreshView extends BaseRefreshView implements Animatable {

  private static final float SCALE_START_PERCENT = 0.5f;
  private static final int ANIMATION_DURATION = 1000;

  private final static float SKY_RATIO = 0.65f;
  private static final float SKY_INITIAL_SCALE = 1.05f;

  private final static float TOWN_RATIO = 0.22f;

  private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

  private HappyView mParent;
  private Matrix mMatrix;
  private Animation mAnimation;

  private int mTop;
  private int mScreenWidth;

  private int mSkyHeight;
  private float mSkyTopOffset;
  private float mSkyMoveOffset;

  private int mTownHeight;

  private float mPercent = 0.0f;

  private Collection<Layer> layers;

  public RefreshView(Context context, final HappyView parent) {
    super(context, parent);
    mParent = parent;
    mMatrix = new Matrix();

    setupAnimations();
    parent.post(new Runnable() {
      @Override public void run() {
        initiateDimens(parent.getWidth());
      }
    });
  }

  public void initiateDimens(int viewWidth) {
    if (viewWidth <= 0 || viewWidth == mScreenWidth) return;
    mScreenWidth = viewWidth;
    mSkyHeight = (int) (SKY_RATIO * mScreenWidth);
    mSkyTopOffset = (mSkyHeight * 0.38f);
    mSkyMoveOffset = Utils.convertDpToPixel(getContext(), 15);
    mTownHeight = (int) (TOWN_RATIO * mScreenWidth);
    mTop = -mParent.getTotalDragDistance();
  }

  public void setLayers(Collection<Layer> layers) {
    this.layers = layers;
  }

  @Override public void setPercent(float percent, boolean invalidate) {
    setPercent(percent);
    if (invalidate) {
      setRotate(percent);
    }
  }

  @Override public void offsetTopAndBottom(int offset) {
    mTop += offset;
    invalidateSelf();
  }

  @Override public void draw(Canvas canvas) {
    if (mScreenWidth <= 0) {
      return;
    }
    final int saveCount = canvas.save();
    canvas.translate(0, mTop);
    if(layers != null) {
      for (Layer layer : layers) {
        drawBitmap(layer, canvas);
      }
    }
    canvas.restoreToCount(saveCount);
  }

  private void drawBitmap(Layer layer, Canvas canvas) {
    mMatrix.reset();
    if(layer.getAnimationScale() != null) {
      mMatrix.postScale(layer.getAnimationScale().scaleX(), layer.getAnimationScale().scaleY());
    }
    if(layer.getAnimationSlide() != null) {
      mMatrix.postTranslate(layer.getAnimationSlide().slideX(), layer.getAnimationSlide().slideY());
    }
    canvas.drawBitmap(layer.getBitmap(), mMatrix, null);
  }

  public void setPercent(float percent) {
    mPercent = percent;
  }

  public void setRotate(float rotate) {
    invalidateSelf();
  }

  public void resetOriginals() {
    setPercent(0);
    setRotate(0);
  }

  @Override public void setAlpha(int i) {

  }

  @Override public void setColorFilter(ColorFilter colorFilter) {

  }

  @Override
  public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  @Override
  public boolean isRunning() {
    return false;
  }

  @Override
  public void start() {
    mAnimation.reset();
    mParent.startAnimation(mAnimation);
  }

  @Override
  public void stop() {
    mParent.clearAnimation();
    resetOriginals();
  }

  private void setupAnimations() {
    mAnimation = new Animation() {
      @Override
      public void applyTransformation(float interpolatedTime, Transformation t) {
        setRotate(interpolatedTime);
      }
    };
    mAnimation.setRepeatCount(Animation.INFINITE);
    mAnimation.setRepeatMode(Animation.RESTART);
    mAnimation.setInterpolator(LINEAR_INTERPOLATOR);
    mAnimation.setDuration(ANIMATION_DURATION);
  }

}
