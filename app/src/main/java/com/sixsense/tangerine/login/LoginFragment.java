package com.sixsense.tangerine.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.sixsense.tangerine.MainActivity;
import com.sixsense.tangerine.R;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.sixsense.tangerine.community.item.Member;

public class LoginFragment extends Fragment {
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
                MainActivity.member = new Member((int)MainActivity.sMyAccount.getId(),"가상로그인",null); //전달받은 회원데이터(가상 데이터)
                NavDirections navDirections = LoginFragmentDirections.actionLoginFragmentToMainPagerFragment();
                Navigation.findNavController(mView).navigate(navDirections);
            }
        });
    }
}
