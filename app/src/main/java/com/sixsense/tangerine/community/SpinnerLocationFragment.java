package com.sixsense.tangerine.community;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.sixsense.tangerine.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class SpinnerLocationFragment extends Fragment implements OnMapReadyCallback {
    // MarketSetLocationActivity의 Spinner에서 받아온 주소를 Geocode해서 맵에 표시
    private static final String TAG = "SpinnerLocationFragment";

    private FusedLocationSource locationSource;
    private NaverMap mNaverMap;
    private MapView mapView;
    private TextView textView;
    private String addr;
    private double lat, lon;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof SetLocationActivity) {
            addr = ((SetLocationActivity) getActivity()).getSpinnerLocation();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.community_fragment_marklocation, container, false);

        mapView = rootView.findViewById(R.id.mapView_mark);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, AppConstants.LOCATION_PERMISSION_REQUEST_CODE);

        textView = rootView.findViewById(R.id.textView_marklocation);

        return rootView;
    }

    public String[] jsonParser(String jsonString){
        String jibunAddress = null;
        String x = null;
        String y = null;

        String[] arraysum = new String[3];

        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            String addresses = jsonObject.getString("addresses");
            JSONArray jsonArray = new JSONArray(addresses);

            for(int i=0;i<jsonArray.length();i++) {
                JSONObject subJsonObject = jsonArray.getJSONObject(i);
                jibunAddress = subJsonObject.getString("jibunAddress");
                x = subJsonObject.getString("x");
                y = subJsonObject.getString("y");
            }
            arraysum[0] = jibunAddress;
            arraysum[1] = x;
            arraysum[2] = y;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arraysum;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.mNaverMap = naverMap;
        naverMap.setLocationSource(locationSource);

        try{
            String[] resultText = jsonParser(new GeocodeApiTask(addr).execute().get());
            if(resultText.length != 0) {
                lon = Double.parseDouble(resultText[1]);
                lat = Double.parseDouble(resultText[2]);
                String str = resultText[0] + " " + resultText[1] + " " + resultText[2];
                textView.setText(resultText[0]);
                Log.d(TAG, "json results: " + str);
            }
        }catch(InterruptedException e){
            Log.e(TAG,"Naver Map error" + e.getMessage());
            e.printStackTrace();
        }catch(ExecutionException e){
            e.printStackTrace();
            Log.e(TAG,"Naver Map error" + e.getMessage());
        }

        CameraPosition cameraPosition =
                new CameraPosition(new LatLng(lat, lon), 14);
        naverMap.setCameraPosition(cameraPosition);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        uiSettings.setCompassEnabled(false);

        Marker marker = new Marker();
        marker.setPosition(new LatLng(lat, lon));
        marker.setMap(naverMap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                mNaverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }
}
