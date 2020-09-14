package com.sixsense.tangerine.community;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.Comment;
import com.sixsense.tangerine.community.item.MarketPost;
import com.sixsense.tangerine.community.item.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class MarketReadingActivity extends BaseActivity implements OnTaskCompletedListener {
    private static final String TAG = "MarketReadingActivity";

    private TextView textViewDate;
    private TextView textViewTitle;
    private TextView textViewDescript;
    private TextView textViewPrice;
    private TextView textViewNickname;
    private ImageView imageViewUserImg;
    private ImageView imageViewImg;

    private EditText editTextComment;
    private Button buttonCommentSubmit;

    private Member member;
    private MarketPost item;

    private CommentAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity_market_reading);

        Bundle bundle = getIntent().getExtras();
        item = (MarketPost) bundle.getSerializable("marketItem");
        member = (Member) bundle.getSerializable("member");

        Context context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerView_mComment);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CommentAdapter();
        recyclerView.setAdapter(adapter);

        updatePost(item.getMk_no());

        textViewDate = findViewById(R.id.textView_date);
        textViewTitle = findViewById(R.id.textView_title);
        textViewDescript = findViewById(R.id.textView_description);
        textViewPrice = findViewById(R.id.textView_price);
        textViewNickname = findViewById(R.id.textView_nickname);

        imageViewUserImg = findViewById(R.id.imageView_profile);
        imageViewImg = findViewById(R.id.imageView_photo);

        editTextComment = findViewById(R.id.editText_comment);
        buttonCommentSubmit = findViewById(R.id.button_comment_submit);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            Date inDate = AppConstants.dateFormat2.parse(item.getDate());
            textViewDate.setText(AppConstants.dateFormat.format(inDate));
        } catch (Exception e) {
            Log.e(TAG, "date format error");
        }

        textViewTitle.setText(item.getTitle());
        textViewDescript.setText(item.getDescription());
        textViewPrice.setText(item.getPrice() + "원");

        Member writer = item.getWriter(); //작성자 가져오기
        textViewNickname.setText(writer.getNickname());//닉네임

        String profilePath = writer.getProfilePath();
        if (!TextUtils.isEmpty(profilePath))
            new DownloadFilesTask(this, imageViewUserImg, AppConstants.ABSOLUTE_PATH).execute(profilePath);

        String imgpath = item.getImgPath();
        if (!TextUtils.isEmpty(imgpath))
            new DownloadFilesTask(this, imageViewImg, AppConstants.RELATEVE_PATH).execute(imgpath);

        buttonCommentSubmit.setOnClickListener(new View.OnClickListener() { //댓글 등록 버튼
            @Override
            public void onClick(View v) {
                insertComment(); //댓글 작성
                editTextComment.getText().clear();
                // 키보드 내려기
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                updatePost(item.getMk_no());
            }
        });
    }

    private void updatePost(int mk_no) {
        String[] paramNames = {"m_id", "p_type","p_no"};
        String[] values = {String.valueOf(member.getId()), AppConstants.MARKET_BINARY, Integer.toString(mk_no)};
        GetDataTask task = new GetDataTask(this, paramNames, values, AppConstants.MODE_READ);
        task.execute("community/get_post.php",null,null);
        adapter.notifyDataSetChanged();
    }

    public void insertComment() {
        String paramNames[] = {"p_no", "p_type", "m_id", "c_description"};
        String values[] = {Integer.toString(item.getMk_no()), "01", Integer.toString(member.getId()), editTextComment.getText().toString()};
        InsertDataTask insertDataTask = new InsertDataTask(this, paramNames, values);
        insertDataTask.execute("community/insert_comment.php", null, null);
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
            this.item = readPost;

            onResume();

            //comment
            ArrayList<Comment> items = new ArrayList<>();
            if(!p_and_c.isNull("commentlist"))
            {
                JSONArray jsonComment = p_and_c.getJSONArray("commentlist");
                for (int i = 0; i < jsonComment.length(); i++)
                {
                    JSONObject item = jsonComment.getJSONObject(i);
                    JSONObject c_writer = item.getJSONObject("writer");
                    int c_m_id = c_writer.getInt("m_id");
                    String c_m_name = c_writer.getString("m_name");
                    String c_m_profile = null;
                    if (!c_writer.isNull("m_profile"))
                        c_m_profile = c_writer.getString("m_profile");

                    JSONObject comment = item.getJSONObject("comment");
                    int p_no = comment.getInt("p_no");
                    int p_type = comment.getInt("p_type");
                    int c_no = comment.getInt("c_no");
                    String c_description = comment.getString("c_description");
                    String c_date = comment.getString("c_date");
                    Comment readComment = new Comment(p_no, p_type, new Member(c_m_id, c_m_name, c_m_profile), c_no, c_description, c_date);
                    items.add(readComment);
                }
            }
            adapter.setItems(items);
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.d(TAG, "showResult: ", e);
        }
    }

    @Override
    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void noResultNotice(String searchString) {

    }

}