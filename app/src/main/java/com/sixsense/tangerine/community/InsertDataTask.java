package com.sixsense.tangerine.community;

import android.os.AsyncTask;
import android.util.Log;

public class InsertDataTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "InsertTask";

    private OnTaskCompletedListener listener;
    private String[] paramNames;
    private String[] values;

    public InsertDataTask(OnTaskCompletedListener listener, String[] paramNames, String[] values) {
        this.listener = listener;
        this.paramNames = paramNames;
        this.values = values;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(TAG, "POST response :" + s);
    }

    @Override
    protected String doInBackground(String... params) {
        String urlPage = (String) params[0];
        String imgPath = (String) params[1];
        String imgName = (String) params[2];

        HttpClient httpClient = new HttpClient();
        String result = httpClient.InsertDataRequest(urlPage, paramNames, values, imgPath, imgName);

        if (result == "" || result == null) {
            result = "InsertTask result: null";
        }
        return result;
    }
}
