package com.sixsense.tangerine.setting;
import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;

public class PostRequestHandler2 extends AsyncTask<String, Void, String> {
    // php URL 주소
    String url;
    // Key, Value 값

    private Member member;

    PostRequestHandler2(String url, Member member){
        this.url = url;
        this.member = member;

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        BackgroundWorker2 backgroundWorker = new BackgroundWorker2();
        try {
            String s = backgroundWorker.postRequestHandler(url, member);
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
