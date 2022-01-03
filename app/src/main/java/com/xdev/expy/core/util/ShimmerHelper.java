package com.xdev.expy.core.util;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;

public class ShimmerHelper {

    private final ShimmerFrameLayout shimmer;
    private final RecyclerView recyclerView;
    private final LinearLayout layoutEmpty;
    private final SwipeRefreshLayout swipeRefresh;

    public ShimmerHelper(ShimmerFrameLayout shimmer, RecyclerView recyclerView, LinearLayout layoutEmpty, SwipeRefreshLayout swipeRefresh) {
        this.shimmer = shimmer;
        this.recyclerView = recyclerView;
        this.layoutEmpty = layoutEmpty;
        this.swipeRefresh = swipeRefresh;
    }

    public void show() {
        shimmer.startShimmer();
        shimmer.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        layoutEmpty.setVisibility(View.INVISIBLE);
    }

    public void hide(boolean isEmpty) {
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        if (isEmpty) {
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }
        swipeRefresh.setRefreshing(false);
    }
}
