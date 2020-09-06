package com.sixsense.tangerine.home;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.main.MainPagerFragmentDirections;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.InRecipe;
import com.sixsense.tangerine.network.RecipeIntroList;

import java.sql.Time;
import java.util.List;
import java.util.TimerTask;

public class RecipeInRecipeAdapter extends RecyclerView.Adapter<RecipeInRecipeAdapter.RecipeViewHolder> {
    private View view;
    List<InRecipe.RecipeContent> resource;
    private String recipeImageURL = HttpClient.BASE_URL + "recipe/imgs/";
    String origin_time;
    CountDownTimer countDownTimer;

    public RecipeInRecipeAdapter(List<InRecipe.RecipeContent> resource) {
        this.resource = resource;
    }

    @NonNull
    @Override
    public RecipeInRecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_recipelist, parent, false);

        return new RecipeInRecipeAdapter.RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeInRecipeAdapter.RecipeViewHolder holder, final int position) {

        if (resource.get(position).recipeImg != null) {
            Glide.with(view.getContext())
                    .load(recipeImageURL + resource.get(position).recipeImg)
                    .into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }

        holder.desc.setText(resource.get(position).recipeDesc);
        holder.detail.setText(resource.get(position).descDetail);

        if (resource.get(position).recipeTime != null) {
            origin_time = holder.time.getText().toString();
            holder.time.setText(resource.get(position).recipeTime);

            origin_time = resource.get(position).recipeTime;
            long hour = Long.parseLong(origin_time.substring(0, 2));
            hour *= 1000 * 60 * 60;
            long minute = Long.parseLong(origin_time.substring(3, 5));
            minute *= 1000 * 60;
            long second = Long.parseLong(origin_time.substring(6, 8));
            second *= 1000;
            long timeMilli = hour + minute + second;

            countDownTimer = new CountDownTimer(timeMilli, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000;
                    String countdown = String.format("%02d", seconds / 3600) + ":" + String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60);
                    holder.time.setText(countdown);
                }

                public void onFinish() {
                    holder.time.setText(origin_time);
                }
            };

            holder.time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countDownTimer.start();
                }
            });
            holder.reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.time.setText(origin_time);
                    countDownTimer.cancel();
                }
            });
        } else {
            holder.time.setText("00:00:00");
        }
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView desc;
        TextView detail;
        Button time;
        ImageView image;
        Button reset;
        View view;

        public RecipeViewHolder(View view) {
            super(view);
            this.view = view;
            image = view.findViewById(R.id.rImage);
            desc = view.findViewById(R.id.desc);
            detail = view.findViewById(R.id.desc_detail);
            time = view.findViewById(R.id.cooking_time);
            reset = view.findViewById(R.id.reset_time);

        }

    }

    @Override
    public int getItemCount() {
        return resource.size();
    }

}