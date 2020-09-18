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
import com.sixsense.tangerine.community.item.Comment;
import com.sixsense.tangerine.community.item.Member;

import java.util.ArrayList;
import java.util.Date;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private static final String TAG = "CommentAdapter";

    private ArrayList<Comment> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.community_item_comment, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Comment item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() { // 어댑터에서 관리하는 아이템의 개수를 반환, 리싸이클러뷰에 어댑터가 관리하는 아이템의 개수를 알아야 할 때 사용된다.
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements OnTaskCompletedListener {
        ImageView cImageViewProfile;
        TextView cTextViewNickname;
        TextView cTextViewText;
        TextView cTextViewDate;


        public ViewHolder(View itemView) {
            super(itemView);

            cImageViewProfile = itemView.findViewById(R.id.imageView_profile);
            cTextViewNickname = itemView.findViewById(R.id.textView_nickname);
            cTextViewText = itemView.findViewById(R.id.textView_text);
            cTextViewDate = itemView.findViewById(R.id.textView_date);
        }

        public void setItem(Comment item) {
            Member writer = item.getWriter();
            cTextViewNickname.setText(writer.getNickname());
            cTextViewText.setText(item.getDescription());
            try{
                Date inDate = AppConstants.dateFormat2.parse(item.getDate());
                cTextViewDate.setText(AppConstants.dateFormat.format(inDate));
            }catch(Exception e){
                Log.e(TAG,"date format error");
            }

            String profilePath = writer.getProfilePath();
            if(!TextUtils.isEmpty(profilePath))
                new DownloadFilesTask(this,cImageViewProfile,AppConstants.ABSOLUTE_PATH).execute(profilePath);
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

    public void setItems(ArrayList<Comment> items) {
        this.items = items;
    }
}