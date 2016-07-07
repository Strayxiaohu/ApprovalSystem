package com.xiaohu.approval.approvalsystem.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xiaohu.approval.approvalsystem.R;

/**
 * Created by Administrator on 2016/7/2.
 */
public class SettingIpActivity extends Activity {
    EditText editTextIp, editTextPort;
    Button btnCommint, cancle;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setip);
        initView();
        initEvent();
    }

    private void initView() {
        sharedPreferences = getSharedPreferences("APPROVAL", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editTextIp = (EditText) findViewById(R.id.setting_ip);
        editTextPort = (EditText) findViewById(R.id.seting_port);
        btnCommint = (Button) findViewById(R.id.setting_http_submit);
        cancle = (Button) findViewById(R.id.setting_http_cancel);
        editTextPort.setText(sharedPreferences.getString("PORT", ""));
        editTextIp.setText(sharedPreferences.getString("IP", ""));

    }

    private void initEvent() {
        btnCommint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = editTextIp.getText().toString().trim();
                String port = editTextPort.getText().toString().trim();
                if (ip.equals("") || port.equals("")) {
                    Toast.makeText(SettingIpActivity.this, "请输入正确的IP和端口", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("IP", ip);
                    editor.putString("PORT", port);
                    editor.commit();
                    finish();
                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
