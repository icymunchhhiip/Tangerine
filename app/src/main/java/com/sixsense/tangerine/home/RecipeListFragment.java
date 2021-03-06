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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sixsense.tangerine.MainActivity;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.HttpInterface;
import com.sixsense.tangerine.network.RecipeIntroList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class RecipeListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private List<RecipeIntroList.RecipeIntro> mRecipeIntroList;
    private int mPageNo;
    private int mHasMore;
    private static final String TAG = RecipeListFragment.class.getSimpleName();
    private String mAction;

    private String mRecipeName;
    private Byte mKindByte;
    private Byte mLevelByte;
    private Byte mToolByte;
    private Byte mTimeByte;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public RecipeListFragment(String mAction, @Nullable String[] conditions) {
        this.mAction = mAction; //like //written //recent //result
        this.mPageNo = 1;
        this.mHasMore = 0;
        this.mRecipeIntroList = new ArrayList<>(0);
        Log.d(TAG,mAction);

        if(conditions !=null){
            this.mRecipeName = conditions[0];
            this.mKindByte = binaryStringToByte(conditions[1]);
            this.mLevelByte = binaryStringToByte(conditions[2]);
            this.mToolByte = binaryStringToByte(conditions[3]);
            this.mTimeByte = binaryStringToByte(conditions[4]);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_recipe_recycler, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary
        );

        mRecyclerView = view.findViewById(R.id.recipe_recycler);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (mRecipeIntroList.isEmpty()) {
            if(mAction.equals("recent")){
                new RecentRecipeCall().execute();

            }
            else if(mAction.equals("result")){
                new ConditionRecipeCall().execute();
            }
            else {
                new MyRecipeCall().execute();
            }
        }

//        else if (mHasMore == 1) {
//            ++mPageNo;
//            new MyRecipeCall().execute();
//        }
        return view;
    }

    @Override
    public void onRefresh() {
        mRecipeIntroList.clear();
        mPageNo = 1;
        if (mAction.equals("recent")) {
            new RecentRecipeCall().execute();
        } else if (mAction.equals("result")) {
            new ConditionRecipeCall().execute();
        } else {
            new MyRecipeCall().execute();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @SuppressLint("StaticFieldLeak")
    private class MyRecipeCall extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            int pageSize = 10;
            Call<RecipeIntroList> call = httpInterface.getMyRecipe(mAction, MainActivity.sMyId, mPageNo, pageSize);
            try {
                RecipeIntroList resource = call.execute().body();
                mRecipeIntroList.addAll(resource.data);
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
                        new MyRecipeCall().execute();
                    }

                }
            };
            mRecyclerView.addOnScrollListener(onScrollListener);

            mRecyclerView.setAdapter(new RecipeListAdapter(mRecipeIntroList, RecipeListFragment.this));
            setHasOptionsMenu(true);
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class RecentRecipeCall extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            int pageSize = 10;
            Call<RecipeIntroList> call = httpInterface.getRecentRecipe(MainActivity.sMyId, mPageNo, pageSize);
            try {
                RecipeIntroList resource = call.execute().body();
                mRecipeIntroList.addAll(resource.data);
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
                        new RecentRecipeCall().execute();
                    }

                }
            };
            mRecyclerView.addOnScrollListener(onScrollListener);
            mRecyclerView.setAdapter(new RecipeListAdapter(mRecipeIntroList, RecipeListFragment.this));
            setHasOptionsMenu(true);
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class ConditionRecipeCall extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            int pageSize = 10;
            Call<RecipeIntroList> call = httpInterface.getRecipeCondition(MainActivity.sMyId,mRecipeName, mKindByte, mLevelByte, mToolByte, mTimeByte, mPageNo, pageSize);
            try {
                RecipeIntroList resource = call.execute().body();
                mRecipeIntroList.addAll(resource.data);
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
            mRecyclerView.setAdapter(new RecipeListAdapter(mRecipeIntroList, RecipeListFragment.this));
            setHasOptionsMenu(true);
        }
    }

    private byte binaryStringToByte(String s) {
        if (s == null || s.equals("0000")) {
            s = "00001111";
        }
        byte ret = 0, total = 0;
        for (int i = 0; i < 8; ++i) {
            ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0;
            total = (byte) (ret | total);
        }
        return total;
    }
}