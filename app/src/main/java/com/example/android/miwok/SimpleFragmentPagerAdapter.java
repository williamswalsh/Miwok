package com.example.android.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    /*
    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }*/

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NumbersFragment();
        } else if (position == 1) {
            return new ColorsFragment();
        } else if (position == 2) {
            return new FamilyFragment();
        } else if (position == 3) {
            return new PhrasesFragment();
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.numbers_tab_title);
        } else if (position == 1) {
            return context.getString(R.string.colors_tab_title);
        } else if (position == 2) {
            return context.getString(R.string.family_tab_title);
        } else if (position == 3) {
            return context.getString(R.string.phrases_tab_title);
        } else {
            return null;
        }
    }
}

