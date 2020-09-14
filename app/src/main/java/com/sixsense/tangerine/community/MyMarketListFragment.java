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
import com.sixsense.tangerine.community.item.MarketPost;
import com.sixsense.tangerine.community.item.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyMarketListFragment extends Fragment implements OnTaskCompletedListener {
    private static final String TAG = "MyMarketListFragment";

    private MyPostsListener myPostsListener;
    private Member member;
    private MarketPostAdapter adapter;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.community_fragment_mymarket_list, container, false);

        Context context = rootView.getContext();
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView_mymarket);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MarketPostAdapter();
        recyclerView.setAdapter(adapter);

        String[] paramNames ={"m_id"};
        String[] values = {Integer.toString(member.getId())};
        GetDataTask task = new GetDataTask(this,paramNames,values,AppConstants.MODE_READ);
        task.execute("community/read_mymarket.php");
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new OnMarketPostItemClickListener() { //장터 게시글 누르면
            @Override
            public void onItemClick(MarketPostAdapter.ViewHolder holder, View view, int position) { //게시글 클릭 이벤트
                MarketPost item = adapter.getItem(position);
//                if(myUpdateListener != null) {
//                    myUpdateListener.onReceivedMarketPost(item);
//                }

                Intent intent = new Intent(getContext(),MarketReadingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("marketItem",item);
                bundle.putSerializable("member", member);
                bundle.putInt("p_type", AppConstants.MARKET_SIG);
                intent.putExtras(bundle);
                startActivityForResult(intent, AppConstants.DELETE_OK);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AppConstants.DELETE_OK || requestCode == AppConstants.EDIT_OK){
            if(myPostsListener != null){
                myPostsListener.showMyMarketListFragment(); //프래그먼트 갱신
            }
        }
    }

    @Override
    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {

    }

    @Override
    public void jsonToItem(String jsonString) {
        String TAG_JSON = "mymarketlist";
        ArrayList<MarketPost> items = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                JSONObject writer = item.getJSONObject("writer");
                JSONObject post = item.getJSONObject("post");

                int m_id = writer.getInt("m_id");
                String m_name = writer.getString("m_name");
                String m_profile = null;
                if(!writer.isNull("m_profile"))
                    m_profile = writer.getString("m_profile");

                int mk_no = post.getInt("mk_no");
                int mk_localcode = post.getInt("mk_localcode");
                int mk_price = post.getInt("mk_price");
                String mk_title = post.getString("mk_title");
                String mk_description = post.getString("mk_description");
                String mk_date = post.getString("mk_date");
                String mk_imgpath = null;
                if(!post.isNull("mk_imgpath"))
                    mk_imgpath = post.getString("mk_imgpath");

                MarketPost readPost = new MarketPost(new Member(m_id,m_name,m_profile), mk_no, mk_localcode, mk_price, mk_title, mk_description, mk_date, mk_imgpath);
                items.add(readPost);
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