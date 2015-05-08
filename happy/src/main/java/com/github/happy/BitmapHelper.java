package com.github.happy;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapHelper {

  public static Bitmap scaled(Resources resources, int resourceId, int width, int height) {
    return Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, resourceId),
        width,
        height,
        true);
  }

}
