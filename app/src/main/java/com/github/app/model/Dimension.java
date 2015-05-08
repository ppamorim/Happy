package com.github.app.model;

import com.github.app.R;
import com.github.ppamorim.recyclerrenderers.interfaces.Renderable;

public class Dimension implements Renderable {

  private String name;

  public Dimension(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override public int getRenderableResourceId(int i) {
    return R.layout.adapter_dimension;
  }

}
