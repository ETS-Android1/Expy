package com.xdev.expy.utils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

public class ShimmerHelper {

    private final ShimmerFrameLayout shimmer;
    private final RecyclerView recyclerView;
    private final LinearLayout layoutEmpty;

    public ShimmerHelper(ShimmerFrameLayout shimmer, RecyclerView recyclerView, LinearLayout layoutEmpty){
        this.shimmer = shimmer;
        this.recyclerView = recyclerView;
        this.layoutEmpty = layoutEmpty;
    }

    public void show(){
        shimmer.startShimmer();
        shimmer.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        layoutEmpty.setVisibility(View.INVISIBLE);
    }

    public void hide(boolean isEmpty){
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if (isEmpty) layoutEmpty.setVisibility(View.VISIBLE);
    }
}
