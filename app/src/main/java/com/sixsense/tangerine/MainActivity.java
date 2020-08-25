package com.sixsense.tangerine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

//import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.kakao.usermgmt.response.MeV2Response;
import com.sixsense.tangerine.home.HomeFragment;
import com.sixsense.tangerine.home.SearchFragment;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    public static MeV2Response MY_ACCOUNT;

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
}
