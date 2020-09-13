package com.sixsense.tangerine.setting;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;

import java.util.ArrayList;

public class MywrittenMarketActivity extends AppCompatActivity {
    private ArrayList<Mymarket> items;
    private RecyclerView recyclerView;
    private MywrittenMarketAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_mywritten_market);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        createItems();
        adapter = new MywrittenMarketAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    //임의로 넣어놓음 DB연결할땐 지우면 됨.
    public void createItems() {
        items = new ArrayList<>();
        items.add(new Mymarket("싱싱한 파팜", "1000원", "7월 21일", "", "ic_default_profile.png", "나는야자취인"));
        items.add(new Mymarket("싱싱한 양파팜", "3000원", "7월 20일", "image1.jpg", "ic_default_profile.png", "자취만렙"));
        items.add(new Mymarket("싱싱한 파팜", "1000원", "7월 21일", "", "ic_default_profile.png", "나는야자취인"));
        items.add(new Mymarket("싱싱한 양파팜", "3000원", "7월 20일", "image1.jpg", "ic_default_profile.png", "자취만렙"));
        items.add(new Mymarket("싱싱한 파팜", "1000원", "7월 21일", "", "ic_default_profile.png", "나는야자취인"));
        items.add(new Mymarket("싱싱한 양파팜", "3000원", "7월 20일", "image1.jpg", "ic_default_profile.png", "자취만렙"));
        items.add(new Mymarket("싱싱한 파팜", "1000원", "7월 21일", "", "ic_default_profile.png", "나는야자취인"));
        items.add(new Mymarket("싱싱한 양파팜", "3000원", "7월 20일", "image1.jpg", "ic_default_profile.png", "자취만렙"));
    }
}