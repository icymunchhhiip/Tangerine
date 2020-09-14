package com.sixsense.tangerine.community;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.MarketPost;
import com.sixsense.tangerine.community.item.Member;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MarketWritingActivity extends AppCompatActivity implements OnTaskCompletedListener {
    private static String TAG = "MarketWritingActivity";

    private EditText mkEditTextTitle;
    private EditText mkEditTextPrice;
    private EditText mkEditTextDescription;

    private ImageView mkImageViewSelected;
    private String imgPath = null;
    private String imgName = null;

    private TextView noticeTitle;
    private TextView noticePrice;
    private TextView noticeDescription;

    private MarketPost item;
    private Member member;
    private int mode;
    private String imgUpdate = "save";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity_market_writing);

        Bundle bundle = getIntent().getExtras();
        mode = bundle.getInt("mode");
        member = (Member) bundle.get("member");

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog().build());

        mkEditTextTitle = (EditText) findViewById(R.id.editText_title);
        mkEditTextPrice = (EditText) findViewById(R.id.editText_price);
        mkEditTextDescription = (EditText) findViewById(R.id.editText_description);
        mkImageViewSelected = (ImageView) findViewById(R.id.imageView_selected_img);

        noticeTitle = (TextView) findViewById(R.id.textView_title);
        noticePrice = (TextView) findViewById(R.id.textView_price);
        noticeDescription = (TextView) findViewById(R.id.textView_description);
        noticeTitle.setVisibility(View.GONE);
        noticePrice.setVisibility(View.GONE);
        noticeDescription.setVisibility(View.GONE);

        if (mode == AppConstants.MODE_EDIT) {
            item = (MarketPost) bundle.getSerializable("item");

            String[] paramNames = {"m_id", "p_type","p_no"};
            String[] values = {String.valueOf(member.getId()),AppConstants.MARKET_BINARY, Integer.toString(item.getMk_no())};
            GetDataTask task = new GetDataTask(this, paramNames, values, AppConstants.MODE_READ);
            task.execute("community/get_post.php",null,null);

            loadPost();
        }

        Button photoButton = findViewById(R.id.button_insert_img);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사진추가버튼 클릭 시 갤러리
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, AppConstants.GET_GALLERY_IMAGE);
            }
        });

        ImageButton closeButton = findViewById(R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(AppConstants.WRITE_OK);
                onBackPressed();
            }
        });

        Button submitButton = findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //외부저장소 권한
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionResult = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (permissionResult == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, AppConstants.PERMISSION_OK);
                    }
                }

                Bundle bundle = getIntent().getExtras();
                Member member = (Member) bundle.getSerializable("member");

                String mk_localcode = Integer.toString(member.getLocalCode());
                String mk_title = mkEditTextTitle.getText().toString();
                String mk_price = mkEditTextPrice.getText().toString();
                String mk_description = mkEditTextDescription.getText().toString();

                if (mk_title.isEmpty() || mk_price.isEmpty() || mk_description.isEmpty()) {
                    if (mk_title.isEmpty()) {
                        noticeTitle.setVisibility(View.VISIBLE);
                        noticeTitle.setText("제목을 입력해주세요.");
                    } else {
                        noticeTitle.setVisibility(View.GONE);
                    }
                    if (mk_price.isEmpty()) {
                        noticePrice.setVisibility(View.VISIBLE);
                        noticePrice.setText("가격을 입력해주세요.");
                    } else {
                        noticePrice.setVisibility(View.GONE);
                    }
                    if (mk_description.isEmpty()) {
                        noticeDescription.setVisibility(View.VISIBLE);
                        noticeDescription.setText("본문을 입력해주세요.");
                    }
                    else {
                        noticeDescription.setVisibility(View.GONE);
                    }
                } else {
                    if (mode == AppConstants.MODE_WRITE) {
                        addPost(member, mk_localcode, mk_title, mk_price, mk_description);
                    } else if (mode == AppConstants.MODE_EDIT) {
                        updatePost(mk_title, mk_price, mk_description);
                    }

                    mkEditTextTitle.getText().clear();
                    mkEditTextPrice.getText().clear();
                    mkEditTextDescription.getText().clear();
                    imgPath = null;

                    finish();
                }
            }
        });
    }

    public void addPost(Member member, String mk_localcode, String mk_title, String mk_price, String mk_description) {
        String paramNames[] = {"m_id", "mk_localcode", "mk_title", "mk_price", "mk_description"};
        String values[] = {String.valueOf(member.getId()), mk_localcode, mk_title, mk_price, mk_description};
        InsertDataTask insertTask = new InsertDataTask(this, paramNames, values);
        insertTask.execute("community/insert_market.php", imgPath, imgName);
    }

    public void updatePost(String mk_title, String mk_price, String mk_description) {
        String paramNames[] = {"mk_no", "mk_title", "mk_price", "mk_description","img_update"};
        String values[] = {Integer.toString(item.getMk_no()), mk_title, mk_price, mk_description, this.imgUpdate};
        InsertDataTask insertTask = new InsertDataTask(this, paramNames, values);
        insertTask.execute("community/edit_market.php", imgPath, imgName);
        Log.e(TAG, String.valueOf(this.imgUpdate));
    }

    public void loadPost() {
        mkEditTextTitle.setText(item.getTitle());
        mkEditTextPrice.setText(Integer.toString(item.getPrice()));
        mkEditTextDescription.setText(item.getDescription());
        if(item.getImgPath()!= null){
            new DownloadFilesTask(this,mkImageViewSelected,AppConstants.RELATEVE_PATH).execute(item.getImgPath());
        }
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
                mkImageViewSelected.setImageBitmap(bitmap);//썸네일 띄우기

                this.imgUpdate = "update";
            } catch (Exception e) {
                Log.e(TAG, "bitmap error" + e.getMessage());
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

    @Override
    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void jsonToItem(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject p_and_c = jsonObject.getJSONObject("post&comment");

            //market
            JSONObject jsonMarket = p_and_c.getJSONObject("marketpost");

            JSONObject writer = jsonMarket.getJSONObject("writer");
            JSONObject post = jsonMarket.getJSONObject("post");

            int m_id = writer.getInt("m_id");
            String m_name = writer.getString("m_name");
            String m_profile = null;
            if (!writer.isNull("m_profile"))
                m_profile = writer.getString("m_profile");

            int mk_no = post.getInt("mk_no");
            int mk_localcode = post.getInt("mk_localcode");
            int mk_price = post.getInt("mk_price");
            String mk_title = post.getString("mk_title");
            String mk_description = post.getString("mk_description");
            String mk_date = post.getString("mk_date");
            String mk_imgpath = null;
            if (!post.isNull("mk_imgpath"))
                mk_imgpath = post.getString("mk_imgpath");

            MarketPost readPost = new MarketPost(new Member(m_id, m_name, m_profile), mk_no, mk_localcode, mk_price, mk_title, mk_description, mk_date, mk_imgpath);
            item = readPost;

        } catch (JSONException e) {
            Log.d(TAG, "showResult: ", e);
        }
    }

    @Override
    public void noResultNotice(String searchString) {

    }
}