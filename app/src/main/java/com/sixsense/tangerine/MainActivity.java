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

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.kakao.usermgmt.response.MeV2Response;
import com.sixsense.tangerine.community.BoardListFragment;
import com.sixsense.tangerine.community.MarketListFragment;
import com.sixsense.tangerine.community.MyCommunityListener;
import com.sixsense.tangerine.community.item.Member;
import com.sixsense.tangerine.home.InRecipeFragment;
import com.sixsense.tangerine.home.SearchFragment;
import com.sixsense.tangerine.home.write.WriteRecipeActivity;

import java.security.MessageDigest;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MyCommunityListener {

    public static MeV2Response sMyAccount;
    public static Member member;

    private ImageButton mImageButtonWriting;
    private static final String TAG = "MainActivity";

    private void getKeyHash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.sixsense.tangerine", PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getKeyHash();

//        Toolbar toolbar = findViewById(R.id.toolbar_home);
//        mImageButtonWriting = toolbar.findViewById(R.id.recipe_write);
//        mImageButtonWriting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, WriteRecipeActivity.class);
//                startActivity(intent);
//            }
//        });
//        setSupportActionBar(toolbar);
    }

//    @Override
//    public void onBackPressed() {
//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_frame);
//        Fragment current = navHostFragment.getChildFragmentManager().getFragments().get(0);
//        if (current instanceof SearchFragment) {
//            ConstraintLayout layout = findViewById(R.id.main_layout);
//            Toolbar toolbar = layout.findViewById(R.id.toolbar_home);
//            toolbar.setNavigationIcon(null);
//            if (mImageButtonWriting.getVisibility() != View.VISIBLE) {
//                mImageButtonWriting.setVisibility(View.VISIBLE);
//            }
//        } else if (current instanceof InRecipeFragment) {
//            ConstraintLayout layout = findViewById(R.id.main_layout);
//            layout.findViewById(R.id.toolbar_show_title).setVisibility(View.GONE);
//            Toolbar toolbar = layout.findViewById(R.id.toolbar_home);
//            toolbar.setVisibility(View.VISIBLE);
//        }
//
//        super.onBackPressed();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMarketListFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new MarketListFragment()).commit();
    }

    @Override
    public void showBoardListFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new BoardListFragment()).commit();
    }

    @Override
    public void setMemberLocation(String localString, int localCode) { member.setLocalAddress(localString,localCode);}

    @Override
    public Member getMember(){
        return member;
    }

}
