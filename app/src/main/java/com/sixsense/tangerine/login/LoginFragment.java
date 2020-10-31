package com.sixsense.tangerine.login;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.sixsense.tangerine.MainActivity;
import com.sixsense.tangerine.OnTaskCompletedListener;
import com.sixsense.tangerine.R;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.sixsense.tangerine.InsertDataTask;
import com.sixsense.tangerine.community.item.Member;

public class LoginFragment extends Fragment implements OnTaskCompletedListener {
    private View mView;
    private static final String TAG = LoginFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.login, container, false);
        SessionCallback sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        return mView;
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e(TAG, exception.toString());
        }
    }

    private void requestMe() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e(TAG, errorResult.toString());
            }

            @Override
            public void onSuccess(MeV2Response result) {
                MainActivity.sMyAccount = result;
                MainActivity.sMyId = (int)result.getId();

                String userId = Long.toString(result.getId());
                String userName = result.getKakaoAccount().getProfile().getNickname();
                String userProfile = result.getKakaoAccount().getProfile().getThumbnailImageUrl();

                //새로운 회원: 회원DB에 추가한 후 MainActivity에 Member객체 전달
                //기존 회원: local 정보만 초기화
                addMemberDB(userId,userName,userProfile);
                MainActivity.member = new Member(Integer.parseInt(userId),userName,userProfile,null,0);

                NavDirections navDirections = LoginFragmentDirections.actionLoginFragmentToMainPagerFragment();
                Navigation.findNavController(mView).navigate(navDirections);
            }
        });
    }

    public void addMemberDB(String id, String nickname,String profile){
        String[] paramNames = {"m_id","m_name","m_profile"};
        String[] values = {id,nickname,profile};
        InsertDataTask insertDataTask = new InsertDataTask(this,paramNames,values);
        insertDataTask.execute("community/add_member.php",null,null);
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
