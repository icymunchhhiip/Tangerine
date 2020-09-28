package com.sixsense.tangerine.setting;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.Member;

public class MyMarketPostActivity extends AppCompatActivity implements MyMarketPostListener {
    private static final String TAG = "MyPostsActivity";
    private MyMarketListFragment myMarketListFragment;

    private Member member;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_mymarket_list);

        Intent intent = getIntent();
        member = (Member) intent.getSerializableExtra("member");

        myMarketListFragment = new MyMarketListFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, myMarketListFragment).commit();
    }

    @Override
    public void showMyMarketListFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyMarketListFragment()).commit();
    }

    @Override
    public Member getMember(){
        return member;
    }

}