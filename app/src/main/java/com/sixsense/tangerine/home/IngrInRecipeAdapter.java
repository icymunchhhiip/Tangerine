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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.InRecipe;

import java.util.List;

public class IngrInRecipeAdapter extends RecyclerView.Adapter<IngrInRecipeAdapter.IngrViewHolder> {
    View view;
    List<InRecipe.IngrInfo> resource;

    public IngrInRecipeAdapter(List<InRecipe.IngrInfo> resource) {
        this.resource = resource;
    }

    @NonNull
    @Override
    public IngrInRecipeAdapter.IngrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_ingrlist, parent, false);

        return new IngrInRecipeAdapter.IngrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngrInRecipeAdapter.IngrViewHolder holder, final int position) {
        holder.ingr.setText(resource.get(position).ingrName);
        holder.cnt.setText(resource.get(position).ingrCnt);
        holder.kcal.setText(resource.get(position).ingrKcal);

    }

    public class IngrViewHolder extends RecyclerView.ViewHolder {
        TextView ingr;
        TextView cnt;
        TextView kcal;
        View view;

        public IngrViewHolder(View view) {
            super(view);
            this.view = view;
            ingr = view.findViewById(R.id.ingrTV);
            cnt = view.findViewById(R.id.numTV);
            kcal = view.findViewById(R.id.kcalTV);

        }
    }

    @Override
    public int getItemCount() {
        return resource.size();
    }
}
