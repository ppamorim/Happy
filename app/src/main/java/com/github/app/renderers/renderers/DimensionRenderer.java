package com.github.app.renderers.renderers;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.github.app.renderers.viewholder.ViewHolderDimension;
import com.github.ppamorim.recyclerrenderers.renderer.Renderer;
import com.github.ppamorim.recyclerrenderers.viewholder.RenderViewHolder;

public class DimensionRenderer extends Renderer {

  LayoutInflater layoutInflater;

  public DimensionRenderer(int id) {
    super(id);
  }

  @Override public RenderViewHolder onCreateViewHolder(ViewGroup viewGroup, int id) {
    if(layoutInflater == null) {
      layoutInflater = LayoutInflater.from(viewGroup.getContext());
    }
    return new ViewHolderDimension(layoutInflater.inflate(id, viewGroup, false));
  }
}
