package com.example.haijun.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

public class OptionActivity extends Activity {

    private Button bt_option_lines;
    private Button bt_option_target;
    private Button bt_option_cancel;
    private Button bt_option_done;
    private MyApplaction myApplaction;
    private int lineNumber;
    private int target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        bt_option_lines = (Button) findViewById(R.id.bt_option_lines);
        bt_option_target = (Button) findViewById(R.id.bt_option_target);
        bt_option_cancel = (Button) findViewById(R.id.bt_option_cancel);
        bt_option_done = (Button) findViewById(R.id.bt_option_done);

        bt_option_lines.setOnClickListener(onClickListener);
        bt_option_target.setOnClickListener(onClickListener);
        bt_option_cancel.setOnClickListener(onClickListener);
        bt_option_done.setOnClickListener(onClickListener);

        myApplaction = (MyApplaction) getApplication();
        lineNumber = myApplaction.getLineNumber();
        target = myApplaction.getTarget();

        Log.i("lineNumber",lineNumber+"");
        Log.i("target", target + "");
        bt_option_lines.setText(lineNumber+"");
        bt_option_target.setText(target+"");

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_option_lines:
                    setLines();
                    break;
                case R.id.bt_option_target:
                    setTarget();
                    break;
                case R.id.bt_option_cancel:
                    OptionActivity.this.finish();
                    break;
                case R.id.bt_option_done:
                    down();
                    OptionActivity.this.finish();
                    break;
            }
        }
    };

    private void setTarget() {
        final String[] lines = new String[]{"512","1024", "2048", "4096"};
        new AlertDialog.Builder(this)
                .setTitle("选择目标值")
                .setItems(lines, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bt_option_target.setText(lines[which]);
                        target = Integer.parseInt(lines[which]);
                    }
                })
                .show();
    }

    private void down() {
        myApplaction.setLineNumber(lineNumber);
        myApplaction.setTarget(target);
    }

    private void setLines() {
        final String[] lines = new String[]{"4", "5", "6","7","8"};
        new AlertDialog.Builder(this)
                .setTitle("选择行数")
                .setItems(lines, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bt_option_lines.setText(lines[which]);
                        lineNumber = Integer.parseInt(lines[which]);
                    }
                })
                .show();
    }
}
