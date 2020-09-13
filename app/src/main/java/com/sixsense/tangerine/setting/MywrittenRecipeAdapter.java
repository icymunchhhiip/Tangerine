package com.sixsense.tangerine.setting;

import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;

import java.util.ArrayList;


public class MywrittenRecipeAdapter extends RecyclerView.Adapter<MywrittenRecipeAdapter.ViewHolder> {
    //2.
    ArrayList<Recipe> items = new ArrayList<Recipe>();
    ArrayList<Tag> tagitems = new ArrayList<Tag>();

    public MywrittenRecipeAdapter(ArrayList<Recipe> myrecipeList) {
        items = myrecipeList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.setting_recipe_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Recipe item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //1.
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView timeTextView;
        TextView tagTextView;
        TextView idTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            tagTextView = itemView.findViewById(R.id.tagTextView);
            idTextView = itemView.findViewById(R.id.idTextView);
        }

        public void setItem(Recipe item) {
            titleTextView.setText(item.getTitle());
            timeTextView.setText(item.getTime());
            tagTextView.setText(item.getTag());
            idTextView.setText(item.getUserid());
        }
    }

    public void addItem(Recipe item) {
        items.add(item);
    }

    public void setItems(ArrayList<Recipe> items) {
        this.items = items;
    }

    public Recipe getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Recipe item) {
        items.set(position, item);
    }
}
