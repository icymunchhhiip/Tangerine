package com.sixsense.tangerine;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.sixsense.tangerine.community.item.Member;

public class SplashFragment extends Fragment {
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.splash, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!Session.getCurrentSession().checkAndImplicitOpen()) {
                    NavDirections navDirections = SplashFragmentDirections.actionSplashFragmentToLoginFragment();
                    Navigation.findNavController(getActivity(),R.id.main_frame).navigate(navDirections);
                } else {
                    UserManagement.getInstance().me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            NavDirections navDirections = SplashFragmentDirections.actionSplashFragmentToLoginFragment();
                            Navigation.findNavController(getActivity(),R.id.main_frame).navigate(navDirections);
                        }

                        @Override
                        public void onSuccess(MeV2Response result) {
                            MainActivity.sMyAccount = result;
                            MainActivity.member = new Member((int)MainActivity.sMyAccount.getId(),"가상로그인",null); //전달받은 회원데이터(가상 데이터)
                            NavDirections navDirections = SplashFragmentDirections.actionSplashFragmentToMainPagerFragment();
                            Navigation.findNavController(getActivity(),R.id.main_frame).navigate(navDirections);
                        }
                    });
                }
            }
        }, 1000);
    }
}
