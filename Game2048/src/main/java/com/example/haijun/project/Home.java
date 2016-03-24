package com.example.haijun.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Home extends Activity {

    private TextView tv_home_target;
    private TextView tv_home_score;
    private TextView tv_home_record;
    private static Home homeActivity;
    private Button bt_home_revert;
    private Button bt_home_restart;
    private Button bt_home_options;
    private GameView mgameView;
    private MyApplaction myApplaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RelativeLayout rl_home_content = (RelativeLayout) findViewById(R.id.rl_home_content);
        tv_home_score = (TextView) findViewById(R.id.tv_home_score);
        tv_home_record = (TextView) findViewById(R.id.tv_home_record);
        tv_home_target = (TextView) findViewById(R.id.tv_home_target);

        bt_home_revert = (Button) findViewById(R.id.bt_home_revert);
        bt_home_restart = (Button) findViewById(R.id.bt_home_restart);
        bt_home_options = (Button) findViewById(R.id.bt_home_options);

        bt_home_revert.setOnClickListener(onClickListener);
        bt_home_restart.setOnClickListener(onClickListener);
        bt_home_options.setOnClickListener(onClickListener);

        homeActivity = this;
        mgameView = new GameView(this);
        myApplaction = (MyApplaction) getApplication();
        tv_home_target.setText(myApplaction.getTarget()+"");
        tv_home_record.setText(myApplaction.getHightestRecord() + "");

        rl_home_content.addView(mgameView);
    }

    //更新Score
    public void updateCurrentScore(int score){
        tv_home_score.setText(score + "");
    }

    //更新target
    public void updateTarget(int score){
        tv_home_target.setText(score + "");
    }

    //更新Recorde
    public void updateHightestScore(int newHightestRecorde){
        tv_home_record.setText(newHightestRecorde+"");
    }

    /*public static Home getHomeActivity(){
        return homeActivity;
    }*/

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_home_revert:
                    revert();
                    break;
                case R.id.bt_home_restart:
                    restart();
                    break;
                case R.id.bt_home_options:
                    options();
                    break;

            }
        }

        private void restart() {
            new AlertDialog.Builder(Home.this).
                    setTitle("确定？")
                    .setMessage("确定重新开始吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mgameView.restart();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }

        private void revert() {
            mgameView.revert();
        }
    };

    public void options() {
        startActivityForResult(new Intent(Home.this,OptionActivity.class),100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateTarget(myApplaction.getTarget());
        mgameView.restart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mgameView.updateHightestRecorde();
    }
}



