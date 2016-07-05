package com.xiaohu.approval.approvalsystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import com.xiaohu.approval.approvalsystem.R;
import com.xiaohu.approval.approvalsystem.service.NoticeService;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    WebView webView;
    TextView textMes;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         intent = new Intent(MainActivity.this, NoticeService.class);
        startService(intent);
        webView = (WebView) findViewById(R.id.webview);
        textMes = (TextView) findViewById(R.id.main_mes);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl("https://www.baidu.com");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //网页加载完成
                    textMes.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                } else {
                    //加载中
                    textMes.setText("正在加载中...");
                    textMes.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                }
            }
        });
    }

    private static boolean mBackKeyPressed = false;//记录是否有首次按键

    //双击退出程序
    @Override
    public void onBackPressed() {
        if (!mBackKeyPressed) {
            Toast.makeText(this, "再按一次退出程序,退出系统将接收不到消息", Toast.LENGTH_SHORT).show();
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        } else {//退出程序
            this.stopService(intent);
            this.finish();
            System.exit(0);
        }
    }
}
