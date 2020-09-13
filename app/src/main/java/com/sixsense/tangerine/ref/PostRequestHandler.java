package com.sixsense.tangerine.ref;

import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;

public class PostRequestHandler extends AsyncTask<String, Void, String> {
    // php URL 주소
    String url;
    // Key, Value 값
    private Myingredient ingredientList;


    public PostRequestHandler(String url, Myingredient params) {
        this.url = url;
        this.ingredientList = params;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        BackgroundWorker backgroundWorker = new BackgroundWorker();
        try {
            String s = backgroundWorker.postRequestHandler(url, ingredientList);
            //String s = backgroundWorker.getRequestHandler(url, ingredientList);
            return s.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
