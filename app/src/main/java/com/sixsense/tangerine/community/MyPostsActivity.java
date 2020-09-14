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
    private MyBoardListFragment myBoardListFragment;

    private Member member;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity_mypost_list);

        // TODO: 로그인 성공 시 회원정보 받아옴
        //Intent intent = getIntent();
        //this.member = (Member)intent.getSerializableExtra("member");

        this.member = new Member(1436323743,"가상로그인",null); //로그인 (가상 데이터)

        myMarketListFragment = new MyMarketListFragment();
        myBoardListFragment = new MyBoardListFragment();

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
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myBoardListFragment).commit();
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


//        marketTextView.setPaintFlags(boardTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        marketTextView.setTextColor(Color.parseColor("#EEBB4D"));
//        marketTextView.setTypeface(null, Typeface.BOLD);


//        marketTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                manager.beginTransaction().replace(R.id.container, mymarketFragment).commit();
//                marketTextView.setTypeface(null, Typeface.BOLD);
//                boardTextView.setTypeface(null,Typeface.NORMAL);
//                marketTextView.setTextColor(Color.parseColor("#EEBB4D"));
//                boardTextView.setTextColor(Color.BLACK);
//                marketTextView.setPaintFlags(marketTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                boardTextView.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
//            }
//
//        });
//
//        boardTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                manager.beginTransaction().replace(R.id.container, myWrittenBoardFragment).commit();
//                boardTextView.setTypeface(null, Typeface.BOLD);
//                marketTextView.setTypeface(null,Typeface.NORMAL);
//                boardTextView.setTextColor(Color.parseColor("#EEBB4D"));
//                marketTextView.setTextColor(Color.BLACK);
//                boardTextView.setPaintFlags(boardTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                marketTextView.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
//            }
//        });

//        marketButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                manager.beginTransaction().replace(R.id.container, mymarketFragment).commit();
//                marketButton.setPaintFlags(marketButton.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
//                boardButton.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
//
//            }
//        });
//
//        boardButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                manager.beginTransaction().replace(R.id.container, myWrittenBoardFragment).commit();
//                boardButton.setPaintFlags(marketButton.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
//                marketButton.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
//
//
//            }
//        });
    }

//    @Override
//    public void onReceivedBoardPost(BoardPost boardPost) {
//        this.boardPost = boardPost;
//    }
//
//    @Override
//    public void onReceivedMarketPost(MarketPost marketPost) {
//        this.marketPost = marketPost;
//    }

    @Override
    public void showMyMarketListFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyMarketListFragment()).commit();
    }

    @Override
    public void showMyBoardListFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyBoardListFragment()).commit();
    }

    @Override
    public Member getMember(){
        return member;
    }

}
