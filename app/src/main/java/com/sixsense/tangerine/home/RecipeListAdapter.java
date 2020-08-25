package com.sixsense.tangerine.home;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sixsense.tangerine.MainActivity;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.HttpInterface;
import com.sixsense.tangerine.network.RecipeIntroList;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
    private View view;
    private List<RecipeIntroList.RecipeIntro> recipeIntro;
    private String recipeImageURL = HttpClient.BASE_URL + "recipe/imgs/";
    private RecipeViewHolder recipeViewHolder;
    private String checkedState;


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
    public void onBindViewHolder(RecipeViewHolder holder, final int position) {
        recipeViewHolder = holder;
        Glide.with(view.getContext())
                .load(recipeImageURL + recipeIntro.get(position).recipe_img)
                .into(recipeViewHolder.recipeImg);

        recipeViewHolder.recipeName.setText(recipeIntro.get(position).recipe_name);

        recipeViewHolder.recipeMin.setText(recipeIntro.get(position).recipe_min);

        recipeViewHolder.recipeTags.setText(recipeIntro.get(position).recipe_tags);

        Glide.with(view.getContext())
                .load(recipeIntro.get(position).mem_profile)
                .into(recipeViewHolder.memProfile);

        recipeViewHolder.memName.setText(recipeIntro.get(position).mem_name);

        if(recipeIntro.get(position).recipe_fav==1){
            recipeViewHolder.recipeFav.setChecked(true);
        }else {
            recipeViewHolder.recipeFav.setChecked(false);
        }

        recipeViewHolder.recipeFav.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (recipeIntro.get(position).recipe_fav==1) {
                    checkedState = "save";
                    new LikeCall().execute(recipeIntro.get(position).recipe_id);
                    if (checkedState.equals("saved")||checkedState.equals("deleted_fail")||checkedState.equals("already_save")) {
                        recipeIntro.get(position).recipe_fav=1;
                        recipeViewHolder.recipeFav.setChecked(true);
                    }
                } else {
                    checkedState = "del";
                    new LikeCall().execute(recipeIntro.get(position).recipe_id);
                    if (checkedState.equals("deleted")||checkedState.equals("saved_fail")||checkedState.equals("already_del")) {
                        recipeIntro.get(position).recipe_fav=0;
                        recipeViewHolder.recipeFav.setChecked(false);
                    }
                }
            }
        });

        recipeViewHolder.recipeFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    recipeIntro.get(position).recipe_fav=1;
                }else{
                    recipeIntro.get(position).recipe_fav=0;
                }
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private class LikeCall extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            //progressbar
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            Call<String> call = httpInterface.setRecipeLike((int)MainActivity.MY_ACCOUNT.getId(), integers[0], checkedState);
            String response = null;
            try {
                response = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String string) {
            switch (string) {
                case "saved":
                case "deleted_fail":
                case "already_save":
                    break;
                case "deleted":
                case "saved_fail":
                case "already_del":
                    break;
                default:

            }

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