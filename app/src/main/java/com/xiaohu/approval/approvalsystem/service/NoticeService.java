package com.xiaohu.approval.approvalsystem.service;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;

import com.xiaohu.approval.approvalsystem.R;
import com.xiaohu.approval.approvalsystem.activity.MainActivity;
import com.xiaohu.approval.approvalsystem.activity.NoticeActivity;
import com.xiaohu.approval.approvalsystem.http.HttpUtils;

/**
 * Created by Administrator on 2016/7/2.
 */
public class NoticeService extends Service {
    private Handler myHandler = new Handler();
    private String httpResult = "0";
    /**
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private Runnable myTasks = new Runnable() {
        @Override
        public void run() {
            //间隔多久
            Message msg = myHandler.obtainMessage();
            msg.obj = 0;
            myHandler.sendMessage(msg);
            System.out.println("```````````2");
            myHandler.postDelayed(myTasks, 20000);
        }
    };

    @Override
    public void onCreate() {
        System.out.println("``````````1`");
        new Thread(myTasks).start();
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //访问服务器
                getWebGetNum();
            }
        };
        // myHandler.postDelayed(myTasks,1000);
        super.onCreate();
    }

    private void getWebGetNum() {
        HttpUtils httpUtils = new HttpUtils();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println(msg.obj.toString() + "-----");
                //显示通知
                String strNum = msg.obj.toString();
                //测试
                httpResult = "未审批-" + strNum.substring(2) + "条";
                AddNotification();
                //结束
//                if (strNum.contains("数量")) {
//                    if(!strNum.substring(2).equals("0")) {
//                        strNum="未审批-" + strNum.substring(2)+"条";
//                        if (strNum.equals(httpResult)) {
//                            //不重新提醒
//
//                        } else {
//                            httpResult = strNum;
//                            AddNotification();
//                        }
//                    }
//                } else {
//                    if (strNum.equals(httpResult)) {
//                        //不重新提醒
//                    } else {
//                        httpResult=strNum;
//                        AddNotification();
//                    }
//                }
            }
        };
        SharedPreferences sharedPreferences = getSharedPreferences("APPROVAL", Context.MODE_PRIVATE);
        String ip = sharedPreferences.getString("IP", "");
        String port = sharedPreferences.getString("PORT", "");
        String uname = sharedPreferences.getString("UserName", "");
        String upsw = sharedPreferences.getString("UserPasswrod", "");
        if (!uname.equals("")) {
            String url = "http://" + ip + ":" + port + "/Mobile/GetApprove.ashx?username=" + uname +"&password=" + upsw;
            httpUtils.HttpGet(this, handler, url, true);
        }
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        //当服务结束，删除mtasks运行线程
        myHandler.removeCallbacks(myTasks);
        super.onDestroy();
    }

    /**
     * 添加顶部通知
     */
    public void AddNotification() {
        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);
        KeyguardManager mKeyguardManager = (KeyguardManager) getSystemService(this.KEYGUARD_SERVICE);
        if (!pm.isScreenOn()) {
            Intent intent = new Intent(this, NoticeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("HttpNum", httpResult);
            startActivity(intent);
        }
            //添加通知到顶部任务栏
            //获得NotificationManager实例
            String service = NOTIFICATION_SERVICE;
            NotificationManager nm = (NotificationManager) getSystemService(service);
            //实例化Notification
            Notification n = new Notification();
            //设置显示图标
            int icon = R.mipmap.mylogo;
            //设置提示信息
            String tickerText = "我的程序";
            //显示时间
            long when = System.currentTimeMillis();
            n.defaults = Notification.DEFAULT_SOUND;
            n.icon = icon;
            n.tickerText = tickerText;
            n.when = when;
            //显示在“正在进行中”
            //  n.flags = Notification.FLAG_ONGOING_EVENT;
            n.flags |= Notification.FLAG_AUTO_CANCEL; //自动终止

            //实例化Intent
//            Intent it = new Intent(Intent.ACTION_MAIN);
//            it.addCategory(Intent.CATEGORY_LAUNCHER);
//            it.setClass(this, MainActivity.class);
//            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

            SharedPreferences sharedPreferences = getSharedPreferences("APPROVAL", Context.MODE_PRIVATE);
            String port = sharedPreferences.getString("PORT", "");
            String ip = sharedPreferences.getString("IP", "");
            String uname = sharedPreferences.getString("UserName", "");
            String upsw = sharedPreferences.getString("UserPasswrod", "");
            String url = "http://" + ip + ":" + port + "/Mobile/SearchApprove.aspx?username=" + uname +"&password=" + upsw;
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
//
//            String sessionid = sharedPreferences.getString("ASP.SessionId", "");
//            it.putExtra("ASP.NET_SessionId=", sessionid);
            // startActivity(it);
            // n.flags=Notification.FLAG_ONGOING_EVENT;


            //it.putExtra(KEY_COUNT, count);
            /*********************
             *获得PendingIntent
             *FLAG_CANCEL_CURRENT:
             *      如果当前系统中已经存在一个相同的PendingIntent对象，
             *      那么就将先将已有的PendingIntent取消，然后重新生成一个PendingIntent对象。
             *FLAG_NO_CREATE:
             *      如果当前系统中不存在相同的PendingIntent对象，
             *      系统将不会创建该PendingIntent对象而是直接返回null。
             *FLAG_ONE_SHOT:
             *      该PendingIntent只作用一次，
             *      如果该PendingIntent对象已经触发过一次，
             *      那么下次再获取该PendingIntent并且再触发时，
             *      系统将会返回一个SendIntentException，在使用这个标志的时候一定要注意哦。
             *FLAG_UPDATE_CURRENT:
             *      如果系统中已存在该PendingIntent对象，
             *      那么系统将保留该PendingIntent对象，
             *      但是会使用新的Intent来更新之前PendingIntent中的Intent对象数据，
             *      例如更新Intent中的Extras。这个非常有用，
             *      例如之前提到的，我们需要在每次更新之后更新Intent中的Extras数据，
             *      达到在不同时机传递给MainActivity不同的参数，实现不同的效果。
             *********************/
            PendingIntent pi = PendingIntent.getActivity(this, 0, it, 0);
            //设置事件信息，显示在拉开的里面
            n.setLatestEventInfo(NoticeService.this, "掌上审批系统", httpResult, pi);
            //发出通知
            //nm.cancel(1012);
            nm.notify(R.mipmap.mylogo, n);

    }
}
