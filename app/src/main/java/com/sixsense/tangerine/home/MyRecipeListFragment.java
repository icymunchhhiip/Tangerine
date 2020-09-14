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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

import static com.sixsense.tangerine.MainActivity.sMyAccount;

public class MyRecipeListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private List<RecipeIntroList.RecipeIntro> mRecipeIntroList;
    private int mPageNo;
    private int mHasMore;
    private static final String TAG = MyRecipeListFragment.class.getSimpleName();
    private String mAction;

    public MyRecipeListFragment(String mAction) {
        this.mAction = mAction; //like //written
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
        ItemOffsetDecoration itemOffsetDecoration = new ItemOffsetDecoration(view.getContext(), R.dimen.card_offset);
        mRecyclerView.addItemDecoration(itemOffsetDecoration);

        if (mRecipeIntroList.isEmpty()) {
            new MyRecipeCall().execute();
        } else if (mHasMore == 1) {
            ++mPageNo;
            new MyRecipeCall().execute();
        }
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class MyRecipeCall extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            int pageSize = 10;
            Call<RecipeIntroList> call = httpInterface.getMyRecipe(mAction,(int) sMyAccount.getId(), mPageNo, pageSize);
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

            mRecyclerView.setAdapter(new RecipeListAdapter(mRecipeIntroList, MyRecipeListFragment.this));
            setHasOptionsMenu(true);
        }

    }

}