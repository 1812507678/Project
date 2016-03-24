package com.example.haijun.project;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by haijun on 2016/3/23.
 */
public class MyApplaction extends Application {

    private SharedPreferences sharedPreferences;
    private int lineNumber;
    private int target;
    private int hightestRecord;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        lineNumber= sharedPreferences.getInt("lineNumber", 4);
        target= sharedPreferences.getInt("target", 2048);
        hightestRecord= sharedPreferences.getInt("hightestRecord",0);

        //设置一个捕获到全局未捕获异常的捕获器。
        //可以在这里把异常信息通过网络发送给服务器。
        //可以把异常信息保存到sd卡上。
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {

            }
        });
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("lineNumber",lineNumber);
        edit.commit();
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("target",target);
        edit.commit();
    }

    public int getHightestRecord() {
        return hightestRecord;
    }

    public void setHightestRecord(int hightestRecord) {
        this.hightestRecord = hightestRecord;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("hightestRecord",hightestRecord);
        edit.commit();
    }
}
