package com.sixsense.tangerine.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.home.SearchFragment;

public class MainPagerFragment extends Fragment {
    private View mView;
    private ViewPager mViewPager;
    private BottomNavigationView mBottomBar;
    private MainPagerAdapter mMainPagerAdapter;
    private MenuItem prevMenuItem;
    private ConstraintLayout mLayout;
    private View mToolbars;
    private Toolbar mToolbarHome;
    private Toolbar mToolbarRef;
    private Toolbar mToolbarCommunity;
    private Toolbar mToolbarSetting;
    private ImageButton buttonWriting;
//    private SearchView mSearchView;
    private EditText mSearchView;
    private SearchFragment mSearchFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main_pager, container, false);

        mLayout = getActivity().findViewById(R.id.main_layout);
        mToolbars = mLayout.findViewById(R.id.toolbar);
        mToolbars.setVisibility(View.VISIBLE);
        mToolbarHome = mLayout.findViewById(R.id.toolbar_home);
        mToolbarRef = mLayout.findViewById(R.id.toolbar_ref);
        mToolbarCommunity = mLayout.findViewById(R.id.toolbar_community);
        mToolbarSetting = mLayout.findViewById(R.id.toolbar_setting);
        mToolbarHome.setVisibility(View.VISIBLE);
        buttonWriting = mToolbarHome.findViewById(R.id.recipe_write);
        mSearchView = mToolbarHome.findViewById(R.id.recipe_name);

        mViewPager = mView.findViewById(R.id.main_view_pager);
        mBottomBar = mView.findViewById(R.id.main_bottom_bar);
        mMainPagerAdapter = new MainPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mMainPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else {
                    mBottomBar.getMenu().getItem(0).setChecked(false);
                }

                mBottomBar.getMenu().getItem(position).setChecked(true);
                prevMenuItem = mBottomBar.getMenu().getItem(position);
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
                        mToolbarHome.setVisibility(View.VISIBLE);
                        mToolbarRef.setVisibility(View.GONE);
                        mToolbarCommunity.setVisibility(View.GONE);
                        mToolbarSetting.setVisibility(View.GONE);
                        break;
                    case R.id.main_refrigerator:
                        mViewPager.setCurrentItem(1);
                        mToolbarHome.setVisibility(View.GONE);
                        mToolbarRef.setVisibility(View.VISIBLE);
                        mToolbarCommunity.setVisibility(View.GONE);
                        mToolbarSetting.setVisibility(View.GONE);
                        break;
                    case R.id.main_community:
                        mViewPager.setCurrentItem(2);
                        mToolbarHome.setVisibility(View.GONE);
                        mToolbarRef.setVisibility(View.GONE);
                        mToolbarCommunity.setVisibility(View.VISIBLE);
                        mToolbarSetting.setVisibility(View.GONE);
                        break;
                    case R.id.main_setting:
                        mViewPager.setCurrentItem(3);
                        mToolbarHome.setVisibility(View.GONE);
                        mToolbarRef.setVisibility(View.GONE);
                        mToolbarCommunity.setVisibility(View.GONE);
                        mToolbarSetting.setVisibility(View.VISIBLE);
                        break;
                }
                return false;
            }
        });

        return mView;
    }
}