package com.sixsense.tangerine.kakao_login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sixsense.tangerine.R;
import com.kakao.usermgmt.response.MeV2Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginSuccessFragment extends Fragment {
    TextView TextViewUserId;
    String userId;

    TextView TextViewUserNickname;
    String userNickname;

    ImageView ImageViewUserImg;
    String userImg;

    public LoginSuccessFragment(MeV2Response result){
        userId = Long.toString(result.getId());
        userNickname = result.getKakaoAccount().getProfile().getNickname();
        userImg = result.getKakaoAccount().getProfile().getThumbnailImageUrl();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_success, container, false);

        TextViewUserId = view.findViewById(R.id.user_id);
        TextViewUserId.setText(userId);

        TextViewUserNickname = view.findViewById(R.id.user_nickname);
        TextViewUserNickname.setText(userNickname);

        ImageViewUserImg = view.findViewById(R.id.user_img);
        new DownloadFilesTask().execute(userImg);
        return view;
    }

    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소
            ImageViewUserImg.setImageBitmap(result);
        }
    }
}
