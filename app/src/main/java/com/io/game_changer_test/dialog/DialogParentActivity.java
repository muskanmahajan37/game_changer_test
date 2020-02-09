package com.io.game_changer_test.dialog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.io.game_changer_test.R;


public class DialogParentActivity extends Activity {
    String isConnected;
    private TextView tv_no_internet;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        isConnected = getIntent().getStringExtra("isConnected");
        registerReceiver(abcd, new IntentFilter("xyz"));
            setContentView(R.layout.no_internet_dialog);
        tv_no_internet = findViewById(R.id.tv_no_internet);
        tv_no_internet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    finish();
                }else{
                    Toast.makeText(DialogParentActivity.this,"Please check Your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
            this.setFinishOnTouchOutside(false);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    private final BroadcastReceiver abcd = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };
}