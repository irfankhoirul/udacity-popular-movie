package com.irfankhoirul.popularmovie.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Irfan Khoirul on 7/9/2017.
 */

public class MultiPageRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private LoadNextListener listener;
    private boolean loaded;

    public MultiPageRecyclerViewScrollListener(boolean loaded, LoadNextListener listener) {
        this.loaded = loaded;
        this.listener = listener;
    }

    public void isLoading(boolean loading) {
        this.loaded = loading;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int totalItemCount = adapter.getItemCount();
        int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

        if ((lastVisibleItemPosition + 1) == totalItemCount) {
            if (!loaded) {
                loaded = true;
                listener.onStartLoadNext();
            }
        }
    }

    public interface LoadNextListener {
        void onStartLoadNext();
    }
}
