package com.sixsense.tangerine.community;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ReverseGeocodeApiTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "ReverseGeocodeApiTask";

    private final String clientId = "vljs114eb7";  //clientId
    private final String clientSecret = "Al291Zsh9XDlrX9jpdb43gzn0V2OhSOLBDjMkt4g ";  //clientSecret
    private String location;
    private String inputLine, receiveMSG;

    public ReverseGeocodeApiTask(String location) {
        this.location = location;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            String coords = URLEncoder.encode(location, "UTF-8");
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords="+coords+"&sourcecrs=epsg:4326&orders=admcode&output=json"; //json
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            if (con.getResponseCode() == con.HTTP_OK) { // 정상 호출
                InputStreamReader tmp = new InputStreamReader(con.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                while ((inputLine = reader.readLine()) != null) {
                    buffer.append(inputLine);
                }
                receiveMSG = buffer.toString();
                reader.close();
                Log.d(TAG, "Naver Map SUCCESS Response :" + con.getResponseCode() + con.getResponseMessage());

            } else { // 에러 발생
                Log.e(TAG, "Naver Map ERROR Response :" + con.getResponseCode() + con.getResponseMessage());

            }
        } catch (MalformedURLException e) {
            Log.e(TAG,e.getMessage());
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }

        return receiveMSG;
    }
}
