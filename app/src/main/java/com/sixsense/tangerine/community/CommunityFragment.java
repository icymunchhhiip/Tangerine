package com.sixsense.tangerine.community;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.sixsense.tangerine.MainActivity;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.Member;

public class CommunityFragment extends Fragment{
    private static final String TAG = "CommunityMainFragment";

    private MyCommunityListener myCommunityListener;

    private TabLayout tabLayout;

    private Member member; //회원정보 저장

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            myCommunityListener = (MyCommunityListener) getActivity();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (myCommunityListener != null) {
            this.member = myCommunityListener.getMember();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_fragment_community,container,false);

        if(member.getLocalCode() == 0){
            myCommunityListener.showNoLocationFragment();
        }else{
            myCommunityListener.showMarketListFragment();
        }

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        if(member.getLocalCode() == 0){
                            myCommunityListener.showNoLocationFragment();
                        }else{
                            myCommunityListener.showMarketListFragment();
                        }
                        break;
                    case 1:
                        //영윤세희팀
                        myCommunityListener.showCookingTipListFragment();
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

        return view;
    }
}