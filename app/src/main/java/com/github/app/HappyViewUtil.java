package com.github.app;

import android.content.Context;
import com.github.happy.BitmapHelper;
import com.github.happy.HappyView;
import com.github.happy.Layer;
import java.util.ArrayList;
import java.util.Collection;

public class HappyViewUtil {

  private final static float SKY_RATIO = 0.65f;

  private Context context;
  private HappyView happyView;

  public HappyViewUtil(Context context, HappyView happyView) {
    this.context = context;
    this.happyView = happyView;
  }

  public Collection<Layer> generateLayers() {
    ArrayList<Layer> layers = new ArrayList<>();
    layers.add(new Layer(
        BitmapHelper.scale(
            context.getResources(),
            R.drawable.sky, happyView.getWidth(),
            (int) (happyView.getHeight() * SKY_RATIO)),
            null,
            animationSlide));
    return layers;
  }

  private Layer.AnimationSlide animationSlide = new Layer.AnimationSlide() {
    @Override public float slideX() {
      return 0;
    }

    @Override public float slideY() {
      return 0;
    }
  };

}
