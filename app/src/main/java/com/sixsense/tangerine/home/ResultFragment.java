package com.sixsense.tangerine.home;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.HttpInterface;
import com.sixsense.tangerine.network.RecipeIntroList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ResultFragment extends Fragment {
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private List<RecipeIntroList.RecipeIntro> recipeIntroList;
    private int page_no;
    private int has_more;

    private String recipeName;
    private Byte kindByte;
    private Byte levelByte;
    private Byte toolByte;
    private Byte timeByte;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_result,container,false);
        recyclerView = view.findViewById(R.id.grid_search_recipe_frame);

        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        if (recipeIntroList.isEmpty()){
            new ConditionRecipeCall().execute();
        } else if (has_more == 1){
            ++page_no;
            new ConditionRecipeCall().execute();
        }

//        SearchRecipeListFragment searchRecipeListFragment = new SearchRecipeListFragment();
//        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.grid_search_recipe_frame, searchRecipeListFragment);
//        fragmentTransaction.commit();

        return view;
    }

    public ResultFragment(){
        this.page_no = 1;
        this.has_more = 0;
        this.recipeIntroList = new ArrayList<>(0);

        this.recipeName = getArguments().getString("recipe_name", " ");
        this.kindByte = binaryStringToByte(getArguments().getString("kind_byte", "00000000"));
        this.levelByte = binaryStringToByte(getArguments().getString("level_byte", "000000000"));
        this.toolByte = binaryStringToByte(getArguments().getString("tool_byte","00000000"));
        this.timeByte = binaryStringToByte(getArguments().getString("time_byte","00000000"));
    }

    @SuppressLint("StaticFieldLeak")
    private class ConditionRecipeCall extends AsyncTask<Void, Void, Void> {
        private  final int PAGE_SIZE = 10;

        @Override
        protected Void doInBackground(Void... voids) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            Call<RecipeIntroList> call = httpInterface.getRecipeCondition(recipeName,kindByte,levelByte,toolByte,timeByte,page_no,PAGE_SIZE);
            RecipeIntroList resource = null;
            try {
                resource = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            recipeIntroList = resource.data;

            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
//                    assert layoutManager != null;
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
                    if ((lastVisible >= totalItemCount - 1) && has_more == 1) {
                        ++page_no;
                        new ResultFragment.ConditionRecipeCall().execute();
                    }
                }
            };
            recyclerView.addOnScrollListener(onScrollListener);
            recyclerView.setAdapter(new RecipeListAdapter(recipeIntroList));
            setHasOptionsMenu(true);
        }

    }

    private byte binaryStringToByte(String s) {
        byte ret = 0, total = 0;
        for (int i = 0; i < 8; ++i) {
            ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0;
            total = (byte) (ret | total);
        }
        return total;
    }
}
