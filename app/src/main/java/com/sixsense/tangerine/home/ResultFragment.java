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
//import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sixsense.tangerine.R;

public class ResultFragment extends Fragment {

    private String recipeName;
    private Byte kindByte;
    private Byte levelByte;
    private Byte toolByte;
    private Byte timeByte;
    private Typeface typeface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_result,container,false);

        if(getArguments()!=null){
            ResultFragmentArgs args = ResultFragmentArgs.fromBundle(getArguments());
            recipeName = args.getRecipeName();
            String kindString = args.getKindByte();
            if(kindString ==null ||kindString.equals("000000")){
                kindString="00000011";
            }
            kindByte = binaryStringToByte(kindString);
            levelByte = binaryStringToByte(args.getLevelByte());
            toolByte = binaryStringToByte(args.getToolByte());
            timeByte = binaryStringToByte(args.getTimeByte());
            FlowLayout flowLayout = view.findViewById(R.id.search_tag_layout);
            String[] conditions = args.getConditions();
            typeface= ResourcesCompat.getFont(view.getContext(), R.font.roboto_medium);
            if(conditions!= null) {
                TextView textView = new TextView(view.getContext());
                textView.setText("검색된 태그");
                textView.setTypeface(typeface);
                textView.setTextSize(14);
                textView.setTextColor(getResources().getColor(R.color.colorFontDark));
                textView.setIncludeFontPadding(false);
                textView.setPadding(20, 20, 20, 20);
                FlowLayout.LayoutParams param = new FlowLayout.LayoutParams(20, 20);
                textView.setLayoutParams(param);
                flowLayout.addView(textView);
                for (String s : conditions) {
                    TextView tv = new TextView(view.getContext());
                    tv.setText(s);
                    tv.setTypeface(typeface);
                    tv.setTextSize(14);
                    tv.setTextColor(Color.WHITE);
                    tv.setBackgroundResource(R.drawable.condition_full);
                    tv.setIncludeFontPadding(false);
                    tv.setPadding(20, 20, 20, 20);
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(10, 10);
                    tv.setLayoutParams(params);
                    flowLayout.addView(tv);
                }
            }
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
        if(s==null || s.equals("0000")){
            s="00001111";
        }
        byte ret = 0, total = 0;
        for (int i = 0; i < 8; ++i) {
            ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0;
            total = (byte) (ret | total);
        }
        return total;
    }
}
