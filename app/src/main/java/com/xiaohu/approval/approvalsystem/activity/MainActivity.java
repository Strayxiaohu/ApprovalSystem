package com.xiaohu.approval.approvalsystem.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaohu.approval.approvalsystem.R;
import com.xiaohu.approval.approvalsystem.service.NoticeService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    WebView webView;
    TextView textMes, textLogin;
    Intent intent;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("APPROVAL", Context.MODE_PRIVATE);
        webView = (WebView) findViewById(R.id.webview);
        textMes = (TextView) findViewById(R.id.main_mes);
        textLogin= (TextView) findViewById(R.id.main_login);
        initEvent();
    }

    private void initEvent() {
        intent = new Intent(MainActivity.this, NoticeService.class);
            System.out.println("  開啟  service...");
            startService(intent);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        String port = sharedPreferences.getString("PORT", "");
        String ip = sharedPreferences.getString("IP", "");
        String url = "http://" + ip + ":" + port + "/Mobile/SearchApprove.aspx";
        synCookies(url);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    System.out.println("---------------------------------------ssss");
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
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url); //在当前的webview中跳转到新的url

                return true;
            }
        });
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("UserName","");
                editor.putString("UserPasswrod","");
                editor.putString("ASP.SessionId","");
                editor.commit();
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                if (isServiceRunning("com.xiaohu.approval.approvalsystem.service")) {
                    stopService(intent);
                }
            }
        });
    }

    private void synCookies(String url) {
        String sessionid = sharedPreferences.getString("ASP.SessionId", "");

        StringBuilder sbCookie = new StringBuilder();
        System.out.println(sessionid+":::0000sssssss");
        sbCookie.append(String.format("ASP.NET_SessionId=%s", sessionid));
        sbCookie.append(String.format(";domain=%s", "192.168.1.113"));
        sbCookie.append(String.format(";path=%s", "/"));
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();

        cookieManager.setCookie(url, sbCookie.toString());
        CookieSyncManager.getInstance().sync();
    }

    @Override
    protected void onPause() {
//        if(!sharedPreferences.getString("UserName","").equals("")) {
//            intent = new Intent(MainActivity.this, NoticeService.class);
//            System.out.println("  開啟  service...");
//            startService(intent);
//        }
        super.onPause();
    }

    private static boolean mBackKeyPressed = false;//记录是否有首次按键

    @Override
    protected void onResume() {
//        if (isServiceRunning("com.xiaohu.approval.approvalsystem.service")) {
//            System.out.println("退出service...");
//            this.stopService(intent);
//        }
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

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
//            if (isServiceRunning("com.xiaohu.approval.approvalsystem.service")) {
                System.out.println("退出service...");
              //  this.stopService(intent);
          //  }
//            SharedPreferences.Editor editor=sharedPreferences.edit();
//            editor.putString("ASP.SessionId","");
//            editor.commit();
            this.finish();
           // System.exit(0);
        }
    }

    //        * 用来判断服务是否运行.
//                * @param context
//                * @param className 判断的服务名字
//                * @return true 在运行 false 不在运行
//                */
    private boolean isServiceRunning(String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
