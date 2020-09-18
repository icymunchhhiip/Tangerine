package com.sixsense.tangerine.community;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.Member;

public class MyPostsActivity extends AppCompatActivity implements MyPostsListener {
    private static final String TAG = "MyPostsActivity";
    private MyMarketListFragment myMarketListFragment;

    private Member member;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity_mypost_list);

        this.member = new Member(1436323743, "가상로그인", null); //로그인 (가상 데이터)

        myMarketListFragment = new MyMarketListFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, myMarketListFragment).commit();

        tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //getMemberDB(member.getId()); //member 업데이트

                switch (tab.getPosition()) {
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myMarketListFragment).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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
