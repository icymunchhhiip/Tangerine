package com.sixsense.tangerine.main;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sixsense.tangerine.community.CommunityFragment;
import com.sixsense.tangerine.ref.RefrigeratorFragment;
import com.sixsense.tangerine.setting.SettingFragment;
import com.sixsense.tangerine.home.HomeFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_ITEMS_MAIN_PAGER = 4;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_REF = 1;
    private static final int FRAGMENT_COMMUNITY = 2;
    private static final int FRAGMENT_SETTING = 3;

    public MainPagerAdapter(FragmentManager fm){
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case FRAGMENT_HOME:
                return new HomeFragment();
            case FRAGMENT_REF:
                return new RefrigeratorFragment();
            case FRAGMENT_COMMUNITY:
                return new CommunityFragment();
            case FRAGMENT_SETTING:
                return new SettingFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getCount() {
        return NUM_ITEMS_MAIN_PAGER;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
    }
//instanse item?
}
