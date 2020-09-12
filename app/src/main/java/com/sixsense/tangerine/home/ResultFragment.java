package com.sixsense.tangerine.home;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sixsense.tangerine.R;

public class ResultFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_result, container, false);

        if (getArguments() != null) {
            ResultFragmentArgs args = ResultFragmentArgs.fromBundle(getArguments());
            String recipeName = args.getRecipeName();
            String kindString = args.getKindByte();
            if (kindString == null || kindString.equals("000000")) {
                kindString = "00000011";
            }
            Byte kindByte = binaryStringToByte(kindString);
            Byte levelByte = binaryStringToByte(args.getLevelByte());
            Byte toolByte = binaryStringToByte(args.getToolByte());
            Byte timeByte = binaryStringToByte(args.getTimeByte());

            FlowLayout flowLayout = view.findViewById(R.id.search_tag_layout);
            String[] conditions = args.getConditions();
            Typeface typeface = ResourcesCompat.getFont(view.getContext(), R.font.roboto_medium);
            if (conditions != null) {
                TextView textNew = new TextView(view.getContext());
                textNew.setText("검색된 태그");
                textNew.setTypeface(typeface);
                textNew.setTextSize(14);
                textNew.setTextColor(getResources().getColor(R.color.colorFontDark));
                textNew.setIncludeFontPadding(false);
                textNew.setPadding(20, 20, 20, 20);
                FlowLayout.LayoutParams param = new FlowLayout.LayoutParams(20, 20);
                textNew.setLayoutParams(param);
                flowLayout.addView(textNew);
                for (String str : conditions) {
                    TextView textView = new TextView(view.getContext());
                    textView.setText(str);
                    textView.setTypeface(typeface);
                    textView.setTextSize(14);
                    textView.setTextColor(Color.WHITE);
                    textView.setBackgroundResource(R.drawable.condition_full);
                    textView.setIncludeFontPadding(false);
                    textView.setPadding(20, 20, 20, 20);
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(10, 10);
                    textView.setLayoutParams(params);
                    flowLayout.addView(textView);
                }
            }
            ResultRecipeListFragment resultRecipeListFragment = new ResultRecipeListFragment(recipeName, kindByte, levelByte, toolByte, timeByte);

            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.grid_search_recipe_frame, resultRecipeListFragment).commit();
        }

        return view;
    }

    private byte binaryStringToByte(String s) {
        if (s == null || s.equals("0000")) {
            s = "00001111";
        }
        byte ret = 0, total = 0;
        for (int i = 0; i < 8; ++i) {
            ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0;
            total = (byte) (ret | total);
        }
        return total;
    }
}
