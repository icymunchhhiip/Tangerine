package com.sixsense.tangerine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
    private static final String TAG = "DownloadFileTask";

    private static final String SERVER_URL = "http://115.85.183.191/";
    private OnTaskCompletedListener listener;
    private ImageView imageView; //적용할 이미지뷰
    private int pathForm;//절대경로인지 상대경로인지

    public DownloadFilesTask(OnTaskCompletedListener listener, ImageView imageView, int pathForm){
        this.listener = listener;
        this.imageView = imageView;
        this.pathForm = pathForm;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bmp = null;
        try {
            String img_url = strings[0]; //url of the image

            URL url = null;
            if (pathForm == AppConstants.ABSOLUTE_PATH){
                url = new URL(img_url);
            }
            else if(pathForm == AppConstants.RELATIVE_PATH) {
                url = new URL(SERVER_URL + img_url);
            }
            else{
                Log.e(TAG,"ERROR get download url");
            }
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            Log.e(TAG,e.getMessage());
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(result != null){
            listener.onDownloadImgSet(imageView,result);
        }
    }
}