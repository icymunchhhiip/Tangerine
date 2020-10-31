package com.sixsense.tangerine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.kakao.usermgmt.response.MeV2Response;
import com.sixsense.tangerine.community.CookingTipListFragment;
import com.sixsense.tangerine.community.MarketListFragment;
import com.sixsense.tangerine.community.MarketNoLocationFragment;
import com.sixsense.tangerine.community.MyCommunityListener;
import com.sixsense.tangerine.community.item.Member;

import java.security.MessageDigest;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MyCommunityListener, OnTaskCompletedListener {

    public static MeV2Response sMyAccount;
    public static int sMyId;
    public static Member member;

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
    }

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
    public void showNoLocationFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new MarketNoLocationFragment()).commit();
    }

    @Override
    public void showCookingTipListFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new CookingTipListFragment()).commit();
    }

    @Override
    public void setMemberLocation(String localString, int localCode) {
        member.setLocalAddress(localString,localCode);

        String[] paramNames = {"m_id","m_localstr","m_localcode"};
        String[] values = {String.valueOf(this.member.getId()),localString, String.valueOf(localCode)};
        InsertDataTask insertDataTask = new InsertDataTask(this,paramNames,values);
        insertDataTask.execute("community/save_local.php",null,null);
    }

    @Override
    public Member getMember(){
        return member;
    }

    @Override
    public boolean jsonToItem(String jsonString) {
        return false;
    }

    @Override
    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {
    }

    @Override
    public void noResultNotice(String searchString) {
    }
}
