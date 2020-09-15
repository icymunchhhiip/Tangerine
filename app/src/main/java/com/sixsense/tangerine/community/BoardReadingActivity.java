package com.sixsense.tangerine.community;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.BoardPost;
import com.sixsense.tangerine.community.item.Comment;
import com.sixsense.tangerine.community.item.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class BoardReadingActivity extends BaseActivity implements OnTaskCompletedListener {
    private static final String TAG = "BoardReadingActivity";

    private BoardPost item;
    private Member member;

    private TextView textViewCategory;
    private TextView textViewDate;
    private TextView textViewTitle;
    private TextView textViewDescript;
    private TextView textViewLikes;
    private TextView textViewNickname;
    private ImageView imageViewHeart;
    private ImageView imageViewUserImg;
    private ImageView imageViewImg;

    private EditText editTextComment;
    private Button buttonCommentSubmit;

    private CommentAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity_board_reading);

        Bundle bundle = getIntent().getExtras();
        item = (BoardPost) bundle.getSerializable("boardItem");
        member = (Member) bundle.get("member");

        Context context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerView_bComment);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CommentAdapter();
        recyclerView.setAdapter(adapter);

        updatePost(item.getB_no());

        textViewCategory = findViewById(R.id.textView_category);
        textViewDate = findViewById(R.id.textView_date);
        textViewTitle = findViewById(R.id.textView_title);
        textViewDescript = findViewById(R.id.textView_description);
        textViewLikes = findViewById(R.id.textView_likes);
        textViewNickname = findViewById(R.id.textView_nickname);

        imageViewUserImg = findViewById(R.id.imageView_profile);
        imageViewImg = findViewById(R.id.imageView_photo);
        imageViewHeart = findViewById(R.id.imageView_heart);

        editTextComment = findViewById(R.id.editText_comment);
        buttonCommentSubmit = findViewById(R.id.button_comment_submit);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(item.isMyLike()){ //내가 좋아요 한 게시글이면
            imageViewHeart.setImageResource(R.drawable.ic_favorite_selected_24dp);
        }

        switch (item.getCategory()) {
            case 1:
                textViewCategory.setText("잡담");
                break;
            case 2:
                textViewCategory.setText("꿀팁");
                break;
            case 3:
                textViewCategory.setText("질문");
                break;
            default:
                textViewCategory.setText("오류");
        }

        try {
            Date inDate = AppConstants.dateFormat2.parse(item.getDate());
            textViewDate.setText(AppConstants.dateFormat.format(inDate));
        } catch (Exception e) {
            Log.e(TAG, "date format error");
        }

        textViewTitle.setText(item.getTitle());
        textViewDescript.setText(item.getDescription());
        textViewLikes.setText(Integer.toString(item.getLikes()));

        /*-------------------------------------------------------------------------------------------------------*/
        final Member writer = item.getWriter(); //작성자 가져오기
        textViewNickname.setText(writer.getNickname());//닉네임

        String profilePath = writer.getProfilePath();
        if(!TextUtils.isEmpty(profilePath))
            new DownloadFilesTask(this,imageViewUserImg, AppConstants.ABSOLUTE_PATH).execute(profilePath);

        String imgpath = item.getImgPath();
        if(!TextUtils.isEmpty(imgpath))
            new DownloadFilesTask(this,imageViewImg, AppConstants.RELATEVE_PATH).execute(imgpath);

//        imageViewHeart.setOnClickListener(new View.OnClickListener() { //좋아요 버튼
//            @Override
//            public void onClick(View v) {
//                String paramNames[] = {"m_id", "b_no", "l_type", "my_like"};
//                if (item.isMyLike() == false) { // 좋아요
//                    imageViewHeart.setImageResource(R.drawable.ic_favorite_selected_24dp);
//                    item.LikePlus();
//                    String values[] = {Integer.toString(member.getId()), Integer.toString(item.getB_no()), AppConstants.BOARD_BINARY, "like"};
//                    InsertDataTask insertDataTask = new InsertDataTask(BoardReadingActivity.this,paramNames,values);
//                    insertDataTask.execute("community/press_like.php",null,null);
//                    textViewLikes.setText(Integer.toString(item.getLikes()));
//                    item.setMyLike(true);
//
//                } else { // 좋아요 취소
//                    imageViewHeart.setImageResource(R.drawable.ic_favorite_unselected_24dp);
//                    item.LikeMinus();
//                    String values[] = {Integer.toString(member.getId()), Integer.toString(item.getB_no()), AppConstants.BOARD_BINARY, "cancel"};
//                    InsertDataTask insertDataTask = new InsertDataTask(BoardReadingActivity.this,paramNames,values);
//                    insertDataTask.execute("community/press_like.php",null,null);
//                    textViewLikes.setText(Integer.toString(item.getLikes()));
//                    item.setMyLike(false);
//                }
//            }
//        });
//
//        buttonCommentSubmit.setOnClickListener(new View.OnClickListener() { //댓글 등록 버튼
//            @Override
//            public void onClick(View v) {
//                insertComment();
//                // 키보드 내려기
//                editTextComment.getText().clear();
//                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                updatePost(item.getB_no());
//            }
//        });
    }

//    public void insertComment(){
//        String paramNames[] = {"p_no","p_type","m_id","c_description"};
//        String values[] = {Integer.toString(item.getB_no()), AppConstants.BOARD_BINARY, Integer.toString(member.getId()), editTextComment.getText().toString()};
//        InsertDataTask insertDataTask = new InsertDataTask(this,paramNames,values);
//        insertDataTask.execute("community/insert_comment.php",null,null);
//    }


    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    private void updatePost(int b_no) {
        String paramNames[] = {"m_id","p_type","p_no"};
        String values[] = {String.valueOf(member.getId()), AppConstants.BOARD_BINARY,String.valueOf(b_no)};
        GetDataTask task = new GetDataTask(this,paramNames,values, AppConstants.MODE_READ);
        task.execute("community/get_post.php",null,null);
        adapter.notifyDataSetChanged();
    }


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
    public void noResultNotice(String searchString) {
    }

}