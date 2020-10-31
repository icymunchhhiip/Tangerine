package com.sixsense.tangerine.community;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.AppConstants;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.CookingTip;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.HttpInterface;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;

public class CookingTipListFragment extends Fragment {
    private RecyclerView recyclerView;
    private CookingTipAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.community_fragment_cookingtip, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getContext(),2);
        adapter = new CookingTipAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        new TipsCall().execute();

        adapter.setOnItemClickListener(new OnCookingTipListItemClickListener() { //게시글 누르면

            public void onItemClick(CookingTipAdapter.ViewHolder holder, View view, int position) {
                CookingTip item = adapter.getItem(position);

                Intent intent = new Intent(getContext(), CookingTipActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("tipsItem", item);
                bundle.putInt("p_type", AppConstants.TIPS_SIG);//putInt로 넘겨주고 여기에서 getInt로 전달받는다
                intent.putExtras(bundle);//putExtras로 Bundle 데이터를 넘겨주고 여기에서getExtras로 데이터를 참조한다.
                startActivityForResult(intent, AppConstants.DELETE_OK);
            }
        });

        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private class TipsCall extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            Call<ArrayList<CookingTip>> call = httpInterface.getTipsImgs();
            try {
                ArrayList<CookingTip> list = call.execute().body();
                adapter.addItems(list);
            } catch (Exception e) {
                Log.e("CookingTipFragment", Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }

    }
}
