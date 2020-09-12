package com.sixsense.tangerine.home;

import android.os.Bundle;
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
import com.sixsense.tangerine.main.MainPagerFragmentDirections;


public class HomeFragment extends Fragment {
    private View mView;
    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.home_, container, false);

        SliderFragment sliderFragment = new SliderFragment();
        RecentRecipeListFragment recentRecipeListFragment = new RecentRecipeListFragment();

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.slider_frame, sliderFragment).addToBackStack(null);
        fragmentTransaction.replace(R.id.grid_recipe_frame, recentRecipeListFragment).addToBackStack(null);

        fragmentTransaction.commit();

        ConstraintLayout layout = getActivity().findViewById(R.id.main_layout);
        mToolbar = layout.findViewById(R.id.toolbar_home);

        SearchView searchView = mToolbar.findViewById(R.id.recipe_search);

        searchView.setFocusable(false);
        searchView.clearFocus();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View a_view, boolean a_hasFocus) {
                if (a_hasFocus) {
                    Fragment current = getChildFragmentManager().findFragmentById(R.id.main_frame);
                    if (!(current instanceof SearchFragment)) {
                        ConstraintLayout layout = getActivity().findViewById(R.id.main_layout);
                        Toolbar toolbar = layout.findViewById(R.id.toolbar_home);
                        ImageButton buttonWriting = toolbar.findViewById(R.id.recipe_write);
                        buttonWriting.setVisibility(View.GONE);

                        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
                        NavDirections action = MainPagerFragmentDirections.actionMainPagerFragmentToSearchFragment();
                        Navigation.findNavController(mView).navigate(action);
                    }
                }
            }
        });
        return mView;
    }
}
