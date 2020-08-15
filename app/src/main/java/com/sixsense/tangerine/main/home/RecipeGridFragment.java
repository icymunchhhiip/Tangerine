package com.sixsense.tangerine.main.home;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.main.home.util.ExpandableHeightGridView;
import com.sixsense.tangerine.main.home.util.RecipeGridAdapter;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.HttpInterface;
import com.sixsense.tangerine.network.RecipeIntroList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.sixsense.tangerine.MainActivity.MY_ACCOUNT;

public class RecipeGridFragment extends Fragment {

    private ExpandableHeightGridView mGridView;
    private List<RecipeIntroList.RecipeIntro> recipeIntroList;
    private int page_no;
    private int has_more;

    public RecipeGridFragment(){
        this.page_no = 1;
        this.has_more = 0;
        this.recipeIntroList = new ArrayList<>(0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_recipe_recycler, container, false);

        mGridView = view.findViewById(R.id.grid_recipe);

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
            RecipeGridAdapter recipeGridAdapter = new RecipeGridAdapter(getContext(), recipeIntroList);
            mGridView.setExpanded(true);

            mGridView.setAdapter(recipeGridAdapter);
//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
////                Toast.makeText(getActivity(), "You Clicked at " + city_names[position], Toast.LENGTH_SHORT).show();
//                    assert getFragmentManager() != null;
//                    transaction=getFragmentManager().beginTransaction();
//                    city_no=position+1;
////                    PlaceListInCity.page_no=1;
////                    PlaceListInCity.has_more=1;
////                    PlaceListInCity.placesList=new ArrayList<>();
//                    placesInCity=new PlaceListInCity();
//                    transaction.replace(R.id.content,placesInCity).addToBackStack(null).commit();
//                }
//            });
        }

    }
}
