package com.sixsense.tangerine.setting;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;

import java.util.ArrayList;

public class MywrittenRecipeActivity extends AppCompatActivity {
    private ArrayList<Recipe> items;
    private RecyclerView recyclerView;
    private MywrittenRecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_mywritten_recipe);

        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        createItems();
        adapter = new MywrittenRecipeAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    //임의로 넣어놓음 DB연결할땐 지우면 됨.
    public void createItems() {
        items = new ArrayList<>();
        items.add(new Recipe("떡국끓이기","13분", "#초보","12345"));
        items.add(new Recipe("떡국끓이기","13분", "#초보","12345"));
        items.add(new Recipe("떡국끓이기","13분", "#초보","12345"));
        items.add(new Recipe("떡국끓이기","13분", "#초보","12345"));
        items.add(new Recipe("떡국끓이기","13분", "#초보","12345"));
        items.add(new Recipe("떡국끓이기","13분", "#초보","12345"));
    }
}