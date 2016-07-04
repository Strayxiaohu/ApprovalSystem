package com.xiaohu.approval.approvalsystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xiaohu.approval.approvalsystem.R;

/**
 * Created by Administrator on 2016/7/2.
 */
public class LoginActivity extends Activity {
    EditText edituser, editpsw;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }

    private void initView() {
        edituser = (EditText) findViewById(R.id.login_username);
        editpsw = (EditText) findViewById(R.id.login_psw_edit);
        btnSave = (Button) findViewById(R.id.login_button);
    }
    private void initEvent(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
