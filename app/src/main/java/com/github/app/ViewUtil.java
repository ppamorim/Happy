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

  private static Layer.AnimationSlide backgroundAnimation = new Layer.AnimationSlide() {

    //private Context context;
    //private HappyView happyView;
    //private float dragPercent = 0;
    //private float skyScale = 0;
    //
    //@Override public void animation(Context context, HappyView happyView) {
    //  this.context = context;
    //  this.happyView = happyView;
    //}
    //
    //@Override public void dragPercentage(float dragPercent) {
    //  this.dragPercent = dragPercent;
    //  float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
    //  if (scalePercentDelta > 0) {
    //    skyScale = SKY_INITIAL_SCALE - (SKY_INITIAL_SCALE - 1.0f)
    //        * scalePercentDelta / (1.0f - SCALE_START_PERCENT);
    //  } else {
    //    skyScale = SKY_INITIAL_SCALE;
    //  }
    //}

    @Override public float slideX() {
      return -(mHappyView.getWidth() * skyScale - mHappyView.getWidth()) / 2.0f;
    }

    @Override public float slideY() {
      return (1.0f - dragPercent) * mHappyView.getTotalDragDistance() - (mHappyView.getHeight() * 0.38f)
          - mHappyView.getHeight() * (skyScale - 1.0f) / 2
          + Utils.convertDpToPixel(context, 15) * dragPercent;
    }

    //@Override public float scaleX() {
    //  return skyScale;
    //}
    //
    //@Override public float scaleY() {
    //  return skyScale;
    //}

  };



}
