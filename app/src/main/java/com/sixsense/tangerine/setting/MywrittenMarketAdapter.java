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

public class MywrittenMarketAdapter extends RecyclerView.Adapter<MywrittenMarketAdapter.ViewHolder> {
    ArrayList<Mymarket> items = new ArrayList<Mymarket>();


    public MywrittenMarketAdapter(ArrayList<Mymarket> mymarketList) {
        items = mymarketList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.setting_market_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Mymarket item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Mymarket item) {
        items.add(item);
    }

    public void setItems(ArrayList<Mymarket> items) {
        this.items = items;
    }

    public Mymarket getItem(int position) {
        return items.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView priceTextView;
        TextView dateTextView;
        TextView myNameTextView;
        ImageView profileImage;
        ImageView pictureImage;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.title);
            priceTextView = itemView.findViewById(R.id.price);
            dateTextView = itemView.findViewById(R.id.date);
            myNameTextView = itemView.findViewById(R.id.myname);
            profileImage = itemView.findViewById(R.id.profile);
            pictureImage = itemView.findViewById(R.id.picture);


        }

        public void setItem(Mymarket item) {

            String picturePath = item.getPicture();
            String profilePath = item.getProfile();

            if (picturePath != null && !picturePath.equals("")) {
                pictureImage.setVisibility(View.VISIBLE);
                pictureImage.setImageURI(Uri.parse("file://" + picturePath));

            } else {
                pictureImage.setVisibility(View.GONE);
                pictureImage.setImageResource(R.drawable.noimagefound);

            }

            if (profilePath != null && !profilePath.equals("")) {
                profileImage.setVisibility(View.VISIBLE);
                profileImage.setImageURI(Uri.parse("file://" + profilePath));
            } else {
                profileImage.setVisibility(View.GONE);
                profileImage.setImageResource(R.drawable.ic_default_profile);
            }

            titleTextView.setText(item.getTitle());
            priceTextView.setText(item.getPrice());
            dateTextView.setText(item.getDate());
            myNameTextView.setText(item.getName());
            pictureImage.setImageResource(R.drawable.noimagefound);
            profileImage.setImageResource(R.drawable.ic_default_profile);


        }


    }

}