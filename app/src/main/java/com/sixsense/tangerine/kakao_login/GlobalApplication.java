package com.sixsense.tangerine.kakao_login;

import android.app.Application;

import com.kakao.auth.KakaoSDK;

public class GlobalApplication extends Application {
    private static GlobalApplication mInstance;

    public static GlobalApplication getGlobalApplicationContext() {
        if (mInstance == null) {
            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mInstance = null;
    }
}