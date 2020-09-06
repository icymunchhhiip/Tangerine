package com.sixsense.tangerine.home.write;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;

import java.util.ArrayList;

public class AddedListAdapter extends RecyclerView.Adapter<AddedListAdapter.ViewHolder> {
    private ArrayList<ingr_list_item> arrayList;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public AddedListAdapter(Context context, ArrayList<ingr_list_item> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_write_ingraddedlist, parent, false);
        ViewHolder evh = new ViewHolder(v, onItemClickListener);
        return evh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ingr.setText(arrayList.get(position).ingr_list_name);
        holder.num.setText(arrayList.get(position).ingr_list_num);
        holder.kcal.setText(arrayList.get(position).ingr_list_kcal);
        holder.unit.setText(arrayList.get(position).ingr_list_unit);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingr, num, kcal, unit;
        ImageView delete_ingr_list;
        ViewHolder(final View itemView,  final AddedListAdapter.OnItemClickListener listener){
            super(itemView);
            delete_ingr_list = itemView.findViewById(R.id.delete_ingr_list);
            ingr = itemView.findViewById(R.id.ingr_list_name);
            num = itemView.findViewById(R.id.ingr_list_num);
            kcal = itemView.findViewById(R.id.ingr_list_kcal);
            unit = itemView.findViewById(R.id.ingr_list_unit);

            delete_ingr_list.setOnClickListener(new View.OnClickListener() {
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
    }
}
