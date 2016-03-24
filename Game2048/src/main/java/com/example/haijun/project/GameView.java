package com.example.haijun.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.*;

/**
 * Created by haijun on 2016/3/21.
 */
public class GameView extends GridLayout {
    private static final String TAG = "GameView";
    private  List<Point> blackList;
    private  NumberItem[][] numberItemMatrix;
    public static int gridRow;
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private List<Integer> fillList;
    private int currentScore;
    private int target;
    private int hightestScore;
    private Home mHome;
    private int[][] histroyMatrix;
    private boolean canRevert = false;
    private MyApplaction myApplaction;


    public GameView(Context context) {
        super(context);
        //context为调用该构造函数的Activity
        mHome= (Home) context;
        initGridView();
    }

    private void initGridView() {
        myApplaction = (MyApplaction) mHome.getApplication();
        gridRow = myApplaction.getLineNumber();
        hightestScore = myApplaction.getHightestRecord();
        target = myApplaction.getTarget();

        mHome.updateCurrentScore(0);

        setRowCount(gridRow);
        setColumnCount(gridRow);
        setBackgroundColor(Color.parseColor("#898989"));

        blackList = new ArrayList<>();
        fillList = new ArrayList<>();
        numberItemMatrix = new NumberItem[gridRow][gridRow];
        histroyMatrix = new int[gridRow][gridRow];

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);

        for (int i=0;i< gridRow;i++) {
            for (int j = 0; j < gridRow; j++) {
                NumberItem  numberItem = new NumberItem(getContext(),0);
                LinearLayout linearLayout = new LinearLayout(mHome);
                linearLayout.addView(numberItem,layoutParams);
                numberItemMatrix[i][j] = numberItem;
                Point point = new Point(i,j);
                blackList.add(point);
                addView(linearLayout);
            }
        }
        //随机产生2个随机位置，填充数字2
        addRandomNumber();
        addRandomNumber();
    }

    private void addRandomNumber() {
        //updateBlackList();
        int size = blackList.size();
        int location = (int) Math.floor(Math.random() * size);
        Point point = blackList.get(location);
        numberItemMatrix[point.x][point.y].setTextNumber(2);
        //numberItemMatrix[point.x][point.y].setTextNumber(Math.random()>0.5?2:4);
        updateBlackList();
    }

    private void updateBlackList() {
        blackList.clear();
        for (int i=0;i<gridRow;i++) {
            for (int j = 0; j < gridRow; j++) {
                if (numberItemMatrix[i][j].getTextNumber()==0){
                    blackList.add(new Point(i,j));
                }
            }
        }
    }

    public void  updateHightestRecorde(){
        if (currentScore>hightestScore){
            hightestScore = currentScore;
            myApplaction.setHightestRecord(hightestScore);
            mHome.updateHightestScore(hightestScore);
        }
    }

    public void restart(){
        updateHightestRecorde();
        removeAllViews();
        initGridView();
        mHome.updateCurrentScore(currentScore);
    }

    public void revert(){
        if (canRevert){
            for (int i=0;i<gridRow;i++){
                for (int j=0;j<gridRow;j++){
                    numberItemMatrix[i][j].setTextNumber(histroyMatrix[i][j]);
                }
            }
        }
    }

    public void saveHistroy(){
        for (int i=0;i<gridRow;i++){
            for (int j=0;j<gridRow;j++){
                histroyMatrix[i][j] = numberItemMatrix[i][j].getTextNumber();
            }
        }
        canRevert = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                saveHistroy();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                stopX = event.getX();
                stopY = event.getY();
                judgeDeriction(startX, startY, stopX, stopY);
                updateBlackList();//移动后blackList的值发生变化，需要修改此值
                mHome.updateCurrentScore(currentScore);
                Log.i("blackList", blackList.size() + "");
                handleResult(isOver());
                Log.i("blackList1", blackList.size() + "");
                break;
        }
        return true;
    }

    private void handleResult(int result) {
        switch (result){
            case 1:
                if (gridRow==4 ||gridRow==5){
                    addRandomNumber();
                }
                else if (gridRow==6 ||gridRow==7){
                    addRandomNumber();
                    addRandomNumber();
                }
                else if (gridRow==8){
                    addRandomNumber();
                    addRandomNumber();
                    addRandomNumber();
                }
                break;
            case 2:
                new AlertDialog.Builder(mHome).setTitle("完成任务")
                        .setMessage("请选择接下来的操作")
                        .setPositiveButton("重玩", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                restart();
                            }
                        })
                        .setNegativeButton("挑战更难", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateHightestRecorde();
                                mHome.options();
                            }
                        })
                        .show();
                break;
            case 3:
                new AlertDialog.Builder(mHome).setTitle("挑战失败")
                        .setMessage("请选择接下来的操作")
                        .setPositiveButton("重玩", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                restart();
                            }
                        })
                        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mHome.finish();
                            }
                        })
                        .show();
                break;
            case 4:
                //其他方向上还可以移动，暂时不响应原来方向的滑动，什么都不做
                break;
        }
    }

    private int isOver(){
        for (int i=0;i<gridRow;i++){
            for (int j=0;j<gridRow;j++){
                if (numberItemMatrix[i][j].getTextNumber()==target){
                    //成功
                    return 2;
                }
            }
        }
        if (blackList.size()==0){
                for (int i=0;i<gridRow;i++){
                    for (int j=0;j<gridRow-1;j++){
                        if (numberItemMatrix[i][j].getTextNumber()==numberItemMatrix[i][j+1].getTextNumber()){
                            //在其他方向上还可以滑动（还有发挥空间）
                            return 4;
                        }
                        if (numberItemMatrix[j][i].getTextNumber()==numberItemMatrix[j+1][i].getTextNumber()){
                            return 4;
                        }
                    }
                }
            //失败
            return 3;
        }
        //未完成
        return 1;
    }

    private void judgeDeriction(float startX, float startY, float stopX, float stopY) {
        float xChange = stopX - startX;
        float yChange = stopY - startY;
        if (Math.abs(xChange)>NumberItem.widthPixels/15 ||Math.abs(yChange)>NumberItem.widthPixels/15){
            boolean isXDerction = Math.abs(xChange)>Math.abs(yChange)?true:false;
            //x方向移动
            if (isXDerction){
                //右移
                if (xChange>0){
                    slideRigth();
                }
                //左移
                else {
                    slideLeft();
                }
            }
            //y方向移动
            else {
                //下移
                if (yChange>0){
                    slideDown();
                }
                //上移
                else {
                    slideUp();
                }
            }
        }
    }

    private void slideUp() {
        for (int i=0;i<gridRow;i++){
            int preNumber = -1;
            for (int j=0;j<gridRow;j++){
                int textNumber = numberItemMatrix[j][i].getTextNumber();
                if (textNumber!=0){
                    if (textNumber!=preNumber && preNumber!=-1){
                        fillList.add(preNumber);
                    }
                    else if (preNumber!=-1){
                        fillList.add(preNumber*2);
                        currentScore += preNumber*2;
                        preNumber = -1;
                        continue;//终止当前的循环过程，但他并不跳出循环,而是继续往下判断循环条件执行语句.他只能结束循环中的一次过程,但不能终止循环继续进行.
                    }
                    preNumber = textNumber;
                }
            }
            if (preNumber!=0 &&preNumber!=-1){
                fillList.add(preNumber);
            }
            for (int n=0;n<fillList.size();n++){
                numberItemMatrix[n][i].setTextNumber(fillList.get(n));
            }
            for (int k=fillList.size();k<gridRow;k++){
                numberItemMatrix[k][i].setTextNumber(0);
            }
            fillList.clear();
        }
    }

    private void slideDown() {
        for (int i=0;i<gridRow;i++){
            int preNumber = -1;
            for (int j=gridRow-1;j>=0;j--){
                int textNumber = numberItemMatrix[j][i].getTextNumber();
                if (textNumber!=0){
                    if (textNumber!=preNumber && preNumber!=-1){
                        fillList.add(preNumber);
                    }
                    else if (preNumber!=-1){
                        fillList.add(preNumber*2);
                        currentScore += preNumber*2;
                        preNumber = -1;
                        continue;//终止当前的循环过程，但他并不跳出循环,而是继续往下判断循环条件执行语句.他只能结束循环中的一次过程,但不能终止循环继续进行.
                    }
                    preNumber = textNumber;
                }
            }
            if (preNumber!=0 &&preNumber!=-1){
                fillList.add(preNumber);
            }
            for (int n=gridRow-1;n>=gridRow-fillList.size();n--){
                numberItemMatrix[n][i].setTextNumber(fillList.get(gridRow-1-n));
            }
            for (int k=gridRow-1-fillList.size();k>=0;k--){
                numberItemMatrix[k][i].setTextNumber(0);
            }
            fillList.clear();
        }
    }

    private void slideLeft() {
        for (int i=0;i<gridRow;i++){
            int preNumber = -1;
            for (int j=0;j<gridRow;j++){
                int textNumber = numberItemMatrix[i][j].getTextNumber();
                if (textNumber!=0){
                    if (textNumber!=preNumber && preNumber!=-1){
                        fillList.add(preNumber);
                    }
                    else if (preNumber!=-1){
                        fillList.add(preNumber*2);
                        currentScore += preNumber*2;
                        preNumber = -1;
                        continue;//终止当前的循环过程，但他并不跳出循环,而是继续往下判断循环条件执行语句.他只能结束循环中的一次过程,但不能终止循环继续进行.
                    }
                    preNumber = textNumber;
                }
            }
            if (preNumber!=0 &&preNumber!=-1){
                fillList.add(preNumber);
            }
            for (int n=0;n<fillList.size();n++){
                numberItemMatrix[i][n].setTextNumber(fillList.get(n));
            }
            for (int k=fillList.size();k<gridRow;k++){
                numberItemMatrix[i][k].setTextNumber(0);
            }
            fillList.clear();
        }
    }

    private void slideRigth() {
        for (int i=0;i<gridRow;i++){
            int preNumber = -1;
            for (int j=gridRow-1;j>=0;j--){
                int textNumber = numberItemMatrix[i][j].getTextNumber();
                if (textNumber!=0 ){
                    if (textNumber!=preNumber && preNumber!=-1){
                        fillList.add(preNumber);
                    }
                    else if (preNumber!=-1){
                        fillList.add(preNumber*2);
                        currentScore += preNumber*2;
                        preNumber = -1;
                        continue;//终止当前的循环过程，但他并不跳出循环,而是继续往下判断循环条件执行语句.他只能结束循环中的一次过程,但不能终止循环继续进行.
                    }
                    preNumber = textNumber;
                }
            }
            if (preNumber!=0 &&preNumber!=-1){
                fillList.add(preNumber);
            }
            for (int n=gridRow-1;n>=gridRow-fillList.size();n--){
                numberItemMatrix[i][n].setTextNumber(fillList.get(gridRow-1-n));
            }
            for (int k=gridRow-1-fillList.size();k>=0;k--){
                numberItemMatrix[i][k].setTextNumber(0);
            }
            fillList.clear();
        }
    }
}
