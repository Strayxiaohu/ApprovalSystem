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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xiaohu.approval.approvalsystem.R;
import com.xiaohu.approval.approvalsystem.http.HttpUtils;
import com.xiaohu.approval.approvalsystem.service.NoticeService;

/**
 * Created by Administrator on 2016/7/2.
 */
public class LoginActivity extends Activity {
    EditText edituser, editpsw;
    Button btnSave;
    ImageView ivSetting;
    SharedPreferences sharedPreferences;
    CheckBox checkBox;

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
        checkBox = (CheckBox) findViewById(R.id.login_checkbox);
        checkBox.setChecked(true);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("PASSWORD", "true");
//        editor.commit();
        if(uname.equals("")&&upsw.equals("")){
            editpsw.setEnabled(true);
            edituser.setEnabled(true);
            btnSave.setText("登  录");
        }else{
            editpsw.setEnabled(false);
            edituser.setEnabled(false);
            btnSave.setText("注  销");
        }
        edituser.setText(uname);
        editpsw.setText(upsw);
    }

    private void initEvent() {
        //绑定监听器
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//                // TODO Auto-generated method stub
//               // Toast.makeText(LoginActivity.this,
//                //        arg1?"选中了":"取消了选中"    , Toast.LENGTH_LONG).show();
//                SharedPreferences.Editor editor=sharedPreferences.edit();
//                if(checkBox.isChecked()){
//                    checkBox.setChecked(false);
//                    editor.putString("PASSWORD","false");
//                }else{
//                    checkBox.setChecked(true);
//                    editor.putString("PASSWORD","true");
//                }
//
//                editor.commit();
//            }
//        });
        final Handler myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj.toString().equals("")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("UserName", edituser.getText().toString().trim());
                 //   if (checkBox.isChecked()) {
                        editor.putString("UserPasswrod", editpsw.getText().toString().trim());
//                    } else {
//                        editor.putString("UserPasswrod", "");
//                    }
                    editor.commit();
//                    Intent intents = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intents);
//                    finish();
                    Intent intent = new Intent(LoginActivity.this, NoticeService.class);
                    System.out.println("  開啟  service...");
                    startService(intent);
                    btnSave.setText("注  销");
                    editpsw.setEnabled(false);
                    edituser.setEnabled(false);
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
                if (btnSave.getText().equals("登  录")) {


                    if (port.equals("") || ip.equals("")) {
                        Toast.makeText(LoginActivity.this, "请设置IP和端口！", Toast.LENGTH_SHORT).show();
                    } else if (edituser.getText().toString().trim().equals("") || editpsw.getText().toString().trim().equals("")) {
                        Toast.makeText(LoginActivity.this, "请输入用户名和密码！", Toast.LENGTH_SHORT).show();
                    } else {
                        HttpUtils httpUtils = new HttpUtils();
                        String url = "http://" + ip + ":" + port + "/Mobile/login.ashx?username=" + edituser.getText().toString().trim() + "&password=" + editpsw.getText().toString().trim();
                        httpUtils.HttpGet(LoginActivity.this, myHandler, url, true);
                    }
                } else {
                    btnSave.setText("登  录");
                    editpsw.setEnabled(true);
                    edituser.setEnabled(true);
                    editpsw.setText("");
                    edituser.setText("");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("UserName", "");
                    editor.putString("UserPasswrod", "");
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, NoticeService.class);
                    System.out.println("  关闭  service...");
                    stopService(intent);
                }
            }
        });
    }
}
