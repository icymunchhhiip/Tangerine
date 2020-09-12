package com.sixsense.tangerine.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.InRecipe;

import java.util.List;

public class IngrInRecipeAdapter extends RecyclerView.Adapter<IngrInRecipeAdapter.IngrViewHolder> {
    private List<InRecipe.IngrInfo> mIngrInfoList;

    public IngrInRecipeAdapter(List<InRecipe.IngrInfo> ingrInfoList) {
        this.mIngrInfoList = ingrInfoList;
    }

    @NonNull
    @Override
    public IngrInRecipeAdapter.IngrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_ingrlist, parent, false);

        return new IngrInRecipeAdapter.IngrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngrInRecipeAdapter.IngrViewHolder holder, final int position) {
        holder.mTextIngrName.setText(mIngrInfoList.get(position).ingrName);
        String cnt = mIngrInfoList.get(position).ingrCount + mIngrInfoList.get(position).ingrUnit;
        holder.mTextCnt.setText(cnt);
        holder.mTextKcal.setText(mIngrInfoList.get(position).ingrKcal);

    }

    public class IngrViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextIngrName;
        private TextView mTextCnt;
        private TextView mTextKcal;

        public IngrViewHolder(View view) {
            super(view);
            mTextIngrName = view.findViewById(R.id.ingrTV);
            mTextCnt = view.findViewById(R.id.numTV);
            mTextKcal = view.findViewById(R.id.kcalTV);

        }
    }

    @Override
    public int getItemCount() {
        return mIngrInfoList.size();
    }
}
