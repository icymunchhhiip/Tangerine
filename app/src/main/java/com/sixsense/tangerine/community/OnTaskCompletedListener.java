package com.sixsense.tangerine.community;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface OnTaskCompletedListener {
    void onDownloadImgSet(ImageView imageView, Bitmap bitmap);
    void jsonToItem(String jsonString);

    void noResultNotice(String searchString); //검색결과 없을때
}
