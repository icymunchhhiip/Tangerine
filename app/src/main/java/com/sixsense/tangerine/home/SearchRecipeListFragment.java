package com.sixsense.tangerine.home;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
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

import retrofit2.Call;

import static com.sixsense.tangerine.MainActivity.MY_ACCOUNT;

public class SearchRecipeListFragment extends Fragment {

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private List<RecipeIntroList.RecipeIntro> recipeIntroList;
    private int page_no;
    private int has_more;

    public SearchRecipeListFragment(){
        this.page_no = 1;
        this.has_more = 0;
        this.recipeIntroList = new ArrayList<>(0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_recipe_recycler, container, false);
        recyclerView = view.findViewById(R.id.recipe_recycler);

        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        if (recipeIntroList.isEmpty()){
            new RecentRecipeCall().execute();
        } else if (has_more == 1){
            ++page_no;
            new RecentRecipeCall().execute();
        }
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class RecentRecipeCall extends AsyncTask<Void, Void, Void> {
        private  final int PAGE_SIZE = 10;

        @Override
        protected Void doInBackground(Void... voids) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            Call<RecipeIntroList> call = httpInterface.getRecentRecipe((int)MY_ACCOUNT.getId(), page_no, PAGE_SIZE);
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
                        new SearchRecipeListFragment.RecentRecipeCall().execute();
                    }
                }
            };
            recyclerView.addOnScrollListener(onScrollListener);
            recyclerView.setAdapter(new RecipeListAdapter(recipeIntroList));
            setHasOptionsMenu(true);
        }

    }
}
