package com.sixsense.tangerine.community;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SgisGeocodeApiTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "SgisGeocodeApiTask";

    private final String consumer_key = "50251592a5d740ec9b9f";
    private final String consumer_secret = "636cf5c3a7fb4817a4e2";
    private String accessToken;
    private String address = null; // Geocode되는 주소
    private String inputLine, receiveMSG;

    public SgisGeocodeApiTask(String address) {
        this.address = address;
    } //CurrentLocationFragment에서 현재 위치 전달

    public void getAccessCode() {
        try {
            String consumer_key = URLEncoder.encode(this.consumer_key, "UTF-8");
            String consumer_secret = URLEncoder.encode(this.consumer_secret, "UTF-8");
            String apiURL = "https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json?consumer_key=" + consumer_key + "&consumer_secret=" + consumer_secret;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            if (con.getResponseCode() == con.HTTP_OK) { // 정상 호출
                InputStreamReader tmp = new InputStreamReader(con.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((inputLine = reader.readLine()) != null) {
                    buffer.append(inputLine);
                }
                String receiveInfo = buffer.toString();
                reader.close();

                String str = "";

                try {
                    JSONObject jsonObject1 = new JSONObject(receiveInfo);
                    String result = jsonObject1.getString("result");
                    JSONObject jsonObject2 = new JSONObject(result);

                    str = jsonObject2.getString("accessToken");

                } catch (JSONException e) {
                    Log.e(TAG,e.getMessage());
                }
                accessToken = str;

            } else { // 에러 발생
                Log.d(TAG,"ERROR");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG,e.getMessage());
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    protected String doInBackground(String... params) {
        //CurrentLocationFragment에서 현재 위치 전달받아 지역코드 반환
        try {
            getAccessCode();
            String apiURL = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/geocode.json?accessToken="+accessToken+"&address="+address;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            if (con.getResponseCode() == con.HTTP_OK) { // 정상 호출
                InputStreamReader tmp = new InputStreamReader(con.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((inputLine = reader.readLine()) != null) {
                    buffer.append(inputLine);
                }
                receiveMSG = buffer.toString();
                reader.close();

                // 받아온 json파일의 데이터 파싱
                String adm_cd = "";
                try {
                    JSONObject jsonObject = new JSONObject(receiveMSG).getJSONObject("result");
                    JSONArray jsonArray = jsonObject.getJSONArray("resultdata");
                    for(int i=0;i<jsonArray.length();i++) {
                        adm_cd = jsonArray.getJSONObject(i).getString("adm_cd");
                    }
                } catch (JSONException e) {
                    Log.e(TAG,e.getMessage());
                }
                receiveMSG = adm_cd;

            } else { // 에러 발생
                Log.d(TAG,"ERROR");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG,e.getMessage());
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }
        return receiveMSG;
    }
}