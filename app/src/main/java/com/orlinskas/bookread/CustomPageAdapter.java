package com.orlinskas.bookread;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.orlinskas.bookread.fragments.PageFragment;

public class CustomPageAdapter extends FragmentStatePagerAdapter {

    public CustomPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return(PageFragment.newInstance(i, "Привет!"));
    }

    @Override
    public int getCount() {
        return 10;
    }
}
