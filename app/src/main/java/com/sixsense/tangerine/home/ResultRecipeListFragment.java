package com.sixsense.tangerine.home;

import android.annotation.SuppressLint;
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

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.HttpInterface;
import com.sixsense.tangerine.network.RecipeIntroList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class ResultRecipeListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private List<RecipeIntroList.RecipeIntro> mRecipeIntroList;
    private int mPageNo;
    private int mHasMore;

    private String mRecipeName;
    private Byte mKindByte;
    private Byte mLevelByte;
    private Byte mToolByte;
    private Byte mTimeByte;

    private static final String TAG = ResultRecipeListFragment.class.getSimpleName();

    public ResultRecipeListFragment(String recipeName, Byte kindByte, Byte levelByte, Byte toolByte, Byte timeByte) {
        this.mRecipeName = recipeName;
        this.mKindByte = kindByte;
        this.mLevelByte = levelByte;
        this.mToolByte = toolByte;
        this.mTimeByte = timeByte;

        this.mPageNo = 1;
        this.mHasMore = 0;
        this.mRecipeIntroList = new ArrayList<>(0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_recipe_recycler, container, false);
        mRecyclerView = view.findViewById(R.id.recipe_recycler);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (mRecipeIntroList.isEmpty()) {
            new ConditionRecipeCall().execute();
        } else if (mHasMore == 1) {
            ++mPageNo;
            new ConditionRecipeCall().execute();
        }

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class ConditionRecipeCall extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            int pageSize = 10;
            Call<RecipeIntroList> call = httpInterface.getRecipeCondition(mRecipeName, mKindByte, mLevelByte, mToolByte, mTimeByte, mPageNo, pageSize);
            try {
                RecipeIntroList resource = call.execute().body();
                mRecipeIntroList = resource.data;
                mHasMore = resource.hasMore;
            } catch (Exception e) {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int totalItemCount = mLayoutManager.getItemCount();
                    int lastVisible = mLayoutManager.findLastCompletelyVisibleItemPosition();
                    if ((lastVisible >= totalItemCount - 1) && mHasMore == 1) {
                        ++mPageNo;
                        new ConditionRecipeCall().execute();
                    }
                }
            };
            mRecyclerView.addOnScrollListener(onScrollListener);
            mRecyclerView.setAdapter(new RecipeListAdapter(mRecipeIntroList, ResultRecipeListFragment.this));
            setHasOptionsMenu(true);
        }
    }
}
