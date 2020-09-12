package com.sixsense.tangerine.home.write;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.HttpClient;

import java.nio.channels.WritePendingException;
import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class RecipeDescAdapter extends RecyclerView.Adapter<RecipeDescAdapter.ViewHolder> {
    ArrayList<RecipeDescItem> arrayList;
    Context context;
    private OnItemClickListener onItemClickListener;
    private String recipeImageURL = HttpClient.BASE_URL + "recipe/imgs/";
    View v;

    public interface OnItemClickListener {
        void onDeleteClick(int position);

        void onImageClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public RecipeDescAdapter(Context context, ArrayList<RecipeDescItem> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_write_recipedesc, parent, false);
        ViewHolder evh = new ViewHolder(v, onItemClickListener);
        return evh;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String pos = String.valueOf(arrayList.get(position).descbtnum);
        arrayList.get(position).setBtnum(position);
        if (arrayList.get(position).food_image_addr != null && !arrayList.get(position).food_image_addr.equals("")) {
            Glide.with(v.getContext())
                    .load(recipeImageURL + arrayList.get(position).food_image_addr)
                    .into(holder.food_image);
        }

        holder.recipe_desc_num.setText(pos);
        holder.recipe_desc.setText(arrayList.get(position).recipe_desc);
        holder.recipe_desc_detail.setText(arrayList.get(position).recipe_desc_detail);
        holder.recipe_desc_num.setText(String.valueOf(position + 1));
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

    public ArrayList<RecipeDescItem> getArrayList() {
        return arrayList;
    }

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
        NumberPicker hourNP, minNP, secNP;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View timerLayout;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        public ViewHolder(View itemview, final OnItemClickListener listener) {
            super(itemview);
            recipe_desc_detail = itemview.findViewById(R.id.rdesc_detail);
            recipe_desc = itemview.findViewById(R.id.rdesc);
            food_image = itemview.findViewById(R.id.food_image);
            recipe_desc_num = itemview.findViewById(R.id.recipe_desc_num);
            delete_desc_iv = itemview.findViewById(R.id.delete_desc_iv);
            stopwatch_bt = itemview.findViewById(R.id.stopwatch_bt);

            timerLayout = inflater.inflate(R.layout.home_write_setting_timer_popup, (ViewGroup) itemview.findViewById(R.id.setting_timer_popup_id));

            stopwatch_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        final int position = getAdapterPosition();
                        int h, m, s;

                        if (position != RecyclerView.NO_POSITION) {
                            alertDialog.setTitle("시간 등록");
                            if (timerLayout.getParent() != null) {
                                ((ViewGroup) timerLayout.getParent()).removeView(timerLayout);
                            }
                            hourNP = (NumberPicker) timerLayout.findViewById(R.id.hourNP);
                            minNP = (NumberPicker) timerLayout.findViewById(R.id.minNP);
                            secNP = (NumberPicker) timerLayout.findViewById(R.id.secNP);

                            String sbt = stopwatch_bt.getText().toString();

                            hourNP.setMinValue(0);
                            hourNP.setMaxValue(59);
                            hourNP.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                            minNP.setMinValue(0);
                            minNP.setMaxValue(59);
                            minNP.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                            secNP.setMinValue(0);
                            secNP.setMaxValue(59);
                            secNP.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
//                            String []num=new String[60];
//                            for(int i=0;i<=60;i++){
//                                num[i] = String.valueOf(i);
//                            }
//                            hourNP.setDisplayedValues(num);
//                            minNP.setDisplayedValues(num);
//                            secNP.setDisplayedValues(num);

                            if (sbt.compareTo("") == 0 || sbt == null && sbt.compareTo("00:00:00") == 0) {
                                h = 0;
                                m = 0;
                                s = 0;
                            } else {
                                h = Integer.parseInt(sbt.substring(0, 2));
                                m = Integer.parseInt(sbt.substring(3, 5));
                                s = Integer.parseInt(sbt.substring(6, 8));
                            }
                            hourNP.setValue(h);
                            minNP.setValue(m);
                            secNP.setValue(s);

                            hourNP.setWrapSelectorWheel(true);
                            minNP.setWrapSelectorWheel(true);
                            secNP.setWrapSelectorWheel(true);

                            alertDialog.setView(timerLayout);


                            alertDialog.setNegativeButton("등록", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int h, m, s;
                                    String sh, sm, ss;
                                    h = hourNP.getValue();
                                    m = minNP.getValue();
                                    s = secNP.getValue();
                                    if (h < 10) {
                                        sh = "0" + h;
                                    } else sh = String.valueOf(h);
                                    if (m < 10) {
                                        sm = "0" + m;
                                    } else sm = String.valueOf(m);
                                    if (s < 10) {
                                        ss = "0" + s;
                                    } else ss = String.valueOf(s);


                                    String settime = sh + ":" + sm + ":" + ss;
                                    RecipeDescItem recipeDescItem = arrayList.get(position);
                                    recipeDescItem.cookingtime = settime;
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

            food_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onImageClick(position);
//                             food_image.setImageBitmap(arrayList.get(position).bitmap);
                        }
                    }
                }
            });

        }
    }


}
