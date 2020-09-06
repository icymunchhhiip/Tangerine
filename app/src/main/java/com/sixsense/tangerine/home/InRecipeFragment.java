package com.sixsense.tangerine.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sixsense.tangerine.MainActivity;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.home.write.WriteRecipeActivity;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.HttpInterface;
import com.sixsense.tangerine.network.InRecipe;
import com.sixsense.tangerine.network.RecipeIntroList;

import java.io.IOException;

import retrofit2.Call;

public class InRecipeFragment extends Fragment {
    View view;
    private String recipeImageURL = HttpClient.BASE_URL + "recipe/imgs/";
    RecipeIntroList.RecipeIntro currentInfo;
    TextView title;
    ImageView mainImg;
    ImageView profileImg;
    ToggleButton like;
    TextView uname;
    TextView level;
    TextView foodType;
    TextView rTime;
    TextView likeNum;
    TextView rTool;
    TextView kcal;
    RecyclerView ingrLV;
    TextView totalKcal;
    RecyclerView recipeLV;
    private String checkedState;
    ConstraintLayout layout;
    Toolbar toolbar;
    InRecipe moreInfo;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_in_recipe, container, false);

        if (getArguments() != null) {
            InRecipeFragmentArgs args = InRecipeFragmentArgs.fromBundle(getArguments());
            currentInfo = args.getRecipeIntroItem();

            layout = getActivity().findViewById(R.id.main_layout);
            toolbar = layout.findViewById(R.id.toolbar_show_title);

            title = toolbar.findViewById(R.id.toolbar_recipe_title);
            title.setText(currentInfo.recipe_name);

            mainImg = view.findViewById(R.id.mainImg);
            Glide.with(view.getContext())
                    .load(recipeImageURL + currentInfo.recipe_img)
                    .into(mainImg);

            profileImg = view.findViewById(R.id.profileImg);
            Glide.with(view.getContext())
                    .load(currentInfo.mem_profile)
                    .into(profileImg);
            profileImg.setBackground(new ShapeDrawable(new OvalShape()));
            profileImg.setClipToOutline(true);

            like = view.findViewById(R.id.like);
            if (currentInfo.recipe_fav == 1) {
                like.setChecked(true);
            } else {
                like.setChecked(false);
            }

            like.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (currentInfo.recipe_fav==0) {
                        checkedState = "save";
                        new LikeCall().execute(currentInfo.recipe_id);
                        if (checkedState.equals("saved")||checkedState.equals("deleted_fail")||checkedState.equals("already_save")) {
                            currentInfo.recipe_fav=1;
                            like.setChecked(true);
                        }
                    } else {
                        checkedState = "del";
                        new LikeCall().execute(currentInfo.recipe_id);
                        if (checkedState.equals("deleted")||checkedState.equals("saved_fail")||checkedState.equals("already_del")) {
                            currentInfo.recipe_fav=0;
                            like.setChecked(false);
                        }
                    }
                }
            });

            uname = view.findViewById(R.id.uname);
            uname.setText(currentInfo.mem_name);

            level = view.findViewById(R.id.level);
            foodType = view.findViewById(R.id.foodType);
            rTime = view.findViewById(R.id.rtime);
            likeNum = view.findViewById(R.id.likes);
            rTool = view.findViewById(R.id.rtool);
            kcal = view.findViewById(R.id.rkcal);
            ingrLV = view.findViewById(R.id.ingrLV);
            ingrLV.setLayoutManager(new LinearLayoutManager(getContext()));
            totalKcal = view.findViewById(R.id.totalKcal);
            recipeLV = view.findViewById(R.id.recipeLV);
            recipeLV.setLayoutManager(new LinearLayoutManager(getContext()));
            try {
                moreInfo = new MoreInfoCall().execute(currentInfo.recipe_id).get();
            }catch (Exception e){ }
            Button edit = toolbar.findViewById(R.id.edit_recipe);
            Button del = toolbar.findViewById(R.id.del_recipe);
            if(edit.getVisibility()==View.VISIBLE) {
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), WriteRecipeActivity.class);
                        intent.putExtra("currentIntro", currentInfo);
                        intent.putExtra("currentDetail", moreInfo);
                        startActivity(intent);
                    }
                });
            }
//            if(del.getVisibility() == View.VISIBLE){
//                del.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), WriteRecipeActivity.class);
//                        intent.putExtra("currentIntro", currentInfo);
//                        intent.putExtra("currentDetail", moreInfo);
//                        startActivity(intent);
//                    }
//                });
//            }
        }

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class MoreInfoCall extends AsyncTask<Integer, Void, InRecipe> {

        @Override
        protected InRecipe doInBackground(Integer... integers) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            Call<InRecipe> call = httpInterface.getInRecipe(integers[0]);
            InRecipe response = null;
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
        protected void onPostExecute(InRecipe moreInfo) {
            level.setText(moreInfo.level);
            foodType.setText(moreInfo.foodType);
            rTime.setText(moreInfo.rTime);
            likeNum.setText(moreInfo.likeNum);
            rTool.setText(moreInfo.rTool);
            kcal.setText(moreInfo.totalKcal);
            totalKcal.setText(moreInfo.totalKcal);

            IngrInRecipeAdapter ingrInRecipeAdapter = new IngrInRecipeAdapter(moreInfo.ingrList);
            ingrLV.setAdapter(ingrInRecipeAdapter);

            RecipeInRecipeAdapter recipeInRecipeAdapter = new RecipeInRecipeAdapter(moreInfo.recipeList);
            recipeLV.setAdapter(recipeInRecipeAdapter);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LikeCall extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            Call<String> call = httpInterface.setRecipeLike((int) MainActivity.MY_ACCOUNT.getId(), integers[0], checkedState);
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

}
