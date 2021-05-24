package com.xdev.expy.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.xdev.expy.R;
import com.xdev.expy.ui.main.home.ExpiredFragment;
import com.xdev.expy.ui.main.home.MonitoredFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return MonitoredFragment.newInstance();
            case 1: return ExpiredFragment.newInstance();
            default: return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @StringRes
    public final int[] TAB_TITLES = new int[]{
            R.string.monitored,
            R.string.expired
    };
}
