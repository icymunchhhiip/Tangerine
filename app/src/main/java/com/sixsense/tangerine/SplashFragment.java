package com.sixsense.tangerine;

import android.content.Intent;
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

public class SplashFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.splash, container, false);
//        return view;
        return inflater.inflate(R.layout.splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!Session.getCurrentSession().checkAndImplicitOpen()) {
                    NavDirections action = SplashFragmentDirections.actionSplashFragmentToLoginFragment();
                    Navigation.findNavController(view).navigate(action);
                } else {
                    UserManagement.getInstance().me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            NavDirections action = SplashFragmentDirections.actionSplashFragmentToLoginFragment();
                            Navigation.findNavController(view).navigate(action);
                        }

                        @Override
                        public void onSuccess(MeV2Response result) {
                            MainActivity.MY_ACCOUNT = result;
                            NavDirections action = SplashFragmentDirections.actionSplashFragmentToMainPagerFragment();
                            Navigation.findNavController(view).navigate(action);
                        }
                    });
                }
            }
        },1000);
    }
}
