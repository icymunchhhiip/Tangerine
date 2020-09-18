package com.sixsense.tangerine.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.CookingTip;
import com.sixsense.tangerine.network.HttpClient;

import java.util.ArrayList;

public class CookingTipAdapter extends RecyclerView.Adapter<CookingTipAdapter.ViewHolder> implements OnCookingTipListItemClickListener{
    private ArrayList<CookingTip> listData = new ArrayList<>();


    private OnCookingTipListItemClickListener listener;
    public void onItemClick(ViewHolder viewHolder, View view, int position) {
        if(listener != null) {
            listener.onItemClick(viewHolder, view, position);
        }
    }

    public void setOnItemClickListener(OnCookingTipListItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.community_item_cookingtip, parent, false);

        return new ViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItems(ArrayList<CookingTip> cookingTips) {
        listData.addAll(cookingTips);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;


        ViewHolder(View itemView, final OnCookingTipListItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        void onBind(CookingTip cookingTip) {
            String url = HttpClient.BASE_URL + "community/tips/";
            Glide.with(itemView.getContext())
                    .load(url + cookingTip.getFname())
                    .into(imageView);
        }
    }
    public CookingTip getItem(int position) {
        return listData.get(position);
    }
}
