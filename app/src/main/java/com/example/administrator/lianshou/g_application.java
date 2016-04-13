package com.example.administrator.lianshou;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
/**
 * Created by Administrator on 2016/4/8.
 */
public class g_application extends Application{
    public static Context AppContext = null;
    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
        Fresco.initialize(AppContext);
    }
}
