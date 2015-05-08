package com.github.app.renderers.viewholder;

import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.app.R;
import com.github.app.model.Dimension;
import com.github.ppamorim.recyclerrenderers.viewholder.RenderViewHolder;

public class ViewHolderDimension extends RenderViewHolder<Dimension> {

  @InjectView(R.id.dimension) TextView dimensionName;

  public ViewHolderDimension(View view) {
    super(view);
    ButterKnife.inject(this, view);
  }

  @Override public void onBindView(Dimension dimension) {
    dimensionName.setText(dimension.getName());
  }

}
