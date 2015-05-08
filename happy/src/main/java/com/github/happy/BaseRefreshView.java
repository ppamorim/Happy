package com.github.happy;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

public abstract class BaseRefreshView extends Drawable implements Drawable.Callback, Animatable {

  private HappyView mRefreshLayout;
  private boolean mEndOfRefreshing;

  public BaseRefreshView(Context context, HappyView layout) {
    mRefreshLayout = layout;
  }

  public Context getContext() {
    return mRefreshLayout != null ? mRefreshLayout.getContext() : null;
  }

  public HappyView getRefreshLayout() {
    return mRefreshLayout;
  }

  @Override public void invalidateDrawable(Drawable who) {
    Callback callback = getCallback();
    if (callback != null) {
      callback.invalidateDrawable(this);
    }
  }

  @Override public void scheduleDrawable(Drawable who, Runnable what, long when) {
    Callback callback = getCallback();
    if (callback != null) {
      callback.scheduleDrawable(this, what, when);
    }
  }

  @Override public void unscheduleDrawable(Drawable who, Runnable what) {
    Callback callback = getCallback();
    if (callback != null) {
      callback.unscheduleDrawable(this, what);
    }
  }

  @Override public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  /**
   * Our animation depend on type of current work of refreshing.
   * We should to do different things when it's end of refreshing
   *
   * @param endOfRefreshing - we will check current state of refresh with this
   */
  public void setEndOfRefreshing(boolean endOfRefreshing) {
    mEndOfRefreshing = endOfRefreshing;
  }

  public abstract void setPercent(float percent, boolean invalidate);
  public abstract void offsetTopAndBottom(int offset);

}