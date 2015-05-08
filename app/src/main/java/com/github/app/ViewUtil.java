package com.github.app;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.github.app.model.Dimension;
import com.github.app.renderers.factory.Factory;
import com.github.happy.BitmapHelper;
import com.github.happy.HappyView;
import com.github.happy.Layer;
import com.github.happy.Utils;
import com.github.ppamorim.recyclerrenderers.adapter.RendererAdapter;
import com.github.ppamorim.recyclerrenderers.builder.RendererBuilder;
import com.github.ppamorim.recyclerrenderers.interfaces.Renderable;
import java.util.ArrayList;
import java.util.Collection;

public class ViewUtil {

  private static final float SCALE_START_PERCENT = 0.5f;
  private static final float SKY_INITIAL_SCALE = 1.05f;

  public static void configRecyclerView(final Context context, RecyclerView recyclerView) {
    recyclerView.setHasFixedSize(true);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setLayoutManager(new LinearLayoutManager(context));
    recyclerView.setAdapter(new RendererAdapter(generateObjects(),
        new RendererBuilder(new Factory())));
  }

  public static ArrayList<Renderable> generateObjects() {
    ArrayList<Renderable> renderables = new ArrayList<>();
    for(int i = 0; i < 100; i++) {
      renderables.add(new Dimension("Zero Dimension"));
      renderables.add(new Dimension("One Dimension"));
      renderables.add(new Dimension("Two Dimension"));
    }
    return renderables;
  }

  public static Collection<Layer> generateLayers(final Context context, final HappyView happyView) {

    ArrayList<Layer> layers = new ArrayList<>();

    layers.add(
    new Layer(BitmapHelper.scaled(context.getResources(), R.drawable.sky, happyView.getWidth(),
        happyView.getHeight()), new Layer.Animation() {
      @Override public float slideX(int dragPercent) {

        float skyScale;
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
          float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
          skyScale = SKY_INITIAL_SCALE - (SKY_INITIAL_SCALE - 1.0f) * scalePercent;
        } else {
          skyScale = SKY_INITIAL_SCALE;
        }

        return (1.0f - dragPercent) * happyView.getTotalDragDistance() - (happyView.getHeight() * 0.38f)
            - happyView.getHeight() * (skyScale - 1.0f) / 2
            + Utils.convertDpToPixel(context, 15) * dragPercent;
      }

      @Override public float slideY(int dragPercent) {
        float skyScale;
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
          float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
          skyScale = SKY_INITIAL_SCALE - (SKY_INITIAL_SCALE - 1.0f) * scalePercent;
        } else {
          skyScale = SKY_INITIAL_SCALE;
        }

        return (1.0f - dragPercent) * happyView.getTotalDragDistance() - (happyView.getHeight() * 0.38f)
            - happyView.getHeight() * (skyScale - 1.0f) / 2
            + Utils.convertDpToPixel(context, 15) * dragPercent;
      }
    }));

    //layers.add(
    //    new Layer(BitmapHelper.scaled(
    //            context.getResources(),
    //            R.drawable.sky,
    //            happyView.getWidth(),
    //            happyView.getHeight())));

    layers.add(
        new Layer(BitmapHelper.scaled(
                context.getResources(),
                R.drawable.sun,
                100,
                100)));

    return layers;
  }



}
