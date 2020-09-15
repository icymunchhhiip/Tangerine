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

import com.sixsense.tangerine.R;
import com.sixsense.tangerine.community.item.BoardPost;
import com.sixsense.tangerine.community.item.Member;

import java.util.ArrayList;
import java.util.Date;

public class BoardPostAdapter extends RecyclerView.Adapter<BoardPostAdapter.ViewHolder>  implements OnBoardPostItemClickListener {
    private static final String TAG = "BoardPostAdapter";

    private ArrayList<BoardPost> items = new ArrayList<BoardPost>();
    private OnBoardPostItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.community_item_boardpost, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        BoardPost item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() { // 어댑터에서 관리하는 아이템의 개수를 반환, 리싸이클러뷰에 어댑터가 관리하는 아이템의 개수를 알아야 할 때 사용된다.
        return items.size();
    }

    public void setOnItemClickListener(OnBoardPostItemClickListener listener) {
        this.listener = listener;
    }

    public void onItemClick(ViewHolder viewHolder, View view, int position) {
        if(listener != null) {
            listener.onItemClick(viewHolder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements OnTaskCompletedListener {
        TextView bTextViewCategory;
        TextView bTextViewTitle;
        TextView bTextViewLikes;
        TextView bTextViewNickname;
        TextView bTextViewDate;
        ImageView bImageViewHeart;
        ImageView bImageViewProfile;

        public ViewHolder(View itemView, final OnBoardPostItemClickListener listener) {
            super(itemView);

            bTextViewCategory = itemView.findViewById(R.id.textView_category);
            bTextViewTitle = itemView.findViewById(R.id.textView_title);
            bTextViewLikes = itemView.findViewById(R.id.textView_likes);
            bTextViewNickname = itemView.findViewById(R.id.textView_nickname);
            bTextViewDate = itemView.findViewById(R.id.textView_date);
            bImageViewProfile = itemView.findViewById(R.id.imageView_profile);
            bImageViewHeart = itemView.findViewById(R.id.imageView_heart);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int position = getAdapterPosition();
                    if (listener != null)
                    {
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(BoardPost item) {
            switch(item.getCategory()){
                case 1:
                    bTextViewCategory.setText("[잡담]");
                    break;
                case 2:
                    bTextViewCategory.setText("[꿀팁]");
                    break;
                case 3:
                    bTextViewCategory.setText("[질문]");
                    break;
                default:
                    bTextViewCategory.setText("[오류]");
            }
            bTextViewTitle.setText(item.getTitle());
            bTextViewLikes.setText(Integer.toString(item.getLikes()));
            try{
                Date inDate = AppConstants.dateFormat2.parse(item.getDate());
                bTextViewDate.setText(AppConstants.dateFormat.format(inDate));
            }catch(Exception e){
                Log.e(TAG,"date format error");
            }
            if(item.isMyLike())
                bImageViewHeart.setImageResource(R.drawable.ic_favorite_selected_24dp);

            Member writer = item.getWriter();
            bTextViewNickname.setText(writer.getNickname());

            String profilePath = writer.getProfilePath();
            if(!TextUtils.isEmpty(profilePath))
                new DownloadFilesTask(this,bImageViewProfile, AppConstants.ABSOLUTE_PATH).execute(profilePath);
        }

        @Override
        public void onDownloadImgSet(ImageView imageView, Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public void jsonToItem(String jsonString) {
        }

        @Override
        public void noResultNotice(String searchString) {
        }
    }

    public void setItems(ArrayList<BoardPost> items) {
        this.items = items;
    }

    public BoardPost getItem(int position) {
        return items.get(position);
    }
}