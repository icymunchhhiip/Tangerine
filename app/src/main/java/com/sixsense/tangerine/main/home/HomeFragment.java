package com.sixsense.tangerine.main.home;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.main.home.util.ExpandableHeightGridView;
import com.sixsense.tangerine.main.home.util.RecipeGridAdapter;
import com.sixsense.tangerine.network.MainEventList;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.HttpInterface;
import com.sixsense.tangerine.network.RecipeIntroList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_home, container, false);

        SliderFragment sliderFragment = new SliderFragment();
        RecipeGridFragment recipeGridFragment = new RecipeGridFragment();

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.slider_frame, sliderFragment);
        fragmentTransaction.add(R.id.grid_recipe_frame, recipeGridFragment);

        fragmentTransaction.commit();

        return view;
    }




}
