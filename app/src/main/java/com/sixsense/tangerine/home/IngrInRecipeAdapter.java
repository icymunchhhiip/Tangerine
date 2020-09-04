package com.sixsense.tangerine.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.InRecipe;

import java.util.List;

public class IngrInRecipeAdapter extends ArrayAdapter {
    Context context;
    List<InRecipe.IngrInfo> resource;

    public IngrInRecipeAdapter(@NonNull Context context, List<InRecipe.IngrInfo> resource) {
        super(context, R.layout.home_ingrlist, resource);
        this.context=context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        if(convertView==null)
            row = LayoutInflater.from(getContext()).inflate(R.layout.home_ingrlist, parent, false);

        TextView ingr = row.findViewById(R.id.ingrTV);
        TextView cnt = row.findViewById(R.id.numTV);
        TextView kcal = row.findViewById(R.id.kcalTV);

        ingr.setText(resource.get(position).ingrName);
        cnt.setText(resource.get(position).ingrCnt);
        kcal.setText(resource.get(position).ingrKcal);
        return row;
    }
}
