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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.BoardPost;
import com.sixsense.tangerine.community.item.Member;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BoardWritingActivity extends AppCompatActivity implements OnTaskCompletedListener {
    private static final String TAG = "BoardWritingActivity";

    private Spinner bSpinnerCategory;
    private EditText bEditTextTitle;
    private EditText bEditTextDescription;

    private ImageView bImageViewSelected;
    private String imgPath = null;
    private String imgName = null;

    private TextView noticeCategory;
    private TextView noticeTitle;
    private TextView noticeDescription;

    private BoardPost item;
    private Member member;
    private int mode;
    private String imgUpdate= "save";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity_board_writing);

        Bundle bundle = getIntent().getExtras();
        mode = bundle.getInt("mode");
        member = (Member) bundle.get("member");

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog().build());

        bSpinnerCategory = findViewById(R.id.spinner_category);
        bEditTextTitle = (EditText) findViewById(R.id.editText_title);
        bEditTextDescription = (EditText) findViewById(R.id.editText_description);
        bImageViewSelected = (ImageView) findViewById(R.id.imageView_selected_img);

        noticeCategory = (TextView) findViewById(R.id.textView_category);
        noticeTitle = (TextView) findViewById(R.id.textView_title);
        noticeDescription = (TextView) findViewById(R.id.textView_description);
        noticeCategory.setVisibility(View.GONE);
        noticeTitle.setVisibility(View.GONE);
        noticeDescription.setVisibility(View.GONE);


        String[] categories = {"잡담", "꿀팁", "질문", "카테고리 선택"};
        ArrayAdapter<String> adapter = new HintAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories);
        bSpinnerCategory.setAdapter(adapter);
        bSpinnerCategory.setSelection(adapter.getCount(), true);
        View v = bSpinnerCategory.getSelectedView();
        ((TextView) v).setTextSize(18);

        if (mode == AppConstants.MODE_EDIT) {
            item = (BoardPost) bundle.getSerializable("item");
            int b_category = item.getCategory();
            switch (b_category) {
                case 01:
                    bSpinnerCategory.setSelection(0);
                    break;
                case 10:
                    bSpinnerCategory.setSelection(1);
                    break;
                case 11:
                    bSpinnerCategory.setSelection(2);
                    break;
                default:
                    Log.d(TAG, "category를 가져오는데 실패:" + b_category);
            }

            String paramNames[] = {"m_id","p_type","p_no"};
            String values[] = {String.valueOf(member.getId()), AppConstants.BOARD_BINARY,String.valueOf(item.getB_no())};
            GetDataTask task = new GetDataTask(this,paramNames,values, AppConstants.MODE_READ);
            task.execute("community/get_post.php",null,null);

            loadPost();
        }

        bSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(18);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //아무것도 선택되지 않았을 때
            }
        });

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

        Button submitButton = findViewById(R.id.button_submit); //글 제출
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
                String b_category = "0";
                switch (bSpinnerCategory.getSelectedItemPosition()) {
                    case 0:
                        b_category = AppConstants.CATEGORY_1;
                        break;
                    case 1:
                        b_category = AppConstants.CATEGORY_2;
                        break;
                    case 2:
                        b_category = AppConstants.CATEGORY_3;
                        break;
                }
                String b_title = bEditTextTitle.getText().toString();
                String b_description = bEditTextDescription.getText().toString();

                if (bSpinnerCategory.getSelectedItemPosition() == 3 || b_title.isEmpty() || b_description.isEmpty()) {
                    if (bSpinnerCategory.getSelectedItemPosition() == 3) {
                        noticeCategory.setVisibility(View.VISIBLE);
                        noticeCategory.setText("카테고리를 선택해주세요.");
                    } else {
                        noticeCategory.setVisibility(View.GONE);
                    }
                    if (b_title.isEmpty()) {
                        noticeTitle.setVisibility(View.VISIBLE);
                        noticeTitle.setText("제목을 입력해주세요.");
                    } else {
                        noticeTitle.setVisibility(View.GONE);
                    }
                    if (b_description.isEmpty()) {
                        noticeDescription.setVisibility(View.VISIBLE);
                        noticeDescription.setText("본문을 입력해주세요.");
                    }
                    else {
                        noticeDescription.setVisibility(View.GONE);
                    }
                } else {
                    if (mode == AppConstants.MODE_WRITE) {
                        addPost(member, b_category, b_title, b_description);
                    } else if (mode == AppConstants.MODE_EDIT) {
                        updatePost(b_category, b_title, b_description);
                    }

                    bEditTextTitle.getText().clear();
                    bEditTextDescription.getText().clear();
//                    imgPath = null;
//                    imgUpdate = false;

                    finish();
                }
            }
        });
    }

    public void addPost(Member member, String b_category, String b_title, String b_description) {
        String paramNames[] = {"m_id", "b_category", "b_title", "b_description"};
        String values[] = {String.valueOf(member.getId()), b_category, b_title, b_description};
        InsertDataTask insertTask = new InsertDataTask(this, paramNames, values);
        insertTask.execute("community/insert_board.php", imgPath, imgName);
    }

    public void updatePost(String b_category, String b_title, String b_description) {
        String paramNames[] = {"b_no", "b_category", "b_title", "b_description","img_update"};
        String values[] = {Integer.toString(item.getB_no()), b_category, b_title, b_description,this.imgUpdate};
        InsertDataTask insertTask = new InsertDataTask(this, paramNames, values);
        insertTask.execute("community/edit_board.php", imgPath, imgName);
    }

    public void loadPost() {
        bSpinnerCategory.setSelection(item.getCategory()-1);
        bEditTextTitle.setText(item.getTitle());
        bEditTextDescription.setText(item.getDescription());
        if(item.getImgPath()!= null){
            new DownloadFilesTask(this,bImageViewSelected, AppConstants.RELATEVE_PATH).execute(item.getImgPath());
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
                bImageViewSelected.setImageBitmap(bitmap);//썸네일 띄우기
                this.imgUpdate="update";
            } catch (Exception e) {
                Log.d(TAG, "bitmap error"+ e.getMessage());
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

            //board
            JSONObject jsonBoard = p_and_c.getJSONObject("boardpost");

            JSONObject writer = jsonBoard.getJSONObject("writer");
            JSONObject post = jsonBoard.getJSONObject("post");
            Boolean myLike = jsonBoard.getBoolean("my_like");

            int m_id = writer.getInt("m_id");
            String m_name = writer.getString("m_name");
            String m_profile = null;
            if (!writer.isNull("m_profile"))
                m_profile = writer.getString("m_profile");

            int b_no = post.getInt("b_no");
            int b_category = post.getInt("b_category");
            String b_title = post.getString("b_title");
            String b_description = post.getString("b_description");
            int b_likes = post.getInt("b_likes");
            String b_date = post.getString("b_date");
            String b_imgpath = null;
            if(!post.isNull("b_imgpath"))
                b_imgpath = post.getString("b_imgpath");

            BoardPost readPost = new BoardPost(new Member(m_id,m_name,m_profile), b_no, b_category, b_title, b_description, b_date, b_likes, b_imgpath, myLike);
            item = readPost;
        } catch (JSONException e) {
            Log.d(TAG, "showResult: ", e);
        }
    }

    @Override
    public void noResultNotice(String searchString) {

    }
}