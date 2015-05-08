package com.github.app.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import butterknife.InjectView;
import com.github.app.R;
import com.github.app.ViewUtil;
import com.github.happy.HappyView;
import com.github.happy.OnRefreshListener;

public class HappyActivity extends AbstractActivity {

  private static final int DELAY = 2000;

  @InjectView(R.id.recycler_view) RecyclerView recyclerView;
  @InjectView(R.id.happy_view) HappyView happyView;

  @Override protected int getContentViewId() {
    return R.layout.activity_happy;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ViewUtil.configRecyclerView(this, recyclerView);
    happyView.post(new Runnable() {
      @Override public void run() {
        happyView.setLayers(ViewUtil.generateLayers(getApplication(), happyView));
        happyView.setOnRefreshListener(onRefreshListener);
        happyView.start();
      }
    });
  }

  private OnRefreshListener onRefreshListener = new OnRefreshListener() {
    @Override public void onRefresh() {
      happyView.postDelayed(new Runnable() {
        @Override public void run() {
          happyView.setRefreshing(false);
        }
      }, DELAY);
    }
  };

}
