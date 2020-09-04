package com.sixsense.tangerine.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.InRecipe;

import java.util.List;

public class RecipeInRecipeAdapter extends ArrayAdapter {
    Context context;
    List<InRecipe.RecipeContent> resource;
    private String recipeImageURL = HttpClient.BASE_URL + "recipe/imgs/";

    public RecipeInRecipeAdapter(@NonNull Context context, List<InRecipe.RecipeContent> resource) {
        super(context, R.layout.home_recipelist, resource);
        this.context=context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return resource.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        if(convertView==null)
            row = LayoutInflater.from(getContext()).inflate(R.layout.home_recipelist, parent, false);

        ImageView image = row.findViewById(R.id.rImage);

        if(resource.get(position).recipeImg!=null) {
            Glide.with(convertView)
                    .load(recipeImageURL + resource.get(position).recipeImg)
                    .into(image);
        }
        TextView desc = row.findViewById(R.id.desc);
        TextView detail = row.findViewById(R.id.desc_detail);
        TextView time = row.findViewById(R.id.cooking_time);

        desc.setText(resource.get(position).recipeDesc);
        detail.setText(resource.get(position).descDetail);
        time.setText(resource.get(position).recipeTime);
        return row;
    }
}