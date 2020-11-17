package com.sixsense.tangerine.setting;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.home.RecipeListFragment;

public class MylikeRecipeActivity extends AppCompatActivity {
    public static int sMyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_mylike_recipe);

        Toolbar toolbar = findViewById(R.id.mylikerecipe_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sMyId = getIntent().getIntExtra("EXTRA_USER_ID",-1);
        if(sMyId!=-1){
            RecipeListFragment recipeListFragment = new RecipeListFragment("like", null);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.my_like_frame, recipeListFragment).commit();
        }
    }
}