package com.aiyiqi.decoration.lib.style;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

public class LibStyleVPAdapter extends PagerAdapter {
    private final String[] mTitles;
    private final ArrayList<View> mPages;

    public LibStyleVPAdapter(final ArrayList<View> pages, final String[] titles) {
        mPages = pages;
        mTitles = titles;
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        ((ViewPager) collection).addView(mPages.get(position), 0);
        return mPages.get(position);
    }

    @Override
    public int getCount() {
        return mPages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (mTitles == null) ? null : mTitles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

}
