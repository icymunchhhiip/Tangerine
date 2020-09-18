package com.sixsense.tangerine.setting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.sixsense.tangerine.AppConstants;
import com.sixsense.tangerine.Constant;
import com.sixsense.tangerine.JsonParser;
import com.sixsense.tangerine.MainActivity;
import com.sixsense.tangerine.OnTaskCompletedListener;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.MyPostsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MyinfoFragment extends Fragment {
    private static String TAG = "sixsense (6)";
    private Member member;

    //xml의 text,img view 이름
    TextView user_id;
    TextView userName;
    ImageView userImg;
    //로그인할때 받아온 값
    String username;
    String userid;
    String userimg;

    private final int REQUEST_CODE_EDIT_NICKNAME = 101;
    private final int REQUEST_CODE_EDIT_PROFILE = 102;
    private String profileUri;
    private String nowNickname;
    private String nowProfilePath;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.setting_fragment_myinfo, container, false);

        Button editBT = rootView.findViewById(R.id.editBT);
        Button myRecipeBT = rootView.findViewById(R.id.myRecipeBT);
        Button mylikeRecipeBT = rootView.findViewById(R.id.mylikeRecipeBT);
        Button myMarketBT = rootView.findViewById(R.id.myMarketBT);


        userImg = rootView.findViewById(R.id.user_img);
        userName = rootView.findViewById(R.id.user_name);

        //프사, 이름 불러오기
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(MeV2Response result) {
                MeV2Response myAccount = result;
                userid = Long.toString(result.getId());
                Handler handler = new Handler();
                handler.execute(userid);

            }
        });


        editBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                nowNickname = member.getNickname();
                nowProfilePath = member.getProfilePath();
                Bundle bundle = new Bundle();
                bundle.putSerializable("member", member);
                bundle.putString("nowNickname", nowNickname);
                bundle.putString("nowProfile", nowProfilePath);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE);
            }
        });

        myRecipeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MywrittenRecipeActivity.class);
                intent.putExtra("EXTRA_USER_ID",MainActivity.sMyId);
                startActivity(intent);
            }
        });

        mylikeRecipeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MylikeRecipeActivity.class);
                intent.putExtra("EXTRA_USER_ID",MainActivity.sMyId);
                startActivity(intent);
            }
        });

        myMarketBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyPostsActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }

    private class Handler extends AsyncTask<String, Void, String> {
        String TAG_JSON = "webnautes";
        String TAG_PROFILE = "m_profile";
        String TAG_NAME = "m_name";
        String TAG_MID = "m_id";
        String TAG_LOCALCODE = "m_localcode";
        String TAG_LOCALSTR = "m_localstr";

        ProgressDialog pDialog;
        String errorString = null;

        URL url;

        @Override
        protected void onPreExecute() {

            super.onPreExecute(); // Showing progress dialog
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }


        //서버에 있는 php파일을 실행시키고 응답을 저장하고 스트링으로 변환하여 리턴.
        protected String doInBackground(String... arg0) {

            String USERID = arg0[0];
            Log.e(TAG, "USERID: " + USERID);

            JsonParser sh = new JsonParser();
            // Making a request to url and getting response
            String jsonStr = sh.convertJson(Constant.READ_MEMBER, USERID);

            Log.e(TAG, "Response from url: " + jsonStr);


            //showResult부분
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray jsonArray = jsonObj.getJSONArray(TAG_JSON);


                    //JsonArray에는 JSONObject가 데이터 갯수만큼 포함되어있음
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);

                        //JSONObject에서 의 값을 가져옴
                        String profile = item.getString(TAG_PROFILE);
                        String nickname = item.getString(TAG_NAME);
                        int mid = item.getInt(TAG_MID);
                        String localcode = item.getString(TAG_LOCALCODE);
                        String localstr = item.getString(TAG_LOCALSTR);

                        member = new Member(mid, nickname, profile);
                        Log.e(TAG, member.getNickname());
                    }

                } catch (final JSONException e) {

                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;

        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            userName.setText(member.getNickname());
            String profilePath = member.getProfilePath();
            if (!TextUtils.isEmpty(profilePath))
                new DownloadFilesTask(new OnTaskCompletedListener() {
                    @Override
                    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public boolean jsonToItem(String jsonString) {
                        return false;
                    }

                    @Override
                    public void noResultNotice(String searchString) {

                    }
                }, userImg, AppConstants.ABSOLUTE_PATH).execute(profilePath);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT_PROFILE) {
            new Handler().execute(userid);
        }
    }
}