package com.xiaohu.approval.approvalsystem.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xiaohu.approval.approvalsystem.R;
import com.xiaohu.approval.approvalsystem.http.HttpUtils;

/**
 * Created by Administrator on 2016/7/2.
 */
public class LoginActivity extends Activity {
    EditText edituser, editpsw;
    Button btnSave;
    ImageView ivSetting;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }

    private void initView() {
        sharedPreferences = getSharedPreferences("APPROVAL", Context.MODE_PRIVATE);
        String uname = sharedPreferences.getString("UserName", "");
        String upsw = sharedPreferences.getString("UserPasswrod", "");
        ivSetting = (ImageView) findViewById(R.id.login_setting_image);
        edituser = (EditText) findViewById(R.id.login_username);
        editpsw = (EditText) findViewById(R.id.login_psw_edit);
        btnSave = (Button) findViewById(R.id.login_button);
        edituser.setText(uname);
        editpsw.setText(upsw);
    }

    private void initEvent() {
        final Handler myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj.toString().equals("")) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("UserName",edituser.getText().toString().trim());
                    editor.putString("UserPasswrod",editpsw.getText().toString().trim());
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }
                System.out.println("*************" + msg.obj);
            }
        };
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SettingIpActivity.class);
                startActivity(intent);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String port = sharedPreferences.getString("PORT", "");
                String ip = sharedPreferences.getString("IP", "");
                if (port.equals("") || ip.equals("")) {
                    Toast.makeText(LoginActivity.this, "请设置IP和端口！", Toast.LENGTH_SHORT).show();
                } else if (edituser.getText().toString().trim().equals("") || editpsw.getText().toString().trim().equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入用户名和密码！", Toast.LENGTH_SHORT).show();
                } else {
                    HttpUtils httpUtils = new HttpUtils();
                    String url = "http://" + ip + ":" + port + "/Mobile/login.ashx?username=" + edituser.getText().toString().trim() + "&password=" + editpsw.getText().toString().trim();
                    httpUtils.HttpGet(LoginActivity.this, myHandler, url);
                }
            }
        });
    }
}
