package com.magicsoft.whalepaydemo;

import android.app.Application;

import com.magicsoft.whale.util.ImageLoaderUtil;

/**
 * Created by junier_li on 2017/3/7.
 */

public class WhaleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoaderUtil.initImageLoader(getApplicationContext());
    }


}
