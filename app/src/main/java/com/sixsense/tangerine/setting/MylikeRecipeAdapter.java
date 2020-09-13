package com.sixsense.tangerine.setting;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;

import java.util.ArrayList;

public class MylikeRecipeAdapter extends RecyclerView.Adapter<MylikeRecipeAdapter.ViewHolder> {

    private ArrayList<Recipe> items = new ArrayList<Recipe>();
    private static Uri selectedImageUri;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {

        void onDeleteClick(int position);
    }

    public MylikeRecipeAdapter(ArrayList<Recipe> likeList) {
        items = likeList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.setting_like_recipe_item, viewGroup, false);

        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MylikeRecipeAdapter.ViewHolder viewHolder, int position) {
        Recipe item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
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


    //1.
    static class ViewHolder extends RecyclerView.ViewHolder {
        static ImageView pictureImageView;
        TextView titleTextView;
        TextView timeTextView;
        TextView tagTextView;
        TextView idTextView;
        ImageView heartImageView;

        public ViewHolder(View itemView,final OnItemClickListener listener) {
            super(itemView);

            pictureImageView = itemView.findViewById(R.id.pictureImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            tagTextView = itemView.findViewById(R.id.tagTextView);
            idTextView = itemView.findViewById(R.id.idTextView);
            heartImageView = itemView.findViewById(R.id.heartImageView);

            heartImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }

        public void setItem(Recipe item) {
            //MylikeRecipeActivity.setpictureImageView();

            //pictureImageView.setImageURI();
            titleTextView.setText(item.getTitle());
            timeTextView.setText(item.getTime());
            tagTextView.setText(item.getTag());
            idTextView.setText(item.getUserid());

        }


    }



}


