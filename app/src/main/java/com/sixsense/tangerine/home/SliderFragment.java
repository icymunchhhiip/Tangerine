package com.sixsense.tangerine.home;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.sixsense.tangerine.R;
import com.sixsense.tangerine.network.HttpClient;
import com.sixsense.tangerine.network.HttpInterface;
import com.sixsense.tangerine.network.MainEventList;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class SliderFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private SliderLayout mSliderLayout;
    private List<MainEventList.MainEvent> mainEventList;
    private String mainEventImageURL = HttpClient.BASE_URL+"main-event/imgs/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_slider, container, false);

        mSliderLayout = view.findViewById(R.id.slider);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        new MainEventCall().execute();

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
        mSliderLayout.setDuration(3000);
        mSliderLayout.addOnPageChangeListener(this);

//        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("StaticFieldLeak")
    private class MainEventCall extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            Call<MainEventList> call = httpInterface.getMainEvent();
            MainEventList resource = null;
            try {
                resource = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert resource != null;
            mainEventList = resource.data;

            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for(MainEventList.MainEvent url : mainEventList){
                DefaultSliderView defaultSliderView = new DefaultSliderView(getContext());
                // initialize a SliderLayout
                defaultSliderView
                        .image(mainEventImageURL + url.img)
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                mSliderLayout.addSlider(defaultSliderView);
            }
        }

    }


    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mSliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
//        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

}
