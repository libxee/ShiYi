package com.rair.diary.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;

import com.rair.diary.config.Cockroach;
import com.rair.diary.constant.Constants;
import com.rair.diary.utils.SPUtils;
import com.rair.diary.utils.StatusBarUtil;
import com.rair.diary.utils.Utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;


public class RairApp extends Application {

    private static RairApp rairApp;
    private SPUtils spUtils;

    /**
     * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
     * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
     * UMConfigure.init调用中appkey和channel参数请置为null）。
     */
    @Override
    public void onCreate() {
        super.onCreate();
        rairApp = this;
        Utils.init(this);
        spUtils = new SPUtils(Constants.SP_NAME);
        Cockroach.install(exceptionHandler);
        configTheme();
    }

    public void configTheme() {
        boolean isNight = spUtils.getBoolean("isNight", false);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    Cockroach.ExceptionHandler exceptionHandler = new Cockroach.ExceptionHandler() {
        @Override
        public void handlerException(Thread thread, Throwable throwable) {
            Log.e("TAG", "handlerException: " + throwable);
        }
    };

    public static RairApp getRairApp() {
        return rairApp;
    }

    public SPUtils getSpUtils() {
        return spUtils;
    }

    public File getRairPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File storageDirectory = Environment.getExternalStorageDirectory();
            File rairFile = new File(storageDirectory, Constants.RAIR_PATH);
            if (!rairFile.exists()) {
                rairFile.mkdirs();
            }
            return rairFile;
        }
        return getCacheDir();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Cockroach.uninstall();
    }
}
