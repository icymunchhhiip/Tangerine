package com.sixsense.tangerine.home;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.HttpInterface;
import com.sixsense.tangerine.network.RecipeIntroList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ResultFragment extends Fragment {

    private String recipeName;
    private Byte kindByte;
    private Byte levelByte;
    private Byte toolByte;
    private Byte timeByte;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_result,container,false);

        if(getArguments()!=null){
            ResultFragmentArgs args = ResultFragmentArgs.fromBundle(getArguments());
            recipeName = args.getRecipeName();
            kindByte = binaryStringToByte(args.getKindByte());
            levelByte = binaryStringToByte(args.getLevelByte());
            toolByte = binaryStringToByte(args.getToolByte());
            timeByte = binaryStringToByte(args.getTimeByte());
            ResultRecipeListFragment resultRecipeListFragment = new ResultRecipeListFragment(recipeName,kindByte,levelByte,toolByte,timeByte);

            FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
            mFragmentTransaction.add(R.id.grid_search_recipe_frame, resultRecipeListFragment);

            mFragmentTransaction.commit();
        }
//        ResultRecipeListFragment searchRecipeListFragment = new ResultRecipeListFragment();
//        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.grid_search_recipe_frame, searchRecipeListFragment);
//        fragmentTransaction.commit();

        return view;
    }



    private byte binaryStringToByte(String s) {
        byte ret = 0, total = 0;
        for (int i = 0; i < 8; ++i) {
            ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0;
            total = (byte) (ret | total);
        }
        return total;
    }
}
