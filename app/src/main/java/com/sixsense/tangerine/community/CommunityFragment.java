package com.sixsense.tangerine.community;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.sixsense.tangerine.MainActivity;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.Member;

import org.json.JSONException;
import org.json.JSONObject;

public class CommunityFragment extends Fragment implements OnTaskCompletedListener {
    private static final String TAG = "CommunityMainFragment";

    private MarketNoLocationFragment marketNoLocationFragment; //위치설정x 인경우
    private MarketListFragment marketListFragment; //위치설정o 인경우 장터 글목록
    private CookingTipListFragment cookingTipFragment; //꿀팁
    private BoardListFragment boardListFragment; //게시판 글목록

    private TabLayout tabLayout;

    private Member member; //회원정보 저장
//    BoardPost boardPost;
//    MarketPost marketPost;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_activity_community,container,false);

        marketNoLocationFragment = new MarketNoLocationFragment();
        marketListFragment = new MarketListFragment();
        cookingTipFragment = new CookingTipListFragment();
        boardListFragment = new BoardListFragment();

        // TODO: 로그인 성공 -> DB에 저장된 Member 정보 -> 전달받음
        //Intent intent = getIntent();
        //this.member = (Member)intent.getSerializableExtra("member");
        member = MainActivity.member; //전달받은 회원데이터(가상 데이터)

        Toast.makeText(view.getContext(), "member id=" + member.getId() + " name="+member.getNickname(), Toast.LENGTH_SHORT).show(); //테스트

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new MarketNoLocationFragment()).commit(); //위치설정안됐을때

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getMemberDB(member.getId()); //member 업데이트

                switch (tab.getPosition()) {
                    case 0:
                        if(member.getLocalCode() == 0){
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, marketNoLocationFragment).commit();
                        }else{
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, marketListFragment).commit();
                        }
                        break;
                    case 1:
                        //영윤세희팀
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, cookingTipFragment).commit();
                        break;
                    case 2:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, boardListFragment).commit();
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

//    @Override
//    public void onReceivedBoardPost(BoardPost boardPost) {
//        this.boardPost = boardPost;
//    }
//
//    @Override
//    public void onReceivedMarketPost(MarketPost marketPost) {
//        this.marketPost = marketPost;
//    }

    public void getMemberDB(int m_id){
        String[] paramNames = {"m_id"};
        String[] values = {String.valueOf(m_id)};
        GetDataTask getDataTask = new GetDataTask(this,paramNames,values, AppConstants.MODE_READ);
        getDataTask.execute("community/get_member.php");
    }

    @Override
    public void jsonToItem(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject memberObject = jsonObject.getJSONObject("member");

            this.member.setNickname(memberObject.getString("m_name"));
            this.member.setProfilePath(memberObject.getString("m_profile"));

            String localString = memberObject.getString("m_localstr");
            int localCode = memberObject.getInt("m_localcode");

            this.member.setLocalAddress(localString,localCode);

        } catch (JSONException e) {
            Log.d(TAG, "showResult: ", e);
        }
    }

    @Override
    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {

    }

    @Override
    public void noResultNotice(String searchString) {

    }
}