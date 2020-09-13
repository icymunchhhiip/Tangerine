package com.sixsense.tangerine.setting;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BackgroundWorker2 {
    // Make a POST Request Handler


    public String postRequestHandler(String requestUrl, Member requestParams) throws UnsupportedEncodingException {

        // Set an Empty URL obj in system

        int m_id = requestParams.getId();
        String m_profile = requestParams.getProfilePath();
        String m_name =requestParams.getNickname();
        int m_localcode = requestParams.getLocalCode();
        String m_localstr = requestParams.getLocalString();


        URL url;


        // Set a String Builder to store result as string
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // Now Initialize URL
            url = new URL(requestUrl);

            // Make a HTTP url connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set Method Type
            connection.setRequestMethod("GET");
            // Set Connection Time
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            // set Input output ok connection.setDoInput(true);
            connection.setDoOutput(true);
            // Remove Caches
            //connection.setUseCaches(false);
            //connection.setDefaultUseCaches(false);


            // Creating a url as String with params
//            StringBuilder url_string = new StringBuilder();


//            boolean ampersand = false;
//            for (Myingredient myingredient : ingredientList) {
//                if (ampersand)
//                    url_string.append("&");
//                else ampersand = true;
//                url_string.append(URLEncoder.encode(params.getKey(), "UTF-8"));
//                url_string.append("=");
//                url_string.append(URLEncoder.encode(params.getValue(), "UTF-8"));
//            }


            String url_string = "m_id=" + m_id + "&m_profile=" + m_profile + "&m_name=" + m_name + "&m_localcode=" + m_localcode + "&m_localstr=" + m_localstr;
            Log.d("Final Url===", url_string);


            //Creating an output stream
            OutputStream outputStream = connection.getOutputStream();

            //Write Output Steam
            //BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            outputStream.write(url_string.getBytes("UTF-8"));

            outputStream.flush();
            outputStream.close();

            // Log.d("Response===", connection.getResponseMessage());

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                // Local String
                String result;
                while ((result = bufferedReader.readLine()) != null) {
                    stringBuilder.append(result);
                }
                // Log.d("Result===", result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    // Get Request Handler
    public String getRequestHandler(String requestUrl) {
        // To Store response
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL(requestUrl);
            // Open Connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            // Set Method Type
            connection.setRequestMethod("POST");
            // Set Connection Time
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            // set Input output ok connection.setDoInput(true);
            connection.setDoOutput(true);



            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // Local
            String result;
            while ((result = bufferedReader.readLine()) != null) {
                stringBuilder.append(result + "\n");
            }
        } catch (
                MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

