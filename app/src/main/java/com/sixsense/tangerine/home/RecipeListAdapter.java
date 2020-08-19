package com.sixsense.tangerine.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.RecipeIntroList;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
    private View view;
    private List<RecipeIntroList.RecipeIntro> recipeIntro;
    private String recipeImageURL = HttpClient.BASE_URL + "recipe/imgs/";

    public RecipeListAdapter(List<RecipeIntroList.RecipeIntro> recipeIntro) {
        this.recipeIntro = recipeIntro;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        Glide.with(view.getContext())
                .load(recipeImageURL + recipeIntro.get(position).recipe_img)
                .into(holder.recipeImg);

        holder.recipeName.setText(recipeIntro.get(position).recipe_name);

        holder.recipeMin.setText(recipeIntro.get(position).recipe_min);

        holder.recipeTags.setText(recipeIntro.get(position).recipe_tags);

        Glide.with(view.getContext())
                .load(recipeIntro.get(position).mem_profile)
                .into(holder.memProfile);

        holder.memName.setText(recipeIntro.get(position).mem_name);

        if(recipeIntro.get(position).recipe_fav==1){
            holder.recipeFav.setChecked(true);
        }else {
            holder.recipeFav.setChecked(false);
        }

    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImg;
        TextView recipeName;
        TextView recipeMin;
        TextView recipeTags;
        ImageView memProfile;
        TextView memName;
        CheckBox recipeFav;

        public RecipeViewHolder(View view) {
            super(view);
            recipeImg = view.findViewById(R.id.recipe_img);
            recipeName = view.findViewById(R.id.recipe_name);
            recipeMin = view.findViewById(R.id.recipe_min);
            recipeTags = view.findViewById(R.id.recipe_tags);
            memProfile = view.findViewById(R.id.mem_profile);
            memName = view.findViewById(R.id.mem_name);
            recipeFav = view.findViewById(R.id.recipe_fav);


        }
    }

    @Override
    public int getItemCount() {
        return recipeIntro.size();
    }

}