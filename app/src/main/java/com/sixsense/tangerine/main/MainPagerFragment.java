package com.sixsense.tangerine.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sixsense.tangerine.R;

public class MainPagerFragment extends Fragment {
    private ViewPager mViewPager;
    private BottomNavigationView mBottomBar;
    private MenuItem mMenuItemPrev;
    private Toolbar mToolbarHome;
    private Toolbar mToolbarSetting;
    private View toolbars;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_pager, container, false);

        ConstraintLayout layout = getActivity().findViewById(R.id.main_layout);
        toolbars = layout.findViewById(R.id.toolbar);
        toolbars.setVisibility(View.VISIBLE);
        mToolbarHome = layout.findViewById(R.id.toolbar_home);
        mToolbarSetting = layout.findViewById(R.id.toolbar_setting);
        mToolbarHome.setVisibility(View.VISIBLE);

        mViewPager = view.findViewById(R.id.main_view_pager);
        mBottomBar = view.findViewById(R.id.main_bottom_bar);
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mainPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mMenuItemPrev != null) {
                    mMenuItemPrev.setChecked(false);
                }

                mBottomBar.getMenu().getItem(position).setChecked(true);
                switch (position) {
                    case 0:
                        toolbars.setVisibility(View.VISIBLE);
                        mToolbarHome.setVisibility(View.VISIBLE);
                        mToolbarSetting.setVisibility(View.GONE);
                        break;
                    case 1:
                    case 2:
                        toolbars.setVisibility(View.GONE);
                        break;
                    case 3:
                        toolbars.setVisibility(View.VISIBLE);
                        mToolbarHome.setVisibility(View.GONE);
                        mToolbarSetting.setVisibility(View.VISIBLE);
                        break;
                }
                mMenuItemPrev = mBottomBar.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.main_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.main_refrigerator:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.main_community:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.main_setting:
                        mViewPager.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });

        return view;
    }
}