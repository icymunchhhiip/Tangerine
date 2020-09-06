package com.sixsense.tangerine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

//import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.kakao.usermgmt.response.MeV2Response;
import com.sixsense.tangerine.home.HomeFragment;
import com.sixsense.tangerine.home.InRecipeFragment;
import com.sixsense.tangerine.home.SearchFragment;
import com.sixsense.tangerine.home.write.WriteRecipeActivity;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    public static MeV2Response MY_ACCOUNT;
    long backKeyPressedTime;
    ImageButton buttonWriting;

    private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.sixsense.tangerine", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getKeyHash();

        Toolbar toolbar = findViewById(R.id.toolbar_home);
        buttonWriting = toolbar.findViewById(R.id.recipe_write);
        buttonWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WriteRecipeActivity.class);
                startActivity(intent);
            }
        });
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        setSupportActionBar(toolbar);


//        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBackground));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            // edited here
//            activity.getWindow().setStatusBarColor(Color.WHITE);
//        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
//    }

//    @Override
//    public void onBackPressed() {
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//        if (count == 0) {
//            super.onBackPressed();
//        } else {
//            getSupportFragmentManager().popBackStack();
//        }
//
//    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 500) {
            backKeyPressedTime = System.currentTimeMillis();
//            for (Fragment fragment: getSupportFragmentManager().getFragments()) {
//                if (fragment.isVisible()) {
//                    //할일
//
//                }
//            }
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_frame);
            Fragment current = navHostFragment.getChildFragmentManager().getFragments().get(0);
            if (current instanceof SearchFragment) {
                ConstraintLayout layout = findViewById(R.id.main_layout);
                Toolbar toolbar = layout.findViewById(R.id.toolbar_home);
                toolbar.setNavigationIcon(null);
                if(buttonWriting.getVisibility()!=View.VISIBLE) {
                    buttonWriting.setVisibility(View.VISIBLE);
                }
                Log.d("yo","yes");
            } else if (current instanceof InRecipeFragment){
                ConstraintLayout layout = findViewById(R.id.main_layout);
                layout.findViewById(R.id.toolbar_show_title).setVisibility(View.GONE);
                Toolbar toolbar = layout.findViewById(R.id.toolbar_home);
                toolbar.setVisibility(View.VISIBLE);
            } else{
                Log.d("yo","no");
                Log.d("yo", String.valueOf(current.getClass()));
            }
            super.onBackPressed();
        } else {
            AppFinish();
        }
    }

    public void AppFinish() {
        finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
