package com.example.srb;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application
{
    private static Context appContext;
    public static ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override
    public void onCreate()
    {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getContext()
    {
        return appContext;
    }
}
