package com.sixsense.tangerine.home;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.InRecipe;

import java.util.List;

public class RecipeInRecipeAdapter extends RecyclerView.Adapter<RecipeInRecipeAdapter.RecipeViewHolder> {
    private View mView;
    private List<InRecipe.RecipeContent> mRecipeContentList;
    private String mOriginTime;

    public RecipeInRecipeAdapter(List<InRecipe.RecipeContent> recipeContentList) {
        this.mRecipeContentList = recipeContentList;
    }

    @NonNull
    @Override
    public RecipeInRecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_recipelist, parent, false);

        return new RecipeInRecipeAdapter.RecipeViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, final int position) {

        if (mRecipeContentList.get(position).recipeImg != null && mRecipeContentList.get(position).recipeImg.length() > 0) {
            String baseUrl = HttpClient.BASE_URL + "recipe/imgs/";
            Glide.with(mView.getContext())
                    .load(baseUrl + mRecipeContentList.get(position).recipeImg)
                    .into(holder.mImage);
        } else {
            holder.mImage.setVisibility(View.GONE);
        }

        holder.mTextDesc.setText(mRecipeContentList.get(position).recipeDesc);
        holder.mTextDetail.setText(mRecipeContentList.get(position).descDetail);

        if (mRecipeContentList.get(position).recipeTime != null && mRecipeContentList.get(position).recipeTime.length() > 0) {
            mOriginTime = holder.mButtonTime.getText().toString();
            holder.mButtonTime.setText(mRecipeContentList.get(position).recipeTime);

            mOriginTime = mRecipeContentList.get(position).recipeTime;
            long hour = Long.parseLong(mOriginTime.substring(0, 2));
            hour *= 1000 * 60 * 60;
            long minute = Long.parseLong(mOriginTime.substring(3, 5));
            minute *= 1000 * 60;
            long second = Long.parseLong(mOriginTime.substring(6, 8));
            second *= 1000;
            long timeMilli = hour + minute + second;

            final CountDownTimer countDownTimer = new CountDownTimer(timeMilli, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000;
                    String countdown =
                            String.format("%02d", seconds / 3600) + ":" + String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60);
                    holder.mButtonTime.setText(countdown);
                }

                public void onFinish() {
                    holder.mButtonTime.setText(mOriginTime);
                }
            };

            holder.mButtonTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countDownTimer.start();
                }
            });
            holder.mButtonReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mButtonTime.setText(mOriginTime);
                    countDownTimer.cancel();
                }
            });
        } else {
            holder.mButtonTime.setText("00:00:00");
        }
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextDesc;
        private TextView mTextDetail;
        private Button mButtonTime;
        private ImageView mImage;
        private Button mButtonReset;

        public RecipeViewHolder(View view) {
            super(view);
            mImage = view.findViewById(R.id.rImage);
            mTextDesc = view.findViewById(R.id.desc);
            mTextDetail = view.findViewById(R.id.desc_detail);
            mButtonTime = view.findViewById(R.id.cooking_time);
            mButtonReset = view.findViewById(R.id.reset_time);
        }

    }

    @Override
    public int getItemCount() {
        return mRecipeContentList.size();
    }

}