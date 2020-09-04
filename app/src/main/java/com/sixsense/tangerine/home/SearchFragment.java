package com.sixsense.tangerine.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.sixsense.tangerine.R;

import java.util.ArrayList;

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

        LinearLayout kindsAllLayout = view.findViewById(R.id.kinds_all_lay);
        LinearLayout kindsMealLayout = view.findViewById(R.id.kinds_meal_lay);
        LinearLayout kindsSnackLayout = view.findViewById(R.id.kinds_snack_lay);
        LinearLayout levelAllLayout = view.findViewById(R.id.level_all_lay);
        LinearLayout levelVeryEasyLayout = view.findViewById(R.id.level_very_easy_lay);
        LinearLayout levelEasyLayout = view.findViewById(R.id.level_easy_lay);
        LinearLayout levelMiddleLayout = view.findViewById(R.id.level_middle_lay);
        LinearLayout levelHardLayout = view.findViewById(R.id.level_hard_lay);
        LinearLayout toolAllLayout = view.findViewById(R.id.tool_all_lay);
        LinearLayout toolFireLayout = view.findViewById(R.id.tool_fire_lay);
        LinearLayout toolMicrowaveLayout = view.findViewById(R.id.tool_microwave_lay);
        LinearLayout toolOvenLayout = view.findViewById(R.id.tool_oven_lay);
        LinearLayout toolAirLayout = view.findViewById(R.id.tool_air_lay);
        LinearLayout timeAllLayout = view.findViewById(R.id.time_all_lay);
        LinearLayout timeUntil30mLayout = view.findViewById(R.id.time_until_30m_lay);
        LinearLayout timeUntil1hLayout = view.findViewById(R.id.time_until_1h_lay);
        LinearLayout timeUntil2hLayout = view.findViewById(R.id.time_until_2h_lay);
        LinearLayout timeOver2hLayout = view.findViewById(R.id.time_over_2h_lay);

        expandViewHitArea(kindsAllLayout, mKindsAll);
        expandViewHitArea(kindsMealLayout, mKindsMeal);
        expandViewHitArea(kindsSnackLayout, mKindsSnack);
        expandViewHitArea(levelAllLayout, mLevelAll);
        expandViewHitArea(levelVeryEasyLayout, mLevelVeryEasy);
        expandViewHitArea(levelEasyLayout, mLevelEasy);
        expandViewHitArea(levelMiddleLayout, mLevelMiddle);
        expandViewHitArea(levelHardLayout, mLevelHard);
        expandViewHitArea(toolAllLayout, mToolAll);
        expandViewHitArea(toolFireLayout, mToolFire);
        expandViewHitArea(toolMicrowaveLayout, mToolMicrowave);
        expandViewHitArea(toolOvenLayout, mToolOven);
        expandViewHitArea(toolAirLayout, mToolAir);
        expandViewHitArea(timeAllLayout, mTimeAll);
        expandViewHitArea(timeUntil30mLayout, mTimeUntil30m);
        expandViewHitArea(timeUntil1hLayout, mTimeUntil1h);
        expandViewHitArea(timeUntil2hLayout, mTimeUntil2h);
        expandViewHitArea(timeOver2hLayout, mTimeOver2h);

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
                ArrayList<String> conditionArray = new ArrayList<String>();
                Log.d("testtestest","in");
                SearchView searchView = getActivity().findViewById(R.id.recipe_search);
                String recipeName="";
                try {
                    recipeName = searchView.getQuery().toString();
                }catch (Exception e){
                }
                String kindCondition = "000000";
                String levelCondition = "0000";
                String toolCondition = "0000";
                String timeCondition = "0000";

                if(mKindsAll.isChecked()){
                    kindCondition +="11";
                    conditionArray.add(mKindsAll.getText().toString());
                }else{
                    if(mKindsMeal.isChecked()){
                        kindCondition+="1";
                        conditionArray.add(mKindsMeal.getText().toString());
                    } else{
                        kindCondition+="0";
                    }
                    if(mKindsSnack.isChecked()){
                        kindCondition+="1";
                        conditionArray.add(mKindsSnack.getText().toString());
                    }else {
                        kindCondition+="0";
                    }
                }
                if(mLevelAll.isChecked()){
                    levelCondition +="1111";
                    conditionArray.add(mLevelAll.getText().toString());
                } else {
                    if(mLevelVeryEasy.isChecked()){
                        levelCondition+="1";
                        conditionArray.add(mLevelVeryEasy.getText().toString());
                    } else{
                        levelCondition+="0";
                    }
                    if(mLevelEasy.isChecked()){
                        levelCondition+="1";
                        conditionArray.add(mLevelEasy.getText().toString());
                    }else{
                        levelCondition+="0";
                    }
                    if(mLevelMiddle.isChecked()){
                        levelCondition+="1";
                        conditionArray.add(mLevelMiddle.getText().toString());
                    }else{
                        levelCondition+="0";
                    }
                    if(mLevelHard.isChecked()){
                        levelCondition+="1";
                        conditionArray.add(mLevelHard.getText().toString());
                    }else{
                        levelCondition+="0";
                    }
                }
                if(mToolAll.isChecked()){
                    toolCondition +="1111";
                    conditionArray.add(mToolAll.getText().toString());
                }else {
                    if(mToolFire.isChecked()){
                        toolCondition+="1";
                        conditionArray.add(mToolFire.getText().toString());
                    }else{
                        toolCondition+="0";
                    }
                    if(mToolMicrowave.isChecked()){
                        toolCondition+="1";
                        conditionArray.add(mToolMicrowave.getText().toString());
                    }  else {
                        toolCondition+="0";
                    }
                    if(mToolOven.isChecked()){
                        toolCondition+="1";
                        conditionArray.add(mToolOven.getText().toString());
                    }else{
                        toolCondition+="0";
                    }
                    if(mToolAir.isChecked()){
                        toolCondition+="1";
                        conditionArray.add( mToolAir.getText().toString());
                    }else{
                        toolCondition+="0";
                    }
                }
                if(mTimeAll.isChecked()){
                    timeCondition +="1111";
                    conditionArray.add(mTimeAll.getText().toString());
                }else {
                    if(mTimeUntil30m.isChecked()){
                        timeCondition+="1";
                        conditionArray.add(mTimeUntil30m.getText().toString());
                    }else{
                        timeCondition+="0";
                    }
                    if(mTimeUntil1h.isChecked()){
                        timeCondition+="1";
                        conditionArray.add(mTimeUntil1h.getText().toString());
                    }else{
                        timeCondition+="0";
                    }
                    if(mTimeUntil2h.isChecked()){
                        timeCondition+="1";
                        conditionArray.add(mTimeUntil2h.getText().toString());
                    }else{
                        timeCondition+="0";
                    }
                    if(mTimeOver2h.isChecked()){
                        timeCondition+="1";
                        conditionArray.add(mTimeOver2h.getText().toString());
                    }else{
                        timeCondition+="0";
                    }
                }
                Log.d("testtestest",recipeName);
//                resultFragment = new ResultFragment(recipeName, kindByte,levelByte,toolByte,timeByte);
                if(kindCondition.equals("00000000")){
                    kindCondition ="00000011";
                    conditionArray.add(mKindsAll.getText().toString());
                }
                if(levelCondition.equals("00000000")){
                        levelCondition ="00001111";
                        conditionArray.add(mLevelAll.getText().toString());
                }
                if(toolCondition.equals("00000000")){
                    toolCondition ="00001111";
                    conditionArray.add(mToolAll.getText().toString());
                }
                if(timeCondition.equals("00000000")){
                    timeCondition ="00001111";
                    conditionArray.add(mTimeAll.getText().toString());
                }

                String[] conditions = new String[conditionArray.size()];
                int i =0;
                for(String s:conditionArray){
                    conditions[i]=s;
                    ++i;
                }
                NavDirections action = SearchFragmentDirections.actionSearchFragmentToResultFragment(recipeName, kindCondition,levelCondition,toolCondition,timeCondition,conditions);
                Navigation.findNavController(view).navigate(action);

            }
        });
        return view;
    }
    private void expandViewHitArea(final View parent, final View child) {

        parent.post(new Runnable() {
            @Override
            public void run() {
                Rect parentRect = new Rect();
                Rect childRect = new Rect();
                parent.getHitRect(parentRect);
                child.getHitRect(childRect);

                childRect.left = 0;
                childRect.top = 0;
                childRect.right = parentRect.width();
                childRect.bottom = parentRect.height();

                parent.setTouchDelegate(new TouchDelegate(childRect, child));
            }
        });
    }

}
