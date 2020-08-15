package com.sixsense.tangerine.main.home.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.RecipeIntroList;

import java.util.ArrayList;
import java.util.List;

public class RecipeGridAdapter extends BaseAdapter {
    private Context context;
    private List<RecipeIntroList.RecipeIntro> recipeIntro;
    private String recipeImageURL = HttpClient.BASE_URL+"recipe/imgs/";

    public RecipeGridAdapter(Context context, List<RecipeIntroList.RecipeIntro> recipeIntro) {
        this.context = context;
        this.recipeIntro = recipeIntro;
    }

    @Override
    public int getCount() {
        return recipeIntro.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View grid;
        if (convertView == null) {

            grid = new View(context);
            if (inflater != null) {
                grid = inflater.inflate(R.layout.home_recipe_item, null);
            }
            ImageView recipeImg = grid.findViewById(R.id.recipe_img);
            Glide.with(grid.getContext())
                    .load(recipeImageURL+recipeIntro.get(position).recipe_img)
                    .into(recipeImg);

            TextView recipeName = grid.findViewById(R.id.recipe_name);
            recipeName.setText(recipeIntro.get(position).recipe_name);

            TextView recipeMin = grid.findViewById(R.id.recipe_min);
            recipeMin.setText(recipeIntro.get(position).recipe_min);

            TextView recipeTags = grid.findViewById(R.id.recipe_tags);
            recipeTags.setText(recipeIntro.get(position).recipe_tags);

            ImageView memProfile = grid.findViewById(R.id.mem_profile);
            Glide.with(grid.getContext())
                    .load(recipeIntro.get(position).mem_profile)
                    .into(memProfile);

            TextView memName = grid.findViewById(R.id.mem_name);
            memName.setText(recipeIntro.get(position).mem_name);

            ImageView recipeFav = grid.findViewById(R.id.recipe_fav);
            Glide.with(grid.getContext())
                    .load(recipeIntro.get(position).recipe_fav)
                    .into(recipeFav);

        } else {
            grid = convertView;
        }

        return grid;
    }

}