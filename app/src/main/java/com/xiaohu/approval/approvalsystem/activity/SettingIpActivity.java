package com.xiaohu.approval.approvalsystem.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.xiaohu.approval.approvalsystem.R;

/**
 * Created by Administrator on 2016/7/2.
 */
public class SettingIpActivity extends Activity{
    EditText editTextIp,editTextPort;
    Button btnCommint,cancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setip);
        editTextIp= (EditText) findViewById(R.id.setting_ip);
        editTextPort= (EditText) findViewById(R.id.seting_port);
        btnCommint= (Button) findViewById(R.id.setting_http_submit);
        cancle= (Button) findViewById(R.id.setting_http_cancel);

    }
}
