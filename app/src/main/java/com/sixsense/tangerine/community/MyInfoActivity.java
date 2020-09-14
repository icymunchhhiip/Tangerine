package com.sixsense.tangerine.community;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.Member;

public class MyInfoActivity extends AppCompatActivity {
    //영윤세희팀꺼

    //xml의 text,img view 이름
    TextView user_id;
    TextView user_name;
    ImageView user_img;
    //로그인할때 받아온 값
    String username;
    String userid;
    String userimg;

    private final int REQUEST_CODE_EDIT_NICKNAME = 101;
    private final int REQUEST_CODE_EDIT_PROFILE = 102;
    private Uri profileUri;
    private String nowNickname;
    private Uri nowProfileUri;

    private Member member; //회원정보 저장

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity_myinfo);

        this.member = new Member(1436323743,"지원사랑",null); //로그인 (가상 데이터)

        Button editBT = findViewById(R.id.editBT);
        Button myRecipeBT = findViewById(R.id.myRecipeBT);
        Button mylikeRecipeBT = findViewById(R.id.mylikeRecipeBT);
        Button myPostBT = findViewById(R.id.myPostBT);
        Button mylikePostBT = findViewById(R.id.mylikePostBT);

//        //프사, 이름 불러오기
//        UserManagement.getInstance().me(new MeV2ResponseCallback() {
//            @Override
//            public void onSessionClosed(ErrorResult errorResult) {}
//            @Override
//            public void onSuccess(MeV2Response result) {
//                MeV2Response myAccount = result;
////                int id = (int)myAccount.getId();
//                username = result.getKakaoAccount().getProfile().getNickname();
//                //username=Long.toString(result.getId());
//                userimg = result.getKakaoAccount().getProfile().getThumbnailImageUrl();
//
                user_name = findViewById(R.id.user_name);
                user_name.setText(member.getNickname());
//                user_name.setText(username);

                user_img = findViewById(R.id.user_img);
                //new DownloadFilesTask().execute(userimg);
//            }
//        });


//        editBT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
//                nowNickname = user_name.getText().toString();
//                intent.putExtra("nowNickname", nowNickname);
//                intent.putExtra("nowProfile", nowProfileUri);
//                startActivityForResult(intent, REQUEST_CODE_EDIT_NICKNAME);
//            }
//        });
//
//        myRecipeBT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MywrittenRecipeActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        mylikeRecipeBT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MylikeRecipeActivity.class);
//                startActivity(intent);
//            }
//        });

        myPostBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPostsActivity.class);
                intent.putExtra("member", member);
                startActivity(intent);
            }
        });

//        mylikePostBT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MyLikePostActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    /*프로필 이미지를 가져오기 위한 메서드*/
//    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            Bitmap bmp = null;
//            try {
//                String img_url = strings[0]; //url of the image
//                URL url = new URL(img_url);
//                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return bmp;
//        }
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//        @Override
//        protected void onPostExecute(Bitmap result) {
////             doInBackground 에서 받아온 total 값 사용 장소
//            user_img.setImageBitmap(result);
//        }
//    }


    /*아마도 edit를 위한 메서드*/
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_EDIT_NICKNAME) {
//            if (data != null) {
//                String nickname = data.getStringExtra("nickname");
//                user_name.setText(nickname);
//                profileUri = data.getData();
//                user_img.setImageURI(profileUri);
//
//            }
////
////            if (data != null) {
////                profileUri = getIntent().getParcelableExtra("profileUri");
////                profileImageView.setImageURI(profileUri);
////            }
//        }
//        // if (requestCode == REQUEST_CODE_EDIT_PROFILE) {
//
//        // }
//    }
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putString("nowNickname",nowNickname);
//        outState.putParcelable("selectedImageUri",profileUri);
//    }
}


