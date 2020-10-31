package com.sixsense.tangerine.community;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sixsense.tangerine.AppConstants;
import com.sixsense.tangerine.HttpClient;
import com.sixsense.tangerine.OnTaskCompletedListener;

import org.json.JSONException;
import org.json.JSONObject;

public class GetDataTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetDataTask";
    private OnTaskCompletedListener listener;
    private String[] paramNames = null;
    private String[] values = null;
    private int mode;

    public GetDataTask(OnTaskCompletedListener listener, String[] paramNames, String[] values, int mode) {
        this.listener = listener;
        this.paramNames = paramNames;
        this.values = values;
        this.mode = mode;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlpage = (String) params[0];

        HttpClient httpClient = new HttpClient();
        String result = httpClient.GetDataRequest(urlpage, paramNames, values);

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JSONObject jo = new JSONObject(result);
            String prettyJsonString = gson.toJson(jo);

            Log.d(TAG, prettyJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (mode == AppConstants.MODE_SEARCH  && result.equals("no result")) {
            listener.noResultNotice(values[1]);
        } else if (result != null) {
            if(mode == AppConstants.MODE_READ || mode == AppConstants.MODE_SEARCH) {
                if (listener.jsonToItem(result) == true)
                    return;
            }
        }
    }
}
