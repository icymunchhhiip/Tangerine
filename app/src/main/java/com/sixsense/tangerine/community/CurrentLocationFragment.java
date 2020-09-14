package com.sixsense.tangerine.community;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;
import com.sixsense.tangerine.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/* 장터게시판 위치설정-> 현재 위치 설정 + 지도 */
public class CurrentLocationFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "CurrentLocationFragment";

    private MyLocationListener myLocationListener;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private MapView mapView;
    private TextView textView;
    private double lat, lon;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(getActivity() != null && getActivity() instanceof MyLocationListener) {
            myLocationListener = (MyLocationListener) getActivity();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.community_fragment_currentlocation, container, false);

        mapView = rootView.findViewById(R.id.mapView_current);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, AppConstants.LOCATION_PERMISSION_REQUEST_CODE);

        textView = rootView.findViewById(R.id.textView_currentlocation);

        return rootView;
    }

    public String[] jsonParser(String jsonString){
        String sido = null; //시도 0
        String sigugun = null; //시군 1
        String dongmyun = null; //동면 // 2

        String[] arraysum = new String[3];

        try{
            JSONArray jarray = new JSONObject(jsonString).getJSONArray("results");
            JSONObject resultsObject = jarray.getJSONObject(0);
            JSONObject regionObject = resultsObject.getJSONObject("region");

            for(int i = 1; i < 4; i++){
                JSONObject area = regionObject.getJSONObject("area"+i);

                switch(i){
                    case 1:
                        sido = area.optString("name");
                        break;
                    case 2:
                        sigugun = area.optString("name");
                        break;
                    case 3:
                        dongmyun = area.optString("name");
                        break;
                }
            }
            arraysum[0] = sido;
            arraysum[1] = sigugun;
            arraysum[2] = dongmyun;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arraysum;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        uiSettings.setCompassEnabled(false);

        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                // 위치가 변경되면 다음의 코드들이 수행된다.
                lat = location.getLatitude();
                lon = location.getLongitude();

                try{
                    String coords = lon + "," + lat;
                    String[] resultText = jsonParser(new ReverseGeocodeApiTask(coords).execute().get()); //한글주소받아오기
                    String localString = resultText[0]+" "+resultText[1]+" "+resultText[2];
                    String localCode = new SgisGeocodeApiTask(localString).execute().get(); // 지역코드 받아오기
                    textView.setText(localString);
                    if(myLocationListener != null) {
                        myLocationListener.onReceivedCurrentLocation(localString, localCode); // 주소, 지역코드 SetLocation으로 전달
                    }
                }catch(InterruptedException e){
                    Log.e(TAG,"ERROR Naver map: " + e.getMessage());
                }catch(ExecutionException e){
                    Log.e(TAG,"ERROR Naver map: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }
}