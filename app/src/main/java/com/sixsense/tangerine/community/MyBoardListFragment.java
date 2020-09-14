package com.sixsense.tangerine.community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.BoardPost;
import com.sixsense.tangerine.community.item.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyBoardListFragment extends Fragment implements OnTaskCompletedListener {
    private static final String TAG = "MyBoardListFragment";

    private MyPostsListener myPostsListener;
    private Member member;
    private BoardPostAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof MyPostsActivity) {
            myPostsListener = (MyPostsListener) getActivity();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (myPostsListener != null) {
            this.member = myPostsListener.getMember();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.community_fragment_myboard_list, container, false);

        Context context = rootView.getContext();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_myboard);
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
//                if (myUpdateListener != null) {
//                    myUpdateListener.onReceivedBoardPost(item);
//                }

                Intent intent = new Intent(getContext(), BoardReadingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("boardItem", item);
                bundle.putSerializable("member", member);
                bundle.putInt("p_type", AppConstants.BOARD_SIG);
                intent.putExtras(bundle);
                startActivityForResult(intent, AppConstants.DELETE_OK);
            }
        });

        return rootView;
    }

    private void loadPost() {
        String paramNames[] = {"m_id"};
        String values[] = {String.valueOf(member.getId())};
        GetDataTask task = new GetDataTask(this,paramNames,values,AppConstants.MODE_READ);
        task.execute("community/read_myboard.php");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.DELETE_OK || requestCode == AppConstants.EDIT_OK) {
            if (myPostsListener != null) {
                myPostsListener.showMyBoardListFragment(); //프래그먼트 갱신
            }
        }
    }

    @Override
    public void jsonToItem(String jsonString) {
        String TAG_JSON = "myboardlist";
        ArrayList<BoardPost> items = new ArrayList<>();
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
    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void noResultNotice(String searchString) {

    }
}