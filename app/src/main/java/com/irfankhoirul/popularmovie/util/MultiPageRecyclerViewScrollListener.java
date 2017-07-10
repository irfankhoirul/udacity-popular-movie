package com.irfankhoirul.popularmovie.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Irfan Khoirul on 7/9/2017.
 */

public class MultiPageRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private LoadNextListener listener;
    private boolean loading;

    public MultiPageRecyclerViewScrollListener(LoadNextListener listener) {
        this.listener = listener;
    }

    public void isLoading(boolean loading) {
        this.loading = loading;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int totalItemCount = adapter.getItemCount();
        int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
//        Log.v("Last Visible Item Pos", String.valueOf(lastVisibleItemPosition));
//        Log.v("Total Item Count", String.valueOf(totalItemCount));

        if ((lastVisibleItemPosition + 1) == totalItemCount) {
            if (!loading) {
                loading = true;
                listener.onStartLoadNext();
            }
        }
    }

    public interface LoadNextListener {
        void onStartLoadNext();
    }
}
