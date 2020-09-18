package com.sixsense.tangerine.community;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.OnTaskCompletedListener;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.MarketPost;
import com.sixsense.tangerine.community.item.Member;

import java.util.ArrayList;
import java.util.Date;

public class MarketPostAdapter extends RecyclerView.Adapter<MarketPostAdapter.ViewHolder> implements OnMarketPostItemClickListener {
    private static final String TAG = "MarketPostAdapter";

    private ArrayList<MarketPost> items = new ArrayList<MarketPost>();
    private OnMarketPostItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.community_item_marketpost, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MarketPost item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() { // 어댑터에서 관리하는 아이템의 개수를 반환, 리싸이클러뷰에 어댑터가 관리하는 아이템의 개수를 알아야 할 때 사용된다.
        return items.size();
    }

    public void setOnItemClickListener(OnMarketPostItemClickListener listener) {
        this.listener = listener;
    }

    public void onItemClick(ViewHolder viewHolder, View view, int position) {
        if(listener != null) {
            listener.onItemClick(viewHolder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements OnTaskCompletedListener {
        TextView mkTextViewTitle;
        TextView mkTextViewPrice;
        ImageView mkImageViewProfile;
        TextView mkTextViewDate;
        ImageView mkImageViewThumbnail;
        TextView mkTextViewNickname;

        public ViewHolder(View itemView, final OnMarketPostItemClickListener listener) {
            super(itemView);

            mkTextViewTitle = itemView.findViewById(R.id.textView_title);
            mkTextViewPrice = itemView.findViewById(R.id.textView_price);
            mkImageViewProfile = itemView.findViewById(R.id.imageView_profile);
            mkTextViewDate = itemView.findViewById(R.id.textView_date);
            mkImageViewThumbnail = itemView.findViewById(R.id.imageView_thumbnail);
            mkTextViewNickname = itemView.findViewById(R.id.textView_nickname);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int position = getAdapterPosition();
                    if (listener != null)
                        listener.onItemClick(ViewHolder.this, v, position);
                }
            });
        }

        public void setItem(MarketPost item) {
            mkTextViewTitle.setText(item.getTitle());
            mkTextViewPrice.setText(Integer.toString(item.getPrice()) + "원");

            try{
                Date inDate = AppConstants.dateFormat2.parse(item.getDate());
                mkTextViewDate.setText(AppConstants.dateFormat.format(inDate));
            }catch(Exception e){
                Log.e(TAG,"date format error");
            }

            Member writer = item.getWriter();
            mkTextViewNickname.setText(writer.getNickname());

            String profilePath = writer.getProfilePath();
            if(!TextUtils.isEmpty(profilePath))
                new DownloadFilesTask(this,mkImageViewProfile, AppConstants.ABSOLUTE_PATH).execute(profilePath);

            String imgpath = item.getImgPath();
            if(!TextUtils.isEmpty(imgpath))
                new DownloadFilesTask(this,mkImageViewThumbnail, AppConstants.RELATIVE_PATH).execute(imgpath);
        }

        @Override
        public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public boolean jsonToItem(String jsonString) {
            return false;
        }

        @Override
        public void noResultNotice(String searchString) {
        }
    }

    public void addItem(MarketPost item) {
        items.add(item);
    }

    public void setItems(ArrayList<MarketPost> items) {
        this.items = items;
    }

    public MarketPost getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, MarketPost item) {
        items.set(position, item);
    }
}