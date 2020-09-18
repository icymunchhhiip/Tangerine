package com.sixsense.tangerine.community;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sixsense.tangerine.OnTaskCompletedListener;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.MarketPost;
import com.sixsense.tangerine.community.item.Member;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    private MarketPost marketPostItem;
    private int p_type;
    private Member member;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity_base);

        Bundle bundle = getIntent().getExtras();

        marketPostItem = (MarketPost) bundle.getSerializable("marketItem");
        member = (Member) bundle.getSerializable("member");
        p_type = bundle.getInt("p_type");
    }

    @Override
    public void setContentView(int layoutResID) {
        LinearLayout fullView = (LinearLayout) getLayoutInflater().inflate(R.layout.community_activity_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //툴바 사용여부 결정(기본적으로 사용)
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        if (p_type == AppConstants.MARKET_SIG) {
            title.setText("장터");
        }
        if (p_type == AppConstants.TIPS_SIG) {
            title.setText("요리TIP");
        }
        if (useToolbar()) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }

    //툴바를 사용할지 말지 정함
    protected boolean useToolbar() {
        return true;
    }

    //메뉴 등록하기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);

        MenuItem item_edit = menu.findItem(R.id.action_edit);
        MenuItem item_delete = menu.findItem(R.id.action_delete);

        if (p_type == AppConstants.MARKET_SIG) {
            if (marketPostItem.getWriter().getId() == member.getId()) {
                item_edit.setVisible(true);
                item_delete.setVisible(true);
            }
        }

        return true;
    }

    //앱바 메뉴 클릭 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(getApplicationContext(), MarketWritingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", marketPostItem);
                bundle.putSerializable("member", member);
                bundle.putInt("mode", AppConstants.MODE_EDIT);
                intent.putExtras(bundle);
                startActivityForResult(intent, AppConstants.EDIT_OK);
                onBackPressed();
                return true;
            case R.id.action_delete:
                String[] paramNames = {"p_no"};
                String[] values = {Integer.toString(marketPostItem.getMk_no())};
                GetDataTask task = new GetDataTask(new OnTaskCompletedListener() {
                    @Override
                    public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {
                    }

                    @Override
                    public boolean jsonToItem(String jsonString) {return false;}


                    @Override
                    public void noResultNotice(String searchString) {
                    }
                }, paramNames, values, AppConstants.MODE_DELETE);
                task.execute("community/delete_marketpost.php");
                setResult(AppConstants.DELETE_OK);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}