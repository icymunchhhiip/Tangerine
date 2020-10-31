package com.sixsense.tangerine.setting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.content.CursorLoader;

import com.sixsense.tangerine.AppConstants;
import com.sixsense.tangerine.DownloadFilesTask;
import com.sixsense.tangerine.OnTaskCompletedListener;
import com.sixsense.tangerine.InsertDataTask;
import com.sixsense.tangerine.community.item.Member;
import com.sixsense.tangerine.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity implements OnTaskCompletedListener {
    //    private final int GET_GALLERY_IMAGE = 200;
    private static String TAG = "EditProfileActivity";

    private Member member;
    String nickname;
    Uri selectedImageUri;
    private String nowNickname;
    private String nowProfilePath;
    private Uri nowProfileUri;
    private TextView changeProfileTextView;
    private EditText nicknameEditText;
    private ImageView changeProfileImageView;
    private Button completeButton;
    private String imgPath = null;
    private String imgName = null;
    ConstraintLayout layout;

    private String img_update = "save";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_edit_profile);


        changeProfileTextView = findViewById(R.id.changeProfileTextView);
        changeProfileImageView = findViewById(R.id.changeProfileImageView);
        nicknameEditText = findViewById(R.id.nicknameEditText);
//        completeButton = findViewById(R.id.completeBT);

        Toolbar toolbar = findViewById(R.id.edit_profile_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        completeButton = findViewById(R.id.ok_profile_BT);

        //EditText lotId_input = (EditText) findViewById(R.id.nicknameEditText);  // EditText 객체 찾아서
        //lotId_input.setPrivateImeOptions("defaultInputmode=korean; ");  // 설정
        setNowState();


        changeProfileTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionResult = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (permissionResult == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, AppConstants.PERMISSION_OK);
                    }
                }

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, AppConstants.GET_GALLERY_IMAGE);

            }


        });

        completeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                nickname = nicknameEditText.getText().toString();
                member.setNickname(nickname);
                member.setProfilePath(imgPath);

                updateprofile(member);

                finish();
            }
        });
    }

    public void updateprofile(Member member) {
        String paramNames[] = {"m_name", "m_id", "img_update"};
        String values[] = {member.getNickname(), String.valueOf(member.getId()), img_update};
        InsertDataTask insertTask = new InsertDataTask(this, paramNames, values);
        insertTask.execute(AppConstants.UPDATE_MEMBER, imgPath, imgName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imgPath = getRealPathFromURI(imageUri); //이미지 실제(절대)경로 얻어오기
            Log.d(TAG, "이미지 경로: " + imgPath);
            try {
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath, bitmapOptions);//비트맵으로 변경
                bitmap = getResizedBitmap(bitmap, 400);//비트맵 리사이징
                imgPath = saveBitmapToJpeg(bitmap);//바뀐 이미지경로로 변경
                Log.d(TAG, "바뀐 이미지 경로:" + imgPath);
                changeProfileImageView.setImageBitmap(bitmap);//썸네일 띄우기
                img_update = "update";
            } catch (Exception e) {
                Log.d(TAG, "bitmap error" + e.getMessage());
            }
        }
    }

    public String saveBitmapToJpeg(Bitmap bitmap) {
        File storage = getApplicationContext().getCacheDir();//임시파일 저장경로
        File tempFile = new File(storage, imgName); //임시파일 생성

        try {
            tempFile.createNewFile(); //파일 생성
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);//사이즈 압축
            out.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "saveBitmapToJpeg error" + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "saveBitmapToJpeg error" + e.getMessage());
        }
        return tempFile.getAbsolutePath(); //경로 리턴

    }

    public Bitmap getResizedBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }


    public String getRealPathFromURI(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader c_loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = c_loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        imgName = path.substring(path.lastIndexOf("/") + 1);
        cursor.close();
        return path;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != AppConstants.PERMISSION_OK || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "ERROR extra memory read/write denied");
        }
    }

    public void setNowState() {
        Bundle bundle = getIntent().getExtras();
        member = (Member) bundle.getSerializable("member");

        nowNickname = bundle.getString("nowNickname");
        nicknameEditText.setText(nowNickname);
        nowProfilePath = bundle.getString("nowProfile");
        Log.e(TAG, "가져온 경로:" + nowProfilePath);
        //imgPath=nowProfilePath;
        //Log.e(TAG, "가져온 경로:" + imgPath);
        if (!TextUtils.isEmpty(nowProfilePath))
            new DownloadFilesTask(this, changeProfileImageView, AppConstants.ABSOLUTE_PATH).execute(nowProfilePath);

    }

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
}
