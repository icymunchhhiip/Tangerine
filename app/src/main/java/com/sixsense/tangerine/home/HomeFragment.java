package com.sixsense.tangerine.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.SplashFragmentDirections;
import com.sixsense.tangerine.main.MainPagerFragmentDirections;

public class HomeFragment extends Fragment {
    View mView;
    Toolbar mToolbar;
    ImageButton buttonWriting;
    SearchView mSearchView;
    FragmentTransaction mFragmentTransaction;
    SearchFragment mSearchFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.home_, container, false);

        SliderFragment sliderFragment = new SliderFragment();
        RecipeListFragment recipeListFragment = new RecipeListFragment();

        mFragmentTransaction = getChildFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.slider_frame, sliderFragment);
        mFragmentTransaction.add(R.id.grid_recipe_frame, recipeListFragment);

        mFragmentTransaction.commit();

        ConstraintLayout layout = getActivity().findViewById(R.id.main_layout);
        mToolbar = layout.findViewById(R.id.toolbar_home);
        buttonWriting = mToolbar.findViewById(R.id.recipe_write);
        mSearchView = mToolbar.findViewById(R.id.recipe_search);
        mSearchView.setFocusable(false);
        mSearchView.setIconified(false);
        mSearchView.clearFocus();
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View a_view, boolean a_hasFocus) {
                if(a_hasFocus){
                    mSearchView.setIconified(false);
                    buttonWriting.setVisibility(View.INVISIBLE);
                    mSearchFragment = new SearchFragment();
                    NavDirections action = MainPagerFragmentDirections.actionMainPagerFragmentToSearchFragment();
                    Navigation.findNavController(getActivity(),R.id.main_frame).navigate(action);
                } else {
                    mToolbar.setNavigationIcon(null);
                    buttonWriting.setVisibility(View.VISIBLE);
                    getActivity().onBackPressed();
                }
            }
        });
//        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                mToolbar.setNavigationIcon(null);
//                buttonWriting.setVisibility(View.VISIBLE);
//                getActivity().onBackPressed();
//                return false;
//            }
//        });

        return mView;
    }





}
