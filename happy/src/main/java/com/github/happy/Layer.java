package com.github.happy;

import android.graphics.Bitmap;

public class Layer {

  private Bitmap bitmap;

  public Layer(Bitmap bitmap) {
    this.bitmap = bitmap;
  }

  public Bitmap getBitmap() {
    return bitmap;
  }

  public void setBitmap(Bitmap bitmap) {
    this.bitmap = bitmap;
  }

}
