package com.chanlin.jetsencloud.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by ChanLin on 2018/1/11.
 * jetsenCloud
 * TODO:
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;
    public MainFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
