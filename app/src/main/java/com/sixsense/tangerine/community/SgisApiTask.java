package com.sixsense.tangerine.community;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SgisApiTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "SgisApiTask";

    private final String consumer_key = "50251592a5d740ec9b9f";
    private final String consumer_secret = "636cf5c3a7fb4817a4e2";
    private String accessToken;
    private String cd = null;
    private String inputLine, receiveMSG, receiveInfo;

    public SgisApiTask() { }
    public SgisApiTask(String cd) {
        this.cd = cd;
    }

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
                receiveInfo = buffer.toString();
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
                Log.e(TAG,"ERROR");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG,e.getMessage());
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            getAccessCode();
            URL url = new URL(getURL());
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

            } else { // 에러 발생
                Log.e(TAG,"ERROR");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG,e.getMessage());
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }
        return receiveMSG;
    }

    public String getURL() {
        String apiURL = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json?accessToken="+accessToken;
        if(cd != null) {
            apiURL += "&cd="+cd;
        }
        return apiURL;
    }
}
