package com.sixsense.tangerine.home;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class SliderFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout mSliderLayout;
    private List<MainEventList.MainEvent> mainEventList;
    private static final String TAG = SliderFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_slider, container, false);

        mSliderLayout = view.findViewById(R.id.slider);

        return view;
    }

    @Override
    public void onResume() {
        new MainEventCall().execute();

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
        mSliderLayout.setDuration(3000);
        mSliderLayout.addOnPageChangeListener(this);
        super.onResume();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @SuppressLint("StaticFieldLeak")
    private class MainEventCall extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpInterface httpInterface = HttpClient.getClient().create(HttpInterface.class);
            Call<MainEventList> call = httpInterface.getMainEvent();
            try {
                mainEventList = call.execute().body().data;
            } catch (Exception e) {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for (MainEventList.MainEvent url : mainEventList) {
                DefaultSliderView defaultSliderView = new DefaultSliderView(getContext());

                String BaseUrl = HttpClient.BASE_URL + "main-event/imgs/";
                defaultSliderView
                        .image(BaseUrl + url.img)
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                mSliderLayout.addSlider(defaultSliderView);
            }
        }

    }

    @Override
    public void onStop() {
        mSliderLayout.stopAutoCycle();
        super.onStop();
    }

}