package com.sixsense.tangerine.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.sixsense.tangerine.OnTaskCompletedListener;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.AppConstants;
import com.sixsense.tangerine.community.GetDataTask;
import com.sixsense.tangerine.community.MarketPostAdapter;
import com.sixsense.tangerine.community.MarketReadingActivity;
import com.sixsense.tangerine.community.OnMarketPostItemClickListener;
import com.sixsense.tangerine.community.item.MarketPost;
import com.sixsense.tangerine.community.item.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyMarketPostActivity extends AppCompatActivity implements OnTaskCompletedListener {
    private static final String TAG = "MyPostActivity";

    private Member member;
    private MarketPostAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_mypost_list);

        Intent intent = getIntent();
        this.member = (Member) intent.getSerializableExtra("member");

        Context context = getApplicationContext();
        RecyclerView recyclerView = findViewById(R.id.recyclerView_mymarket);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MarketPostAdapter();
        recyclerView.setAdapter(adapter);

        String[] paramNames ={"m_id"};
        String[] values = {Integer.toString(member.getId())};
        GetDataTask task = new GetDataTask(this,paramNames,values, AppConstants.MODE_READ);
        task.execute("community/read_mymarket.php");
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new OnMarketPostItemClickListener() { //장터 게시글 누르면
            @Override
            public void onItemClick(MarketPostAdapter.ViewHolder holder, View view, int position) { //게시글 클릭 이벤트
                MarketPost item = adapter.getItem(position);
//                if(myUpdateListener != null) {
//                    myUpdateListener.onReceivedMarketPost(item);
//                }

                Intent intent = new Intent(getApplicationContext(), MarketReadingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("marketItem",item);
                bundle.putSerializable("member", member);
                intent.putExtras(bundle);
                startActivityForResult(intent, AppConstants.DELETE_OK);
            }
        });
    }

    @Override
    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {

    }

    @Override
    public boolean jsonToItem(String jsonString) {
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
            return true;
        } catch (JSONException e) {
            Log.d(TAG, "showResult: ", e);
        }
        return false;
    }

    @Override
    public void noResultNotice(String searchString) {

    }
}
