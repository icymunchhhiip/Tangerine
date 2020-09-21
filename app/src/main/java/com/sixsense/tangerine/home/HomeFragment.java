package com.sixsense.tangerine.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.home.write.WriteRecipeActivity;
import com.sixsense.tangerine.main.MainPagerFragmentDirections;


public class HomeFragment extends Fragment {
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.home_, container, false);

        SliderFragment sliderFragment = new SliderFragment();
        RecipeListFragment recentRecipeListFragment = new RecipeListFragment("recent",null);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.slider_frame, sliderFragment).addToBackStack(null);
        fragmentTransaction.replace(R.id.grid_recipe_frame, recentRecipeListFragment).addToBackStack(null);

        fragmentTransaction.commit();

        SearchView searchView = mView.findViewById(R.id.recipe_search);

        searchView.setFocusable(false);
        searchView.clearFocus();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View a_view, boolean a_hasFocus) {
                if (a_hasFocus) {
                        NavDirections action = MainPagerFragmentDirections.actionMainPagerFragmentToSearchFragment();
                        Navigation.findNavController(mView).navigate(action);
                }
            }
        });

        ImageButton imageButton = mView.findViewById(R.id.recipe_write);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WriteRecipeActivity.class);
                startActivity(intent);
            }
        });

        
        return mView;
    }
}
