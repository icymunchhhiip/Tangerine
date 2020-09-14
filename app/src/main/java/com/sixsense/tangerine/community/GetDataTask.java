package com.sixsense.tangerine.community;

import android.os.AsyncTask;

public class GetDataTask extends AsyncTask<String, Void, String> {
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

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (mode == AppConstants.MODE_SEARCH  && result.equals("no result")) {
            listener.noResultNotice(values[1]);
        } else if (result != null) {
            if(mode == AppConstants.MODE_READ || mode == AppConstants.MODE_SEARCH)
                listener.jsonToItem(result);
        }
    }
}
