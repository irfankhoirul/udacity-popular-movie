package com.irfankhoirul.popularmovie.modules.movie_list;

import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Irfan Khoirul on 6/15/2017.
 */

public class RecyclerViewMarginDecoration extends RecyclerView.ItemDecoration {
    private final int columns;
    private int margin;

    public RecyclerViewMarginDecoration(@IntRange(from = 0) int margin, @IntRange(from = 0) int columns) {
        this.margin = margin;
        this.columns = columns;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        outRect.right = margin;
        outRect.bottom = margin;
        if (position < columns) {
            outRect.top = margin;
        }
        if (position % columns == 0) {
            outRect.left = margin;
        }
    }
}