package com.github.happy;

import android.content.Context;

public class Utils {
  public static int convertDpToPixel(Context context, int dp) {
    return Math.round((float) dp * context.getResources().getDisplayMetrics().density);
  }
}
