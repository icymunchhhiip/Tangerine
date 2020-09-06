package com.sixsense.tangerine.home.write;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sixsense.tangerine.R;

import java.util.List;

public class SearchAdapter extends BaseAdapter {
    private Context context;
    private List<ingr_list_item> list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder = new ViewHolder();

    public SearchAdapter(Context context, List<ingr_list_item> list) {
        this.context = context;
        this.list = list;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflate.inflate(R.layout.home_write_search_row_ingr_list,null);

            viewHolder = new ViewHolder();
            viewHolder.ingr =  convertView.findViewById(R.id.ingr_name);
            viewHolder.kcal =  convertView.findViewById(R.id.ingr_kcal);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.ingr.setText(list.get(position).getIngr_list_name());
        viewHolder.kcal.setText(list.get(position).getIngt_list_kcal());

        return convertView;
    }
    static class ViewHolder{
        public TextView ingr;
        public TextView kcal;

    }

}
