package com.sixsense.tangerine.main.home.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sixsense.tangerine.R;

import java.util.ArrayList;

public class RecipeGridAdapter extends BaseAdapter {
    private Context context;
    //    private final String[] city_names;
    private ArrayList<CityList.CityVO> city_info;
    private ImageView imageView;
    private TextView textView;
    public RecipeGridAdapter(Context context, ArrayList<CityList.CityVO> city_info) {
        this.context = context;
        this.city_info = city_info;
    }

//    public void getCityNames(){

    //비동기
//        httpInterface = HttpClient.getClient().create(HttpInterface.class);
//        Call<CityList> call = httpInterface.getCitynames();
//        call.enqueue(new Callback<CityList>() {
//            @Override
//            public void onResponse(Call<CityList> call, Response<CityList> response) {
//                Log.d("TAG",response.code()+"");
//                String displayResponse = "";
//
//                CityList resource = response.body();
//                List<CityList.CityVO> cityVOList = resource.data;
//                for (CityList.CityVO cityVO: cityVOList) {
//                    city_names[0]=cityVO.city_names;
//                    System.out.println(cityVO.city_names);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CityList> call, Throwable t) {
//
//            }
//        });
//        new AsyncCall().execute();
//
//    }

    @Override
    public int getCount() {
        return city_info.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View grid;
        if (convertView == null) {

            grid = new View(context);
            if (inflater != null) {
                grid = inflater.inflate(R.layout.home_recipe_item, null);
            }
            textView = grid.findViewById(R.id.city_name);
            textView.setText(city_info.get(position).city_name);
            imageView = grid.findViewById(R.id.city_img);
            Glide.with(grid.getContext())
                    .load(city_info.get(position).city_img)
                    .into(imageView);
        } else {
            grid = convertView;
        }

        return grid;
    }

}