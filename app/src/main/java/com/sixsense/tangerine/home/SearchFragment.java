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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.sixsense.tangerine.R;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {
    private CheckBox mCheckBoxKindsAll;
    private CheckBox mCheckBoxKindsMeal;
    private CheckBox mCheckBoxKindsSnack;
    private CheckBox mCheckBoxLevelAll;
    private CheckBox mCheckBoxLevelVeryEasy;
    private CheckBox mCheckBoxLevelEasy;
    private CheckBox mCheckBoxLevelMiddle;
    private CheckBox mCheckBoxLevelHard;
    private CheckBox mCheckBoxToolAll;
    private CheckBox mCheckBoxToolFire;
    private CheckBox mCheckBoxToolMicrowave;
    private CheckBox mCheckBoxToolOven;
    private CheckBox mCheckBoxToolAir;
    private CheckBox mCheckBoxTimeAll;
    private CheckBox mCheckBoxTimeUntil30m;
    private CheckBox mCheckBoxTimeUntil1h;
    private CheckBox mCheckBoxTimeUntil2h;
    private CheckBox mCheckBoxTimeOver2h;

    private View view;
    private static final String TAG = SearchFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_search, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_search);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mCheckBoxKindsAll = view.findViewById(R.id.kinds_all);
        mCheckBoxKindsMeal = view.findViewById(R.id.kinds_meal);
        mCheckBoxKindsSnack = view.findViewById(R.id.kinds_snack);

        mCheckBoxLevelAll = view.findViewById(R.id.level_all);
        mCheckBoxLevelVeryEasy = view.findViewById(R.id.level_very_easy);
        mCheckBoxLevelEasy = view.findViewById(R.id.level_easy);
        mCheckBoxLevelMiddle = view.findViewById(R.id.level_middle);
        mCheckBoxLevelHard = view.findViewById(R.id.level_hard);

        mCheckBoxToolAll = view.findViewById(R.id.tool_all);
        mCheckBoxToolFire = view.findViewById(R.id.tool_fire);
        mCheckBoxToolMicrowave = view.findViewById(R.id.tool_microwave);
        mCheckBoxToolOven = view.findViewById(R.id.tool_oven);
        mCheckBoxToolAir = view.findViewById(R.id.tool_air);

        mCheckBoxTimeAll = view.findViewById(R.id.time_all);
        mCheckBoxTimeUntil30m = view.findViewById(R.id.time_until_30m);
        mCheckBoxTimeUntil1h = view.findViewById(R.id.time_until_1h);
        mCheckBoxTimeUntil2h = view.findViewById(R.id.time_until_2h);
        mCheckBoxTimeOver2h = view.findViewById(R.id.time_over_2h);

        mCheckBoxKindsAll.setChecked(true);
        mCheckBoxLevelAll.setChecked(true);
        mCheckBoxToolAll.setChecked(true);
        mCheckBoxTimeAll.setChecked(true);

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

        expandViewHitArea(kindsAllLayout, mCheckBoxKindsAll);
        expandViewHitArea(kindsMealLayout, mCheckBoxKindsMeal);
        expandViewHitArea(kindsSnackLayout, mCheckBoxKindsSnack);
        expandViewHitArea(levelAllLayout, mCheckBoxLevelAll);
        expandViewHitArea(levelVeryEasyLayout, mCheckBoxLevelVeryEasy);
        expandViewHitArea(levelEasyLayout, mCheckBoxLevelEasy);
        expandViewHitArea(levelMiddleLayout, mCheckBoxLevelMiddle);
        expandViewHitArea(levelHardLayout, mCheckBoxLevelHard);
        expandViewHitArea(toolAllLayout, mCheckBoxToolAll);
        expandViewHitArea(toolFireLayout, mCheckBoxToolFire);
        expandViewHitArea(toolMicrowaveLayout, mCheckBoxToolMicrowave);
        expandViewHitArea(toolOvenLayout, mCheckBoxToolOven);
        expandViewHitArea(toolAirLayout, mCheckBoxToolAir);
        expandViewHitArea(timeAllLayout, mCheckBoxTimeAll);
        expandViewHitArea(timeUntil30mLayout, mCheckBoxTimeUntil30m);
        expandViewHitArea(timeUntil1hLayout, mCheckBoxTimeUntil1h);
        expandViewHitArea(timeUntil2hLayout, mCheckBoxTimeUntil2h);
        expandViewHitArea(timeOver2hLayout, mCheckBoxTimeOver2h);

        CheckBox.OnCheckedChangeListener onCheckedChangeListener = new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch (buttonView.getId()) {
                        case R.id.kinds_all:
                            mCheckBoxKindsMeal.setChecked(false);
                            mCheckBoxKindsSnack.setChecked(false);
                            break;
                        case R.id.kinds_meal:
                        case R.id.kinds_snack:
                            mCheckBoxKindsAll.setChecked(false);
                            break;
                        case R.id.level_all:
                            mCheckBoxLevelVeryEasy.setChecked(false);
                            mCheckBoxLevelEasy.setChecked(false);
                            mCheckBoxLevelMiddle.setChecked(false);
                            mCheckBoxLevelHard.setChecked(false);
                            break;
                        case R.id.level_very_easy:
                        case R.id.level_easy:
                        case R.id.level_middle:
                        case R.id.level_hard:
                            mCheckBoxLevelAll.setChecked(false);
                            break;
                        case R.id.tool_all:
                            mCheckBoxToolFire.setChecked(false);
                            mCheckBoxToolMicrowave.setChecked(false);
                            mCheckBoxToolOven.setChecked(false);
                            mCheckBoxToolAir.setChecked(false);
                            break;
                        case R.id.tool_fire:
                        case R.id.tool_microwave:
                        case R.id.tool_oven:
                        case R.id.tool_air:
                            mCheckBoxToolAll.setChecked(false);
                            break;
                        case R.id.time_all:
                            mCheckBoxTimeUntil30m.setChecked(false);
                            mCheckBoxTimeUntil1h.setChecked(false);
                            mCheckBoxTimeUntil2h.setChecked(false);
                            mCheckBoxTimeOver2h.setChecked(false);
                            break;
                        case R.id.time_until_30m:
                        case R.id.time_until_1h:
                        case R.id.time_until_2h:
                        case R.id.time_over_2h:
                            mCheckBoxTimeAll.setChecked(false);
                            break;
                    }
                }
            }
        };

        mCheckBoxKindsAll.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxKindsMeal.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxKindsSnack.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxLevelAll.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxLevelVeryEasy.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxLevelEasy.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxLevelMiddle.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxLevelHard.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxToolAll.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxToolFire.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxToolMicrowave.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxToolOven.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxToolAir.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxTimeAll.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxTimeUntil30m.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxTimeUntil1h.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxTimeUntil2h.setOnCheckedChangeListener(onCheckedChangeListener);
        mCheckBoxTimeOver2h.setOnCheckedChangeListener(onCheckedChangeListener);


        Button button = view.findViewById(R.id.search_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> conditionArray = new ArrayList<>();
                SearchView searchView = view.findViewById(R.id.recipe_search);
                String recipeName = "";
                try {
                    recipeName = searchView.getQuery().toString();
                } catch (Exception e) {
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                }
                StringBuilder kindCondition = new StringBuilder("00000000");
                StringBuilder levelCondition = new StringBuilder("00000000");
                StringBuilder toolCondition = new StringBuilder("00000000");
                StringBuilder timeCondition = new StringBuilder("00000000");

                if (mCheckBoxKindsMeal.isChecked()) {
                    kindCondition.setCharAt(7, '1');
                    conditionArray.add(mCheckBoxKindsMeal.getText().toString());
                }
                if (mCheckBoxKindsSnack.isChecked()) {
                    kindCondition.setCharAt(6, '1');
                    conditionArray.add(mCheckBoxKindsSnack.getText().toString());
                }

                if (mCheckBoxLevelVeryEasy.isChecked()) {
                    levelCondition.setCharAt(7,'1');
                    conditionArray.add(mCheckBoxLevelVeryEasy.getText().toString());
                }
                if (mCheckBoxLevelEasy.isChecked()) {
                    levelCondition.setCharAt(6,'1');
                    conditionArray.add(mCheckBoxLevelEasy.getText().toString());
                }
                if (mCheckBoxLevelMiddle.isChecked()) {
                    levelCondition.setCharAt(5,'1');
                    conditionArray.add(mCheckBoxLevelMiddle.getText().toString());
                }
                if (mCheckBoxLevelHard.isChecked()) {
                    levelCondition.setCharAt(4,'1');
                    conditionArray.add(mCheckBoxLevelHard.getText().toString());
                }

                if (mCheckBoxToolFire.isChecked()) {
                    toolCondition.setCharAt(7,'1');
                    conditionArray.add(mCheckBoxToolFire.getText().toString());
                }
                if (mCheckBoxToolMicrowave.isChecked()) {
                    toolCondition.setCharAt(6,'1');
                    conditionArray.add(mCheckBoxToolMicrowave.getText().toString());
                }
                if (mCheckBoxToolOven.isChecked()) {
                    toolCondition.setCharAt(5,'1');
                    conditionArray.add(mCheckBoxToolOven.getText().toString());
                }
                if (mCheckBoxToolAir.isChecked()) {
                    toolCondition.setCharAt(4,'1');
                    conditionArray.add(mCheckBoxToolAir.getText().toString());
                }

                if (mCheckBoxTimeUntil30m.isChecked()) {
                    timeCondition.setCharAt(7,'1');
                    conditionArray.add(mCheckBoxTimeUntil30m.getText().toString());
                }
                if (mCheckBoxTimeUntil1h.isChecked()) {
                    timeCondition.setCharAt(6,'1');
                    conditionArray.add(mCheckBoxTimeUntil1h.getText().toString());
                }
                if (mCheckBoxTimeUntil2h.isChecked()) {
                    timeCondition.setCharAt(5,'1');
                    conditionArray.add(mCheckBoxTimeUntil2h.getText().toString());
                }
                if (mCheckBoxTimeOver2h.isChecked()) {
                    timeCondition.setCharAt(4,'1');
                    conditionArray.add(mCheckBoxTimeOver2h.getText().toString());
                }


                if (mCheckBoxKindsAll.isChecked()||kindCondition.toString().equals("00000000")) {
                    kindCondition.setCharAt(6, '1');
                    kindCondition.setCharAt(7, '1');
                    conditionArray.add(mCheckBoxKindsAll.getText().toString());
                }
                if (mCheckBoxLevelAll.isChecked()||levelCondition.toString().equals("00000000")) {
                    levelCondition.setCharAt(4,'1');
                    levelCondition.setCharAt(5,'1');
                    levelCondition.setCharAt(6,'1');
                    levelCondition.setCharAt(7,'1');
                    conditionArray.add(mCheckBoxLevelAll.getText().toString());
                }
                if (mCheckBoxToolAll.isChecked()||toolCondition.toString().equals("00000000")) {
                    toolCondition.setCharAt(4,'1');
                    toolCondition.setCharAt(5,'1');
                    toolCondition.setCharAt(6,'1');
                    toolCondition.setCharAt(7,'1');
                    conditionArray.add(mCheckBoxToolAll.getText().toString());
                }
                if (mCheckBoxTimeAll.isChecked()||timeCondition.toString().equals("00000000")) {
                    timeCondition.setCharAt(4,'1');
                    timeCondition.setCharAt(5,'1');
                    timeCondition.setCharAt(6,'1');
                    timeCondition.setCharAt(7,'1');
                    conditionArray.add(mCheckBoxTimeAll.getText().toString());
                }

                String[] conditions = new String[conditionArray.size()];
                int i = 0;
                for (String s : conditionArray) {
                    conditions[i] = s;
                    ++i;
                }
                NavDirections navDirections =
                        SearchFragmentDirections
                                .actionSearchFragmentToResultFragment(recipeName,
                                        kindCondition.toString(), levelCondition.toString(),
                                        toolCondition.toString(), timeCondition.toString(), conditions);
                Navigation.findNavController(view).navigate(navDirections);

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
