package com.github.happy;

import android.graphics.Bitmap;

public class Layer {

  private Bitmap bitmap;
  private Animation animation;

  public Layer(Bitmap bitmap) {
    this.bitmap = bitmap;
  }

  public Layer(Bitmap bitmap, Animation animation) {
    this.bitmap = bitmap;
    this.animation = animation;
  }

  public Bitmap getBitmap() {
    return bitmap;
  }

  public void setBitmap(Bitmap bitmap) {
    this.bitmap = bitmap;
  }

  public Animation getAnimation() {
    return animation;
  }

  public interface Animation {
    float slideX(float dragPercent);
    float slideY(float dragPercent);
    float scaleX(float dragPercent);
    float scaleY(float dragPercent);
  }

}
