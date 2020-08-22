package com.sixsense.tangerine.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.sixsense.tangerine.R;

public class SearchFragment extends Fragment {
    private CheckBox mKindsAll;
    private CheckBox mKindsMeal;
    private CheckBox mKindsSnack;
    private CheckBox mLevelAll;
    private CheckBox mLevelVeryEasy;
    private CheckBox mLevelEasy;
    private CheckBox mLevelMiddle;
    private CheckBox mLevelHard;
    private CheckBox mToolAll;
    private CheckBox mToolFire;
    private CheckBox mToolMicrowave;
    private CheckBox mToolOven;
    private CheckBox mToolAir;
    private CheckBox mTimeAll;
    private CheckBox mTimeUntil30m;
    private CheckBox mTimeUntil1h;
    private CheckBox mTimeUntil2h;
    private CheckBox mTimeOver2h;

    private ResultFragment resultFragment;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_search, container, false);

        mKindsAll = view.findViewById(R.id.kinds_all);
        mKindsMeal = view.findViewById(R.id.kinds_meal);
        mKindsSnack = view.findViewById(R.id.kinds_snack);

        mLevelAll = view.findViewById(R.id.level_all);
        mLevelVeryEasy = view.findViewById(R.id.level_very_easy);
        mLevelEasy = view.findViewById(R.id.level_easy);
        mLevelMiddle = view.findViewById(R.id.level_middle);
        mLevelHard = view.findViewById(R.id.level_hard);

        mToolAll = view.findViewById(R.id.tool_all);
        mToolFire = view.findViewById(R.id.tool_fire);
        mToolMicrowave = view.findViewById(R.id.tool_microwave);
        mToolOven = view.findViewById(R.id.tool_oven);
        mToolAir = view.findViewById(R.id.tool_air);

        mTimeAll = view.findViewById(R.id.time_all);
        mTimeUntil30m = view.findViewById(R.id.time_until_30m);
        mTimeUntil1h = view.findViewById(R.id.time_until_1h);
        mTimeUntil2h = view.findViewById(R.id.time_until_2h);
        mTimeOver2h = view.findViewById(R.id.time_over_2h);

        mKindsAll.setChecked(true);
        mLevelAll.setChecked(true);
        mToolAll.setChecked(true);
        mTimeAll.setChecked(true);


        CheckBox.OnCheckedChangeListener onCheckedChangeListener = new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    switch (buttonView.getId()) {
                        case R.id.kinds_all:
                            mKindsMeal.setChecked(false);
                            mKindsSnack.setChecked(false);
                            break;
                        case R.id.kinds_meal:
                        case R.id.kinds_snack:
                            mKindsAll.setChecked(false);
                            break;
                        case R.id.level_all:
                            mLevelVeryEasy.setChecked(false);
                            mLevelEasy.setChecked(false);
                            mLevelMiddle.setChecked(false);
                            mLevelHard.setChecked(false);
                            break;
                        case R.id.level_very_easy:
                        case R.id.level_easy:
                        case R.id.level_middle:
                        case R.id.level_hard:
                            mLevelAll.setChecked(false);
                            break;
                        case R.id.tool_all:
                            mToolFire.setChecked(false);
                            mToolMicrowave.setChecked(false);
                            mToolOven.setChecked(false);
                            mToolAir.setChecked(false);
                            break;
                        case R.id.tool_fire:
                        case R.id.tool_microwave:
                        case R.id.tool_oven:
                        case R.id.tool_air:
                            mToolAll.setChecked(false);
                            break;
                        case R.id.time_all:
                            mTimeUntil30m.setChecked(false);
                            mTimeUntil1h.setChecked(false);
                            mTimeUntil2h.setChecked(false);
                            mTimeOver2h.setChecked(false);
                            break;
                        case R.id.time_until_30m:
                        case R.id.time_until_1h:
                        case R.id.time_until_2h:
                        case R.id.time_over_2h:
                            mTimeAll.setChecked(false);
                            break;
                    }
                }
            }
        };

        mKindsAll.setOnCheckedChangeListener(onCheckedChangeListener);
        mKindsMeal.setOnCheckedChangeListener(onCheckedChangeListener);
        mKindsSnack.setOnCheckedChangeListener(onCheckedChangeListener);
        mLevelAll.setOnCheckedChangeListener(onCheckedChangeListener);
        mLevelVeryEasy.setOnCheckedChangeListener(onCheckedChangeListener);
        mLevelEasy.setOnCheckedChangeListener(onCheckedChangeListener);
        mLevelMiddle.setOnCheckedChangeListener(onCheckedChangeListener);
        mLevelHard.setOnCheckedChangeListener(onCheckedChangeListener);
        mToolAll.setOnCheckedChangeListener(onCheckedChangeListener);
        mToolFire.setOnCheckedChangeListener(onCheckedChangeListener);
        mToolMicrowave.setOnCheckedChangeListener(onCheckedChangeListener);
        mToolOven.setOnCheckedChangeListener(onCheckedChangeListener);
        mToolAir.setOnCheckedChangeListener(onCheckedChangeListener);
        mTimeAll.setOnCheckedChangeListener(onCheckedChangeListener);
        mTimeUntil30m.setOnCheckedChangeListener(onCheckedChangeListener);
        mTimeUntil1h.setOnCheckedChangeListener(onCheckedChangeListener);
        mTimeUntil2h.setOnCheckedChangeListener(onCheckedChangeListener);
        mTimeOver2h.setOnCheckedChangeListener(onCheckedChangeListener);



        Button button = view.findViewById(R.id.search_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("testtestest","in");
                SearchView searchView = getActivity().findViewById(R.id.recipe_search);
                String recipeName="";
                try {
                    recipeName = searchView.getQuery().toString();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("testtestest","err");
                }
                String kindCondition = "000000";
                String levelCondition = "0000";
                String toolCondition = "0000";
                String timeCondition = "0000";

                if(mKindsAll.isChecked()){
                    kindCondition +="11";
                }else{
                    if(mKindsMeal.isChecked()){
                        kindCondition+="1";
                    } else{
                        kindCondition+="0";
                    }
                    if(mKindsSnack.isChecked()){
                        kindCondition+="1";
                    }else {
                        kindCondition+="0";
                    }
                }
                if(mLevelAll.isChecked()){
                    levelCondition +="1111";
                } else {
                    if(mLevelVeryEasy.isChecked()){
                        levelCondition+="1";
                    } else{
                        levelCondition+="0";
                    }
                    if(mLevelEasy.isChecked()){
                        levelCondition+="1";
                    }else{
                        levelCondition+="0";
                    }
                    if(mLevelMiddle.isChecked()){
                        levelCondition+="1";
                    }else{
                        levelCondition+="0";
                    }
                    if(mLevelHard.isChecked()){
                        levelCondition+="1";
                    }else{
                        levelCondition+="0";
                    }
                }
                if(mToolAll.isChecked()){
                    toolCondition +="1111";
                }else {
                    if(mToolFire.isChecked()){
                        toolCondition+="1";
                    }else{
                        toolCondition+="0";
                    }
                    if(mToolMicrowave.isChecked()){
                        toolCondition+="1";
                    }  else {
                        toolCondition+="0";
                    }
                    if(mToolOven.isChecked()){
                        toolCondition+="1";
                    }else{
                        toolCondition+="0";
                    }
                    if(mToolAir.isChecked()){
                        toolCondition+="1";
                    }else{
                        toolCondition+="0";
                    }
                }
                if(mTimeAll.isChecked()){
                    timeCondition +="1111";
                }else {
                    if(mTimeUntil30m.isChecked()){
                        timeCondition+="1";
                    }else{
                        timeCondition+="0";
                    }
                    if(mTimeUntil1h.isChecked()){
                        timeCondition+="1";
                    }else{
                        timeCondition+="0";
                    }
                    if(mTimeUntil2h.isChecked()){
                        timeCondition+="1";
                    }else{
                        timeCondition+="0";
                    }
                    if(mTimeOver2h.isChecked()){
                        timeCondition+="1";
                    }else{
                        timeCondition+="0";
                    }
                }
                Log.d("testtestest",recipeName);
//                resultFragment = new ResultFragment(recipeName, kindByte,levelByte,toolByte,timeByte);
                NavDirections action = SearchFragmentDirections.actionSearchFragmentToResultFragment(recipeName, kindCondition,levelCondition,toolCondition,timeCondition);
                Navigation.findNavController(getActivity(),R.id.main_frame).navigate(action);

            }
        });
        return view;
    }

}
