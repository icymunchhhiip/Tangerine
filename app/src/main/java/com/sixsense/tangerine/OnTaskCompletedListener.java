package com.sixsense.tangerine;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface OnTaskCompletedListener {
    void onDownloadImgSet(ImageView imageView, Bitmap bitmap);
    void jsonToItem(String jsonString);

    void noResultNotice(String searchString);
}
