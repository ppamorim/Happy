package com.github.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.github.app.model.Dimension;
import com.github.app.renderers.factory.Factory;
import com.github.happy.BitmapHelper;
import com.github.happy.HappyView;
import com.github.happy.Layer;
import com.github.ppamorim.recyclerrenderers.adapter.RendererAdapter;
import com.github.ppamorim.recyclerrenderers.builder.RendererBuilder;
import com.github.ppamorim.recyclerrenderers.interfaces.Renderable;
import java.util.ArrayList;
import java.util.Collection;

public class ViewUtil {

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

  public static Collection<Layer> generateLayers(Context context, HappyView happyView) {
    ArrayList<Layer> layers = new ArrayList<>();

    layers.add(
        new Layer(
            BitmapHelper.scaled(
                context.getResources(),
                R.drawable.sky,
                happyView.getWidth(),
                happyView.getHeight())));

    layers.add(
        new Layer(
            BitmapHelper.scaled(
                context.getResources(),
                R.drawable.sun,
                100,
                100)));

    return layers;
  }



}
