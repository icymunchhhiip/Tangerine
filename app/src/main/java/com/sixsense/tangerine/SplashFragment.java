package com.sixsense.tangerine;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.sixsense.tangerine.community.AppConstants;
import com.sixsense.tangerine.community.GetDataTask;
import com.sixsense.tangerine.community.item.Member;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashFragment extends Fragment implements OnTaskCompletedListener {
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
                            MainActivity.sMyId = (int)result.getId();
                            getMemberDB();

                            NavDirections navDirections = SplashFragmentDirections.actionSplashFragmentToMainPagerFragment();
                            Navigation.findNavController(getActivity(),R.id.main_frame).navigate(navDirections);
                        }
                    });
                }
            }
        }, 1000);
    }

    @Override
    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {

    }

    public void getMemberDB(){
        String[] paramNames = {"m_id"};
        String[] values = {String.valueOf(MainActivity.sMyAccount.getId())};
        GetDataTask getDataTask = new GetDataTask(this,paramNames,values, AppConstants.MODE_READ);
        AsyncTask.Status result = getDataTask.execute("community/get_member.php").getStatus();
        while(result == AsyncTask.Status.FINISHED) {
            Log.e("Login","GetDataTask Completed");
            return;
        }
    }

    @Override
    public boolean jsonToItem(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject memberObject = jsonObject.getJSONObject("member");

            String name = memberObject.getString("m_name");
            String profile = memberObject.getString("m_profile");

            String localString = memberObject.getString("m_localstr");
            int localCode = memberObject.getInt("m_localcode");

            MainActivity.member = new Member((int)MainActivity.sMyAccount.getId(),name,profile,localString,localCode);
            return true;

        } catch (JSONException e) {
            Log.d("Login", "showResult: ", e);
        }
        return false;
    }
    @Override
    public void noResultNotice(String searchString) {

    }
}
