package com.xdev.expy.main;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.xdev.expy.R;
import com.xdev.expy.main.home.ExpiredFragment;
import com.xdev.expy.main.home.MonitoredFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    private static final int TRANSACTION_SCREENS_NUMBER = 2;
    
    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return MonitoredFragment.newInstance();
            case 1: return ExpiredFragment.newInstance();
            default: throw new IllegalStateException("Invalid adapter position");
        }
    }

    @Override
    public int getItemCount() {
        return TRANSACTION_SCREENS_NUMBER;
    }

    @StringRes
    public final int[] TAB_TITLES = new int[]{
            R.string.monitored,
            R.string.expired
    };
}
