package com.example.haijun.project;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by haijun on 2016/3/21.
 */
public class NumberItem extends TextView {


    private int textNumber;
    //private TextView textView;
    public static int widthPixels;


    public NumberItem(Context context) {
        super(context);
        initView(textNumber);
    }

    public NumberItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(textNumber);
    }

    public NumberItem(Context context,int textNumber) {
        super(context);
        initView(textNumber);
    }

    private void initView(int textNumber) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        defaultDisplay.getMetrics(displayMetrics);
        widthPixels = displayMetrics.widthPixels-10*2* GameView.gridRow;

       // textView = new TextView(getContext());

        setGravity(Gravity.CENTER);

        setWidth(widthPixels / GameView.gridRow);
        setHeight(widthPixels / GameView.gridRow);

        setTextNumber(textNumber);
        //addView(textView);
    }


    public void setTextNumber(int textNumber) {
        this.textNumber = textNumber;
        if (textNumber==0){
            setText("");
        }
        else {
            setText(textNumber + "");
        }
        switch (textNumber){
            case 0:
                setBackgroundColor(Color.parseColor("#898989"));
                break;
            case 2:
                setBackgroundColor(Color.parseColor("#EDE5DA"));
                break;
            case 4:
                setBackgroundColor(Color.parseColor("#ECE0C8"));
                break;
            case 8:
                setBackgroundColor(Color.parseColor("#F1C17A"));
                break;
            case 16:
                setBackgroundColor(Color.parseColor("#F59767"));
                break;
            case 32:
                setBackgroundColor(Color.parseColor("#F58D6F"));
                break;
            case 64:
                setBackgroundColor(Color.parseColor("#F66F3D"));
                break;
            case 128:
                setBackgroundColor(Color.parseColor("#ECCF74"));
                break;
            case 256:
                setBackgroundColor(Color.parseColor("#EDCC64"));
                break;
            case 512:
                setBackgroundColor(Color.parseColor("#F6429F"));
                break;
            case 1024:
                setBackgroundColor(Color.parseColor("#F65855"));
                break;
            case 2048:
                setBackgroundColor(Color.parseColor("#F64325"));
                break;
        }
    }

    public int getTextNumber(){
        return textNumber;
    }
}
