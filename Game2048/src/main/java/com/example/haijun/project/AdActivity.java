package com.example.haijun.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

public class AdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*startActivity(new Intent(AdActivity.this,Home.class));
                finish();*/

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(AdActivity.this,Home.class));
                        finish();
                    }
                });
            }
        }.start();

        AdManager.getInstance(this).init("3011b8ffd4d51c09", "d09783b59eae715e", true);
        SpotManager.getInstance(this).loadSpotAds();
        SpotManager.getInstance(this).setSpotOrientation(SpotManager.ORIENTATION_PORTRAIT);
        SpotManager.getInstance(this).setAnimationType(SpotManager.ANIM_SIMPLE);
        SpotManager.getInstance(this).showSpotAds(this);
    }
}
