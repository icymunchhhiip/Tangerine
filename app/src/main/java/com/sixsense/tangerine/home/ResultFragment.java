package com.sixsense.tangerine.home;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.sixsense.tangerine.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ResultFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_result, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_result);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        if (getArguments() != null) {
            ResultFragmentArgs args = ResultFragmentArgs.fromBundle(getArguments());
            String[] byteString = new String[5];
            //name, kind, level, tool, time

            byteString[0] = args.getRecipeName();
            final SearchView searchView = view.findViewById(R.id.recipe_search);
            searchView.setQuery(byteString[0],false);
            searchView.setSubmitButtonEnabled(false);
            searchView.setInputType(InputType.TYPE_NULL);
            searchView.setEnabled(false);
            searchView.clearFocus();

            byteString[1] = args.getKindByte();
            if (byteString[1] == null || byteString[1].equals("000000")) {
                byteString[1] = "00000011";
            }

            byteString[2] = args.getLevelByte();
            byteString[3] = args.getToolByte();
            byteString[4] = args.getTimeByte();

            FlexboxLayout flexboxLayout = view.findViewById(R.id.search_tag_layout);
            flexboxLayout.setFlexDirection(FlexDirection.ROW);
            flexboxLayout.setFlexWrap(FlexWrap.WRAP);
            String[] conditions = args.getConditions();
            Typeface typeface = ResourcesCompat.getFont(view.getContext(), R.font.roboto_medium);
            if (conditions != null) {
                for (String str : conditions) {
                    TextView textView = new TextView(view.getContext());
                    textView.setText(str);
                    textView.setTypeface(typeface);
                    textView.setTextSize(14);
                    textView.setTextColor(Color.WHITE);
                    textView.setBackgroundResource(R.drawable.condition_full);
                    textView.setIncludeFontPadding(false);
                    textView.setPadding(20, 20, 20, 20);
                    FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
                    params.setMargins(8,8,8,8);
                    textView.setLayoutParams(params);
                    flexboxLayout.addView(textView);
                }
            }
            RecipeListFragment recipeListFragment = new RecipeListFragment("result",byteString);

            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.grid_search_recipe_frame, recipeListFragment).commit();
        }

        return view;
    }



}
