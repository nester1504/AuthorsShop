package com.nesterenko.authorsshop.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nesterenko.authorsshop.Fragment.MyAnnouncementFragment;
import com.nesterenko.authorsshop.Fragment.MyArchiveFragment;

public class PagerAdapter extends FragmentPagerAdapter {


    private int mNoOfTabs;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int mNoOfTabs) {
        super(fm, behavior);
        this.mNoOfTabs = mNoOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new MyAnnouncementFragment();
            case 1:
                return new MyArchiveFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
