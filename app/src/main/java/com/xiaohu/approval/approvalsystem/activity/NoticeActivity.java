package com.xiaohu.approval.approvalsystem.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaohu.approval.approvalsystem.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/7/4.
 */
public class NoticeActivity extends Activity {
    PowerManager.WakeLock wakeLock;
    RelativeLayout relativeLayout;
    TextView textNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.notice_activity);
        String num=getIntent().getStringExtra("HttpNum");
        relativeLayout= (RelativeLayout) findViewById(R.id.notice_relative_layout);
        textNum= (TextView) findViewById(R.id.notice_mes_num);

        textNum.setText(num);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("APPROVAL", Context.MODE_PRIVATE);
                String port = sharedPreferences.getString("PORT", "");
                String ip = sharedPreferences.getString("IP", "");
                String uname = sharedPreferences.getString("UserName", "");
                String upsw = sharedPreferences.getString("UserPasswrod", "");
                String url = "http://" + ip + ":" + port + "/Mobile/SearchApprove.aspx?username=" + uname +"&password=" + upsw;
                Uri uri = Uri.parse(url);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });
        Timer timer=new Timer();
        TimerTask task=new TimerTask(){
            public void run(){
               finish();
                //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        };
        timer.schedule(task, 4000);
    }

    @Override
    protected void onResume() {
        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "TAG");
        //wakeLock.acquire();
//启用屏幕常亮功能
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "TAG");
        wakeLock.acquire();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //关闭 屏幕常亮功能
        if (wakeLock != null) {wakeLock.release();}
        super.onPause();
    }
}
