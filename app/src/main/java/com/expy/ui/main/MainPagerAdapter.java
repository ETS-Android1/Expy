package com.expy.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.expy.R;
import com.expy.ui.main.home.ExpiredFragment;
import com.expy.ui.main.home.MonitoredFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private final Context context;

    public MainPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new MonitoredFragment();
            case 1: return new ExpiredFragment();
            default: return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.monitored,
            R.string.expired
    };

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return context.getResources().getString(TAB_TITLES[position]);
    }
}
