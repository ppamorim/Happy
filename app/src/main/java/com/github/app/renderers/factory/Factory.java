package com.github.app.renderers.factory;

import com.github.app.R;
import com.github.app.renderers.renderers.DimensionRenderer;
import com.github.ppamorim.recyclerrenderers.interfaces.RendererFactory;
import com.github.ppamorim.recyclerrenderers.renderer.Renderer;

public class Factory implements RendererFactory {
  @Override public Renderer getRenderer(int id) {
    switch (id) {
      case R.layout.adapter_dimension:
        return new DimensionRenderer(id);
      default:
        return null;
    }
  }
}
