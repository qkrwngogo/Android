package com.example.originaltest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 4;
    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return Routine.newInstance();
            case 1:
                return Exercise.newInstance();
            case 2:
                return Record.newInstance();
            case 3:
                return Profile.newInstance();
            default:
                return null;
        }
    }

}
