package com.xdev.expy.utils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.xdev.expy.R;

import static com.xdev.expy.utils.AppUtils.showToast;

public class ShimmerHelper {

    private final Context context;
    private final ShimmerFrameLayout shimmer;
    private final RecyclerView recyclerView;
    private final LinearLayout layoutEmpty;

    public ShimmerHelper(Context context, ShimmerFrameLayout shimmer, RecyclerView recyclerView, LinearLayout layoutEmpty){
        this.context = context;
        this.shimmer = shimmer;
        this.recyclerView = recyclerView;
        this.layoutEmpty = layoutEmpty;
        initOnClickListener();
    }

    private void initOnClickListener(){
        shimmer.setOnClickListener(view -> showToast(context, context.getString(R.string.loading)));
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
