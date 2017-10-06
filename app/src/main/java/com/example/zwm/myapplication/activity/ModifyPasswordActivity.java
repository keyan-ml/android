package com.example.zwm.myapplication.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.util.HttpUtils;

public class ModifyPasswordActivity extends AppCompatActivity {
    public static Activity instance;

    private static final String MODIFYUSERINFO_SERVLET_URL = "http://182.254.247.94:8080/KeyanWeb/modifyuserinfoservlet";

    private EditText uemailaddressEditor;
    private EditText passwordEditor;
    private EditText passwordConfirmedEditor;
    private TextView errorInfoView;
    private Button modifyButton;

    private String uemailaddress;
    private String newPassword;
    private String newPasswordConfirmed;

    private String postString;
    private String resultFromPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);

        initViews();
        initEvents();
    }

    private void initViews() {
        instance = this;

        uemailaddressEditor = (EditText) findViewById(R.id.modify_password_uemailaddress);
        passwordEditor = (EditText) findViewById(R.id.modify_password_upassword);
        passwordConfirmedEditor = (EditText) findViewById(R.id.modify_password_confirmed);
        errorInfoView = (TextView) findViewById(R.id.modify_password_error_info);
        modifyButton = (Button) findViewById(R.id.modify_password_button);

        uemailaddress = "";
        newPassword = "";
        newPasswordConfirmed = "";
    }

    private void initEvents() {
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uemailaddress = uemailaddressEditor.getText().toString();
                newPassword = passwordEditor.getText().toString();
                newPasswordConfirmed = passwordConfirmedEditor.getText().toString();
                if (uemailaddress == null || uemailaddress.equals("")) {
                    errorInfoView.setText("请输入您的注册邮箱地址！");
                    return;
                }
                if (newPassword == null || newPassword.equals("")) {
                    errorInfoView.setText("请输入新密码！");
                    return;
                }
                if (newPasswordConfirmed == null || newPasswordConfirmed.equals("")) {
                    errorInfoView.setText("请再次输入新密码！");
                    return;
                }
                if (!newPassword.equals(newPasswordConfirmed)) {
                    errorInfoView.setText("两次输入不一致！");
                    return;
                }

                postString = "postreason=upassword"
                        + "&uemailaddress=" + uemailaddress
                        + "&upassword=" + newPassword;

                new Thread(){
                    @Override
                    public void run() {
                        super.run();

                        try {
                            resultFromPost = HttpUtils.post(MODIFYUSERINFO_SERVLET_URL, postString);
                            Log.d("MyDebug", resultFromPost);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (resultFromPost.contains("failed")) { // 失败
                                        errorInfoView.setText("连接服务器失败！");
                                    }
                                    else { // 成功
                                        errorInfoView.setText("修改成功！");
                                    }
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });
    }
}
