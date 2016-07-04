package com.xiaohu.approval.approvalsystem.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

                editor.putString("IP", editTextIp.getText().toString().trim());
                editor.putString("PORT", editTextPort.getText().toString().trim());
                editor.commit();
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
