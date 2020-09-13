package com.sixsense.tangerine;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class JsonParser {
    private static final String TAG = JsonParser.class.getSimpleName();

    public String convertJson(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // read the response
            int responseStatusCode = conn.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }

//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

//            InputStream in = new BufferedInputStream(inputStream);
            response = convertStreamToString(inputStream);


        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }
    public String convertJson(String reqUrl, String USERID) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String userid = "m_id=" + USERID;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();


            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod(Constant.POST_METHOD);
            conn.connect();


            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(userid.getBytes("UTF-8"));

            outputStream.flush();
            outputStream.close();


            int responseStatusCode = conn.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }

//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

//            InputStream in = new BufferedInputStream(inputStream);
            response = convertStreamToString(inputStream);


        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }


    public String convertJson(String reqUrl, int USERID) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String userid = "m_id=" + USERID;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();


            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod(Constant.POST_METHOD);
            conn.connect();


            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(userid.getBytes("UTF-8"));

            outputStream.flush();
            outputStream.close();


            int responseStatusCode = conn.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }

//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

//            InputStream in = new BufferedInputStream(inputStream);
            response = convertStreamToString(inputStream);


        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
//                sb.append(line).append('\n');
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //return sb.toString();
        return sb.toString().trim();
    }
}

