package com.sixsense.tangerine.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.sixsense.tangerine.R;
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
    ListView ingrLV;
    TextView totalKcal;
    ListView recipeLV;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_in_recipe, container, false);

        if (getArguments() != null) {
            InRecipeFragmentArgs args = InRecipeFragmentArgs.fromBundle(getArguments());
            currentInfo = args.getRecipeIntroItem();

            title = view.findViewById(R.id.rtitle);
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

            uname = view.findViewById(R.id.uname);
            uname.setText(currentInfo.mem_name);

            level = view.findViewById(R.id.level);
            foodType = view.findViewById(R.id.foodType);
            rTime = view.findViewById(R.id.rtime);
            likeNum = view.findViewById(R.id.likes);
            rTool = view.findViewById(R.id.rtool);
            kcal = view.findViewById(R.id.rkcal);
            ingrLV = view.findViewById(R.id.ingrLV);
            totalKcal = view.findViewById(R.id.totalKcal);
            recipeLV = view.findViewById(R.id.recipeLV);

            new MoreInfoCall().execute(currentInfo.recipe_id);
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

            IngrInRecipeAdapter ingrInRecipeAdapter = new IngrInRecipeAdapter(view.getContext(), moreInfo.ingrList);
            ingrLV.setAdapter(ingrInRecipeAdapter);

            RecipeInRecipeAdapter recipeInRecipeAdapter = new RecipeInRecipeAdapter(view.getContext(), moreInfo.recipeList);
            recipeLV.setAdapter(recipeInRecipeAdapter);
//            recipeLV.setScrollContainer(false);
//            setListViewHeightBasedOnItems(recipeLV);
        }
    }
    public class NonScrollListView extends ListView {

        public NonScrollListView(Context context) {
            super(context);
        }
        public NonScrollListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        public NonScrollListView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }
        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
    }
}
