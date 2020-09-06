package com.sixsense.tangerine.home.write;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sixsense.tangerine.R;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class RecipeDescAdapter extends RecyclerView.Adapter<RecipeDescAdapter.ViewHolder>{
    ArrayList<RecipeDescItem> arrayList;
    Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public RecipeDescAdapter(Context context, ArrayList<RecipeDescItem>arrayList){
        this.arrayList=arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_write_recipedesc, parent, false);
        ViewHolder evh = new ViewHolder(v, onItemClickListener);
        return evh;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String pos = String.valueOf(arrayList.get(position).descbtnum);
        arrayList.get(position).setBtnum(position);

        holder.recipe_desc_num.setText(pos);
        holder.recipe_desc.setText(arrayList.get(position).recipe_desc);
        holder.recipe_desc_detail.setText(arrayList.get(position).recipe_desc_detail);
        holder.recipe_desc_num.setText(String.valueOf(position+1));
        holder.stopwatch_bt.setText(arrayList.get(position).cookingtime);
        final RecipeDescItem recipeDescItem = arrayList.get(position);

        holder.recipe_desc.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                recipeDescItem.recipe_desc = holder.recipe_desc.getText().toString();
                arrayList.set(position, recipeDescItem);
            }
        });
        holder.recipe_desc_detail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                recipeDescItem.recipe_desc_detail = holder.recipe_desc_detail.getText().toString();
                arrayList.set(position, recipeDescItem);
            }
        });
    }

    public ArrayList<RecipeDescItem> getArrayList(){return arrayList;}
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText recipe_desc, recipe_desc_detail, recipe_addr;
        ImageView food_image, delete_desc_iv;
        TextView recipe_desc_num;
        Button stopwatch_bt;
        EditText set_hour_tv, set_minute_tv, set_second_tv;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View timerLayout;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        public ViewHolder(View itemview, final OnItemClickListener listener) {
                super(itemview);
                recipe_desc_detail=itemview.findViewById(R.id.rdesc_detail);
                recipe_desc = itemview.findViewById(R.id.rdesc);
                food_image = itemview.findViewById(R.id.food_image);
                recipe_desc_num=itemview.findViewById(R.id.recipe_desc_num);
                delete_desc_iv = itemview.findViewById(R.id.delete_desc_iv);
                stopwatch_bt = itemview.findViewById(R.id.stopwatch_bt);

                timerLayout = inflater.inflate(R.layout.home_write_setting_timer_popup, (ViewGroup)itemview.findViewById(R.id.setting_timer_popup_id));

                stopwatch_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            final int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {

                                alertDialog.setTitle("시간 등록");
                                if (timerLayout.getParent() != null)
                                    ((ViewGroup) timerLayout.getParent()).removeView(timerLayout);
                                alertDialog.setView(timerLayout);
                                alertDialog.setNegativeButton("등록", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String h, m,s, zero;
                                        zero = "00";

                                        set_hour_tv = timerLayout.findViewById(R.id.set_hour_tv);
                                        set_minute_tv = timerLayout.findViewById(R.id.set_minute_tv);
                                        set_second_tv = timerLayout.findViewById(R.id.set_second_tv);

                                        h = String.valueOf(set_hour_tv.getText());
                                        m = String.valueOf(set_minute_tv.getText());
                                        s = String.valueOf(set_second_tv.getText());
                                        if(h.compareTo("")==0)h =zero;
                                        else if(Integer.parseInt(h)<10)h="0"+ h;
                                        if(m.compareTo("")==0) m = zero;
                                        else if(Integer.parseInt(m)<10)m="0"+m;
                                        if(s.compareTo("")==0)s = zero;
                                        else if(Integer.parseInt(s)<10)s="0"+s;

                                        String settime = h+":"+m+":"+s;
                                        RecipeDescItem recipeDescItem = arrayList.get(position);
                                        recipeDescItem.cookingtime=settime;
                                        arrayList.set(position, recipeDescItem);
                                        stopwatch_bt.setText(settime);
                                    }
                                });
                                AlertDialog newAD = alertDialog.create();
                                newAD.show();
                            }
                        }
                    }
                });

                delete_desc_iv.setOnClickListener(new View.OnClickListener() {
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
