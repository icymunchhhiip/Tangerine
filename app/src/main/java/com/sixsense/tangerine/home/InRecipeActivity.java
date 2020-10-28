package com.sixsense.tangerine.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
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

import java.util.Objects;

import retrofit2.Call;

public class InRecipeActivity extends AppCompatActivity {

    private RecipeIntroList.RecipeIntro mCurrentInfo;
    private ToggleButton mToggleButtonLike;
    private TextView mTextLevel;
    private TextView mTextFoodType;
    private TextView mTextTime;
    private TextView mTextLikeNum;
    private TextView mTextTool;
    private TextView mTextKcal;
    private RecyclerView mRecyclerIngr;
    private TextView mTextTotalKcal;
    private RecyclerView mRecyclerRecipe;
    private String mCheckedState;
    private InRecipe mMoreInfo;
    private static final String TAG = InRecipeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_in_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar_show_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            mCurrentInfo = (RecipeIntroList.RecipeIntro) bundle.getSerializable("EXTRA_CURRENT_INFO");

            TextView textTitle = findViewById(R.id.toolbar_recipe_title);
            ImageView imageMain = findViewById(R.id.mainImg);
            ImageView imageProfile = findViewById(R.id.profileImg);
            TextView textName = findViewById(R.id.uname);
            mToggleButtonLike = findViewById(R.id.like);
            mTextLevel = findViewById(R.id.level);
            mTextFoodType = findViewById(R.id.foodType);
            mTextTime = findViewById(R.id.rtime);
            mTextLikeNum = findViewById(R.id.likes);
            mTextTool = findViewById(R.id.rtool);
            mTextKcal = findViewById(R.id.rkcal);
            mRecyclerIngr = findViewById(R.id.ingrLV);
            mRecyclerIngr.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mTextTotalKcal = findViewById(R.id.totalKcal);
            mRecyclerRecipe = findViewById(R.id.recipeLV);
            mRecyclerRecipe.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            textTitle.setText(mCurrentInfo.recipeName);

            String baseUrl = HttpClient.BASE_URL + "recipe/imgs/";
            Glide.with(getApplicationContext())
                    .load(baseUrl + mCurrentInfo.recipeImg)
                    .into(imageMain);

            Glide.with(getApplicationContext())
                    .load(mCurrentInfo.memProfile)
                    .into(imageProfile);
            imageProfile.setBackground(new ShapeDrawable(new OvalShape()));
            imageProfile.setClipToOutline(true);

            if (mCurrentInfo.recipeFav == 1) {
                mToggleButtonLike.setChecked(true);
            } else {
                mToggleButtonLike.setChecked(false);
            }

            mToggleButtonLike.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mCurrentInfo.recipeFav == 0) {
                        mCheckedState = "save";
                        new LikeCall().execute(mCurrentInfo.recipeId);
                        if (mCheckedState.equals("saved") || mCheckedState.equals("deleted_fail") || mCheckedState.equals("already_save")) {
                            mCurrentInfo.recipeFav = 1;
                            mToggleButtonLike.setChecked(true);
                        }
                    } else {
                        mCheckedState = "del";
                        new LikeCall().execute(mCurrentInfo.recipeId);
                        if (mCheckedState.equals("deleted") || mCheckedState.equals("saved_fail") || mCheckedState.equals("already_del")) {
                            mCurrentInfo.recipeFav = 0;
                            mToggleButtonLike.setChecked(false);
                        }
                    }
                }
            });

            textName.setText(mCurrentInfo.memName);

            try {
                mMoreInfo = new MoreInfoCall().execute(mCurrentInfo.recipeId).get();
            } catch (Exception e) {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }

            Button buttonEdit = findViewById(R.id.edit_recipe);
            Button buttonDel = findViewById(R.id.del_recipe);
            if (mCurrentInfo.memId == MainActivity.sMyId) {
                buttonEdit.setVisibility(View.VISIBLE);
                buttonDel.setVisibility(View.VISIBLE);
            } else {
                buttonEdit.setVisibility(View.INVISIBLE);
                buttonDel.setVisibility(View.INVISIBLE);
            }

            if (buttonEdit.getVisibility() == View.VISIBLE) {
                buttonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(InRecipeActivity.this, WriteRecipeActivity.class);
                        intent.putExtra("EXTRA_CURRENT_INFO", mCurrentInfo);
                        intent.putExtra("EXTRA_CURRENT_DETAIL", mMoreInfo);
                        startActivity(intent);
                    }
                });
            }
            if (buttonDel.getVisibility() == View.VISIBLE) {
                buttonDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String state = "fail";
                        try {
                            state = new DeleteCall().execute().get();
                            if (state.equals("success")) {
                                onBackPressed();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                        }
                    }
                });
            }

        }
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
            } catch (Exception e) {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }
            return response;
        }

        @Override
        protected void onPostExecute(InRecipe moreInfo) {
            mTextLevel.setText(moreInfo.level);
            mTextFoodType.setText(moreInfo.foodType);
            mTextTime.setText(moreInfo.rTime);
            mTextLikeNum.setText(moreInfo.likeNum);
            mTextTool.setText(moreInfo.rTool);
            mTextKcal.setText(moreInfo.totalKcal);
            mTextTotalKcal.setText(moreInfo.totalKcal);

            IngrInRecipeAdapter ingrInRecipeAdapter = new IngrInRecipeAdapter(moreInfo.ingrList);
            mRecyclerIngr.setAdapter(ingrInRecipeAdapter);

            RecipeInRecipeAdapter recipeInRecipeAdapter = new RecipeInRecipeAdapter(moreInfo.recipeList);
            mRecyclerRecipe.setAdapter(recipeInRecipeAdapter);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LikeCall extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            Call<String> call = httpInterface.setRecipeLike(MainActivity.sMyId, integers[0], mCheckedState);
            String response = null;
            try {
                response = call.execute().body();
            } catch (Exception e) {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }
            return response;
        }

        @Override
        protected void onPostExecute(String string) {
            switch (string) {
                case "saved":
                case "deleted":
                    Toast.makeText(getApplicationContext(),R.string.need_refresh_message,Toast.LENGTH_SHORT).show();
                case "deleted_fail":
                case "already_save":
                case "saved_fail":
                case "already_del":
                    break;
                default:

            }

        }

    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteCall extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... Voids) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            Call<String> call = httpInterface.deleteRecipe(mCurrentInfo.recipeId);
            String response = null;
            try {
                response = call.execute().body();
            } catch (Exception e) {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }
            return response;
        }

    }

}
