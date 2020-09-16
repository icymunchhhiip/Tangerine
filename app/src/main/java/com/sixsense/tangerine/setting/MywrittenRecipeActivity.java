package com.sixsense.tangerine.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kakao.usermgmt.response.MeV2Response;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.home.MyRecipeListFragment;
import com.sixsense.tangerine.home.RecentRecipeListFragment;

import java.util.ArrayList;

public class MywrittenRecipeActivity extends AppCompatActivity {
    public static int sMyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_mywritten_recipe);

        sMyId = getIntent().getIntExtra("EXTRA_USER_ID",-1);
        if(sMyId!=-1){

            MyRecipeListFragment myRecipeListFragment = new MyRecipeListFragment("written");

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.my_written_frame, myRecipeListFragment).commit();
        }
    }
}