package com.sixsense.tangerine.community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.MainActivity;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.BoardPost;
import com.sixsense.tangerine.community.item.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BoardListFragment extends Fragment implements OnTaskCompletedListener {
    private static final String TAG = "BoardListFragment";

    ArrayList<BoardPost> items = new ArrayList<>();

    private BoardPostAdapter adapter;
    private MyCommunityListener myCommunityListener;
    private Member member;

    private EditText editTextSearch;
    private TextView textViewKeyword, textViewNoResult;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            myCommunityListener = (MyCommunityListener) getActivity();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (myCommunityListener != null) {
            this.member = myCommunityListener.getMember();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.community_fragment_board_list, container, false);

        textViewKeyword = rootView.findViewById(R.id.textView_keyword);
        textViewNoResult = rootView.findViewById(R.id.textView_noResult);
        textViewKeyword.setVisibility(View.GONE);
        textViewNoResult.setVisibility(View.GONE);

        ImageButton buttonWriting = rootView.findViewById(R.id.button_writing); //글쓰기 이동
        buttonWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BoardWritingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("member", member);
                bundle.putInt("mode", AppConstants.MODE_WRITE);
                intent.putExtras(bundle);
                startActivityForResult(intent, AppConstants.WRITE_OK);
            }
        });

        Context context = rootView.getContext();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_board);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BoardPostAdapter();
        recyclerView.setAdapter(adapter);

        loadPost();
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new OnBoardPostItemClickListener() { //게시글 누르면
            @Override
            public void onItemClick(BoardPostAdapter.ViewHolder holder, View view, int position) {
                BoardPost item = adapter.getItem(position);

                Intent intent = new Intent(getContext(), BoardReadingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("boardItem", item);
                bundle.putSerializable("member", member);

                bundle.putInt("p_type", AppConstants.BOARD_SIG);
                intent.putExtras(bundle);
                startActivityForResult(intent, AppConstants.DELETE_OK);
            }
        });

        editTextSearch = rootView.findViewById(R.id.editText_search);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    textViewKeyword.setVisibility(View.GONE);
                    textViewNoResult.setVisibility(View.GONE);

                    if (editTextSearch.getText().toString() != null) {
                        String paramNames[] = new String[]{"m_id", "search"};
                        String values[] = {String.valueOf(member.getId()), editTextSearch.getText().toString()};
                        GetDataTask task = new GetDataTask(BoardListFragment.this,paramNames, values, AppConstants.MODE_SEARCH);
                        task.execute("community/search_board.php");
                        adapter.notifyDataSetChanged();
                        editTextSearch.setText("");
                        // 키보드 내려기
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } else
                        Toast.makeText(getContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    private void loadPost() {
        String paramNames[] = {"m_id"};
        String values[] = {String.valueOf(member.getId())};
        GetDataTask task = new GetDataTask(this,paramNames,values, AppConstants.MODE_READ);
        task.execute("community/read_board.php");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.WRITE_OK || requestCode == AppConstants.DELETE_OK || requestCode == AppConstants.EDIT_OK) {
            if (myCommunityListener != null) {
                myCommunityListener.showBoardListFragment(); //프래그먼트 갱신
            }
        }
    }

    @Override
    public void jsonToItem(String jsonString) {
        String TAG_JSON = "boardlist";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                JSONObject writer = item.getJSONObject("writer");
                JSONObject post = item.getJSONObject("post");
                Boolean myLike = item.getBoolean("my_like");

                int m_id = writer.getInt("m_id");
                String m_name = writer.getString("m_name");
                String m_profile = null;
                if(!writer.isNull("m_profile"))
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
                items.add(readPost);
            }
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e(TAG, "jsonToItem: ", e);
        }
    }

    @Override
    public void noResultNotice(String searchString) {
        ArrayList<BoardPost> tmpItems = new ArrayList<>();
        adapter.setItems(tmpItems);
        adapter.notifyDataSetChanged();

        textViewKeyword.setText("'" + searchString  + "'");
        textViewKeyword.setVisibility(View.VISIBLE);
        textViewNoResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}