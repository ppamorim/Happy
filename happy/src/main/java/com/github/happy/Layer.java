package com.github.happy;

import android.graphics.Bitmap;

public class Layer {

  private Bitmap bitmap;
  private AnimationScale animationScale;
  private AnimationSlide animationSlide;

  public Layer(Bitmap bitmap) {
    this.bitmap = bitmap;
  }

  public Layer(Bitmap bitmap, AnimationScale animationScale, AnimationSlide animationSlide) {
    this.bitmap = bitmap;
    this.animationScale = animationScale;
    this.animationSlide = animationSlide;
  }

  public Bitmap getBitmap() {
    return bitmap;
  }

  public void setBitmap(Bitmap bitmap) {
    this.bitmap = bitmap;
  }

  public AnimationScale getAnimationScale() {
    return animationScale;
  }

  public AnimationSlide getAnimationSlide() {
    return animationSlide;
  }

  public interface AnimationScale {
    float scaleX();
    float scaleY();
  }

  public interface AnimationSlide {
    float slideX();
    float slideY();
  }

}
