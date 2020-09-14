package com.sixsense.tangerine.community;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sixsense.tangerine.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/* 장터게시판 위치설정 */
public class SetLocationActivity extends AppCompatActivity implements MyLocationListener {
    private static final String TAG = "SetLocationActivity";
    private HintAdapter sidoAdapter, sigunguAdapter, dongmyunAdapter;
    private String[] sidoList = new String[1];
    private String[] sigunguList = new String[1];
    private String[] dongmyunList = new String[1];
    private int checkSpinner; // 시도/시군구/동면읍까지 다 선택할 시 -> 3 (3이 아니면 `검색` 버튼 누르면 넘어가지 않음)

    private CurrentLocationFragment currentLocationFragment;
    private SpinnerLocationFragment spinnerLocationFragment;

    private String[] sidoCode = new String[1]; // 시도 지역코드 (ex. 서울특별시 -> 11)
    private String[] sigunguCode = new String[1]; // 시도+시군구 지역코드 (ex. 서울특별시 강남구 -> 11230)
    private String[][] dongmyunInfo = new String[1][]; // [][0] : 시도+시군구+동면읍 지역코드 (ex. 서울특별시 강남구 개포1동 -> 1123068) [][1] : 전체 주소

    private String currentLocation; // 현재 위치
    private String currentCode; //현재 위치 지역코드
    private String spinnerLocation; // Spinner 위치
    private String spinnerCode; // Spinner 위치의 지역코드

    private static int mode; // 현재 보여지는 Fragment (1:CurrentLocation, 2:SpinnerLocation)
    private static int searchCnt = 0; // 검색 버튼이 눌린 횟수(재검색 확인용)

    private String[][] resultText; // 파싱된 text


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity_setlocation);

        currentLocationFragment = new CurrentLocationFragment();
        spinnerLocationFragment = new SpinnerLocationFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.locationContainer, currentLocationFragment).commit(); //현재위치 맵띄우기
        mode = 1;

        final Spinner sidoSpinner = findViewById(R.id.spinner_sido);
        final Spinner sigunguSpinner = findViewById(R.id.spinner_sigungu);
        final Spinner dongmyunSpinner = findViewById(R.id.spinner_dongmyun);

        try {
            String[][] resultText = jsonParser(new SgisApiTask().execute().get()); // 시도 정보
            sidoList = new String[resultText.length + 1];
            sidoCode = new String[resultText.length];
            for (int i = 0; i < resultText.length; i++) {
                sidoList[i] = resultText[i][0];
                sidoCode[i] = resultText[i][1];
            }
            sidoList[resultText.length] = "시도";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        sidoAdapter = new HintAdapter(this, android.R.layout.simple_spinner_dropdown_item, sidoList);
        sidoSpinner.setAdapter(sidoAdapter);
        sidoSpinner.setSelection(sidoAdapter.getCount(), true);

        sigunguList = new String[2];
        sigunguList[1] = "시군구";
        sigunguAdapter = new HintAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sigunguList);
        sigunguSpinner.setAdapter(sigunguAdapter);
        sigunguSpinner.setSelection(sigunguAdapter.getCount(), true);
        sigunguSpinner.setEnabled(false);


        dongmyunList = new String[2];
        dongmyunList[1] = "동면읍";
        dongmyunAdapter = new HintAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, dongmyunList);
        dongmyunSpinner.setAdapter(dongmyunAdapter);
        dongmyunSpinner.setSelection(dongmyunAdapter.getCount(), true);
        dongmyunSpinner.setEnabled(false);

        sidoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != sidoAdapter.getCount()) {
                    checkSpinner = 1;
                    sigunguSpinner.setEnabled(true);
                    dongmyunSpinner.setEnabled(false);
                    try {
                        resultText = jsonParser(new SgisApiTask(sidoCode[position]).execute().get()); // 선택된 시도의 시군구 정보
                        sigunguList = new String[resultText.length + 1];
                        sigunguCode = new String[resultText.length];
                        for (int i = 0; i < resultText.length; i++) {
                            sigunguList[i] = resultText[i][0];
                            sigunguCode[i] = resultText[i][1];
                        }
                        sigunguList[resultText.length] = "시군구";
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    sigunguAdapter = new HintAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sigunguList);
                    sigunguSpinner.setAdapter(sigunguAdapter);
                    sigunguSpinner.setSelection(sigunguAdapter.getCount(), true);

                    sigunguSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != sigunguAdapter.getCount()) {
                                checkSpinner = 2;
                                dongmyunSpinner.setEnabled(true);
                                try {
                                    String[][] resultText = jsonParser(new SgisApiTask(sigunguCode[position]).execute().get()); // 선택된 시도+시군구의 동면읍 정보
                                    dongmyunList = new String[resultText.length + 1];
                                    dongmyunInfo = new String[resultText.length + 1][2];
                                    for (int i = 0; i < resultText.length; i++) {
                                        dongmyunList[i] = resultText[i][0];
                                        dongmyunInfo[i][0] = resultText[i][1];
                                        dongmyunInfo[i][1] = resultText[i][2];
                                    }
                                    dongmyunList[resultText.length] = "동면읍";
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                dongmyunAdapter = new HintAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, dongmyunList);
                                dongmyunSpinner.setAdapter(dongmyunAdapter);
                                dongmyunSpinner.setSelection(dongmyunAdapter.getCount(), true);

                                dongmyunSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (position != dongmyunAdapter.getCount()) {
                                            checkSpinner = 3;
                                            spinnerCode = dongmyunInfo[position][0];
                                            spinnerLocation = dongmyunInfo[position][1];
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button_search = findViewById(R.id.button_search); // 검색버튼을 눌렀을 때
        button_search.setOnClickListener(new View.OnClickListener() { // Fragment를 '스피너 위치(SpinnerLocation)' 맵으로 전환
            @Override
            public void onClick(View v) {
                searchCnt++;
                switch (checkSpinner) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "검색할 지역을 설정해주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "'시군구'를 설정해주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "'동면읍'을 설정해주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        showSpinnerFragment();
                        break;
                }
            }
        });

        Button button_current = findViewById(R.id.button_current);
        button_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == 2) {
                    sidoSpinner.setSelection(sidoAdapter.getCount(), true);
                    sigunguSpinner.setSelection(sigunguAdapter.getCount(), true);
                    dongmyunSpinner.setSelection(dongmyunAdapter.getCount(), true);
                    sigunguSpinner.setEnabled(false);
                    dongmyunSpinner.setEnabled(false);

                    mode = 1;
                    getSupportFragmentManager().beginTransaction().replace(R.id.locationContainer, currentLocationFragment).commit();
                }
            }
        });


        Button button = findViewById(R.id.button_setLocation); // 위치 설정버튼을 눌렀을 때
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String localString = "";
                String localCode = "";
                if (mode == 1) { // Fragment가 '현재 위치(CurrentLocation)'가 되면 현재 위치로 지역 설정
                    localString = currentLocation;
                    localCode = currentCode;
                } else if (mode == 2) { // Fragment가 '스피너 위치(SpinnerLocation)'가 되면 spinner로 설정한 주소로 지역 설정
                    localString = spinnerLocation;
                    localCode = spinnerCode;
                }

                Intent intent = new Intent();
                if (localString.length() != 0) {
                    intent.putExtra("localString", localString); // 주소
                    intent.putExtra("localCode", localCode); // 지역 코드
                    setResult(RESULT_OK, intent);
                    onBackPressed(); //MarketNoLocation 프래그먼트로 응답보냄
                } else {
                    setResult(RESULT_CANCELED, intent);
                }
            }
        });

        ImageButton closeButton = findViewById(R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void showSpinnerFragment() {
        // Spinner주소가 검색/입력되면 Fragment를 CurrentLocation에서 SpinnerLocation으로 교체
        mode = 2;
        if (searchCnt == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.locationContainer, spinnerLocationFragment).commit();
        } else {
            SpinnerLocationFragment fragment = new SpinnerLocationFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.locationContainer, fragment).commit();
        }
    }

    @Override
    public void onReceivedCurrentLocation(String currentLocation, String currentCode) { //CurrentLocation 프래그먼트에서 현위치 설정 시 지역정보 받아옴
        this.currentLocation = currentLocation;
        this.currentCode = currentCode;
    }

    public String getSpinnerLocation() {
        return spinnerLocation; //스피너 설정 시 스피너 위치를 맵으로 띄운다
    }

    public String[][] jsonParser(String jsonString) {
        String[][] arraysum = new String[1][];

        String[] array = new String[1];

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String result = jsonObject.getString("result");
            JSONArray jsonArray = new JSONArray(result);

            arraysum = new String[jsonArray.length()][3];

            array = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject subJsonObject = jsonArray.getJSONObject(i);
                arraysum[i][0] = subJsonObject.getString("addr_name"); // (ex. 개포1동), Spinner에 추가할 때 사용
                arraysum[i][1] = subJsonObject.getString("cd"); // 지역코드
                arraysum[i][2] = subJsonObject.getString("full_addr"); // 전체 주소 (ex. 서울특별시 강남구 개포1동)
            }

        } catch (JSONException e) {
            Log.d(TAG,"ERROR :"+e.getMessage());
        }
        return arraysum;
    }
}