package com.sixsense.tangerine.home;

import android.annotation.SuppressLint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sixsense.tangerine.MainActivity;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.main.MainPagerFragmentDirections;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.HttpInterface;
import com.sixsense.tangerine.network.RecipeIntroList;
import com.sixsense.tangerine.setting.MylikeRecipeFragment;
import com.sixsense.tangerine.setting.MylikeRecipeFragmentDirections;
import com.sixsense.tangerine.setting.MywrittenRecipeFragmentDirections;

import java.util.List;

import retrofit2.Call;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
    private View mView;
    private List<RecipeIntroList.RecipeIntro> mRecipeIntro;
    private RecipeViewHolder mRecipeViewHolder;
    private String mCheckedState;
    private Fragment mFragment;

    public RecipeListAdapter(List<RecipeIntroList.RecipeIntro> recipeIntro, Fragment fragment) {
        this.mRecipeIntro = recipeIntro;
        this.mFragment = fragment;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_recipe_item, parent, false);

        return new RecipeViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, final int position) {
        mRecipeViewHolder = holder;
        String baseUrl = HttpClient.BASE_URL + "recipe/imgs/";
        Glide.with(mView.getContext())
                .load(baseUrl + mRecipeIntro.get(position).recipeImg)
                .into(mRecipeViewHolder.recipeImg);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != RecyclerView.NO_POSITION) {
                    NavDirections navDirections;
//                    ConstraintLayout layout = mFragment.getActivity().findViewById(R.id.main_layout);
//                    Toolbar toolbar = mView.findViewById(R.id.toolbar_show_title);
//                    layout.findViewById(R.id.toolbar_home).setVisibility(View.GONE);
//                    toolbar.setVisibility(View.VISIBLE);
//                    Button buttonEdit = mView.findViewById(R.id.edit_recipe);
//                    Button buttonDel = mView.findViewById(R.id.del_recipe);
//                    if (mRecipeIntro.get(position).memId == MainActivity.sMyAccount.getId()) {
//                        buttonEdit.setVisibility(View.VISIBLE);
//                        buttonDel.setVisibility(View.VISIBLE);
//                    } else {
//                        buttonEdit.setVisibility(View.INVISIBLE);
//                        buttonDel.setVisibility(View.INVISIBLE);
//                    }
//                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mFragment.getActivity().onBackPressed();
//                        }
//                    });
                    String navLabel = (String) Navigation.findNavController(mFragment.getView()).getCurrentDestination().getLabel();
                    if (mFragment instanceof RecentRecipeListFragment) {
                        navDirections = MainPagerFragmentDirections.actionMainPagerFragmentToInRecipeFragment(mRecipeIntro.get(position));
                    } else if (mFragment instanceof ResultRecipeListFragment) {
                        navDirections = ResultFragmentDirections.actionResultFragmentToInRecipeFragment(mRecipeIntro.get(position));
                    } else if (navLabel.equals("MylikeRecipeFragment")){
                        navDirections = MylikeRecipeFragmentDirections.actionMylikeRecipeFragmentToInRecipeFragment(mRecipeIntro.get(position));
                    } else {
                        navDirections = MywrittenRecipeFragmentDirections.actionMywrittenRecipeFragmentToInRecipeFragment(mRecipeIntro.get(position));
                    }
                    Navigation.findNavController(mFragment.getView()).navigate(navDirections);

                }
            }
        });

        mRecipeViewHolder.recipeName.setText(mRecipeIntro.get(position).recipeName);
        mRecipeViewHolder.recipeMin.setText(mRecipeIntro.get(position).recipeMin);
        mRecipeViewHolder.recipeTags.setText(mRecipeIntro.get(position).recipeTags);

        Glide.with(mView.getContext())
                .load(mRecipeIntro.get(position).memProfile)
                .into(mRecipeViewHolder.memProfile);
        mRecipeViewHolder.memProfile.setBackground(new ShapeDrawable(new OvalShape()));
        mRecipeViewHolder.memProfile.setClipToOutline(true);

        mRecipeViewHolder.memName.setText(mRecipeIntro.get(position).memName);

        if (mRecipeIntro.get(position).recipeFav == 1) {
            mRecipeViewHolder.recipeFav.setChecked(true);
        } else {
            mRecipeViewHolder.recipeFav.setChecked(false);
        }

        mRecipeViewHolder.recipeFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mRecipeIntro.get(position).recipeFav == 1) {
                    mCheckedState = "save";
                    new LikeCall().execute(mRecipeIntro.get(position).recipeId);
                    if (mCheckedState.equals("saved") || mCheckedState.equals("deleted_fail") || mCheckedState.equals("already_save")) {
                        mRecipeIntro.get(position).recipeFav = 1;
                        mRecipeViewHolder.recipeFav.setChecked(true);
                    }
                } else {
                    mCheckedState = "del";
                    new LikeCall().execute(mRecipeIntro.get(position).recipeId);
                    if (mCheckedState.equals("deleted") || mCheckedState.equals("saved_fail") || mCheckedState.equals("already_del")) {
                        mRecipeIntro.get(position).recipeFav = 0;
                        mRecipeViewHolder.recipeFav.setChecked(false);
                    }
                }
            }
        });

        mRecipeViewHolder.recipeFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRecipeIntro.get(position).recipeFav = 1;
                } else {
                    mRecipeIntro.get(position).recipeFav = 0;
                }
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private class LikeCall extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            Call<String> call = httpInterface.setRecipeLike((int) MainActivity.sMyAccount.getId(), integers[0], mCheckedState);
            String response = null;
            try {
                response = call.execute().body();
            } catch (Exception ignore) {}
            return response;
        }

        @Override
        protected void onPostExecute(String string) {
            switch (string) {
                case "saved":
                case "deleted_fail":
                case "already_save":
                    break;
                case "deleted":
                case "saved_fail":
                case "already_del":
                    break;
                default:
            }
        }
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipeImg;
        private TextView recipeName;
        private TextView recipeMin;
        private TextView recipeTags;
        private ImageView memProfile;
        private TextView memName;
        private CheckBox recipeFav;
        private View view;

        public RecipeViewHolder(View view) {
            super(view);
            this.view = view;
            recipeImg = view.findViewById(R.id.recipe_img);
            recipeName = view.findViewById(R.id.recipe_name);
            recipeMin = view.findViewById(R.id.recipe_min);
            recipeTags = view.findViewById(R.id.recipe_tags);
            memProfile = view.findViewById(R.id.mem_profile);
            memName = view.findViewById(R.id.mem_name);
            recipeFav = view.findViewById(R.id.recipe_fav);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeIntro.size();
    }


}