package com.sixsense.tangerine.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;

import java.util.ArrayList;


public class MymarketFragment extends Fragment {
    private ArrayList<Mymarket> items;
    private RecyclerView recyclerView;
    private MywrittenMarketAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.setting_activity_mywritten_market, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        createItems();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new MywrittenMarketAdapter(items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;

    }

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