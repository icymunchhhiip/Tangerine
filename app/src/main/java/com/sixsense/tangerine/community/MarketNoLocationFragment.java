package com.sixsense.tangerine.community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sixsense.tangerine.AppConstants;
import com.sixsense.tangerine.OnTaskCompletedListener;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.Member;

/*장터게시판 : 위치설정 안했을 때*/
public class MarketNoLocationFragment extends Fragment implements OnTaskCompletedListener {
    private MyCommunityListener myCommunityListener;
    private Member member;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof MyCommunityListener) {
            myCommunityListener = (MyCommunityListener) getActivity();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(myCommunityListener != null){
            this.member = myCommunityListener.getMember();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.community_fragment_market_nolocation, container, false);

        EditText editTextSearch = rootView.findViewById(R.id.editText_search);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(getContext(), "지역을 설정해주세요.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        ImageButton buttonWriting = rootView.findViewById(R.id.button_writing);
        buttonWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "지역을 설정해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView = rootView.findViewById(R.id.textView_setting_region);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetLocationActivity.class);
                startActivityForResult(intent, AppConstants.REQUEST_CODE_SETLOCATION); //MarketSetLocation 액티비티(위치설정화면) 호출
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstants.REQUEST_CODE_SETLOCATION) {
            if (resultCode == AppConstants.RESULT_OK) {
                String localString = data.getStringExtra("localString");
                int localCode = Integer.parseInt(data.getStringExtra("localCode")); //MarketSetLocation 액티비티에서 받아옴

                if (myCommunityListener != null) {
                    myCommunityListener.setMemberLocation(localString, localCode); //Community 액티비티의 member객체에 위치 set

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().remove(MarketNoLocationFragment.this).commit(); //현재 프래그먼트 스택 제거
                    myCommunityListener.showMarketListFragment();//위치설정o 프래그먼트로 이동
                }
            }
        }
    }

    @Override
    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {

    }

    @Override
    public boolean jsonToItem(String jsonString) {
        return false;
    }

    @Override
    public void noResultNotice(String searchString) {

    }
}
