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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_pager, container, false);

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