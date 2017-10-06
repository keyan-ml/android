package com.example.zwm.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.util.HttpUtils;

import java.net.HttpRetryException;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    public static Activity instance;

    private static final String SIGN_IN_URL = "http://182.254.247.94:8080/KeyanWeb/signinservlet";
    private static final String MODIFYUSERINFO_SERVLET_URL = "http://182.254.247.94:8080/KeyanWeb/modifyuserinfoservlet";

    // view
    private EditText uemailaddressView;
    private EditText upasswordView;
    private TextView errorInfoView;
    private Button signInButton;
    private TextView modifyPasswordView;
    private TextView signUpView;
    // view


    private String uemailaddress;
    private String upassword;

    private String resultFromPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initViews();
        initEvents();
    }

    private void initViews() {
        instance = this;

        if (FirstWelcomeActivity.instance != null) {
            FirstWelcomeActivity.instance.finish();
            FirstWelcomeActivity.instance = null;
        }

        uemailaddressView = (EditText) findViewById(R.id.sign_in_uemailaddress);
        upasswordView = (EditText) findViewById(R.id.sign_in_upassword);
        errorInfoView = (TextView) findViewById(R.id.sign_in_error_info);
        signInButton = (Button) findViewById(R.id.sign_in_button);
        modifyPasswordView = (TextView) findViewById(R.id.sign_in_forget_password);
        signUpView = (TextView) findViewById(R.id.sign_in_go_sign_up);
    }

    private void initEvents() {
        signInButton.setOnClickListener(this);
        modifyPasswordView.setOnClickListener(this);
        signUpView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                // 登录按钮
                uemailaddress = uemailaddressView.getText().toString();
                upassword = upasswordView.getText().toString();

                // 邮箱地址不能为空
                String regEx = "[a-zA-Z_0-9]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
                if (uemailaddress.equals("")) {
                    errorInfoView.setText("邮箱地址不能为空!");
                    return;
                }
                // 邮箱地址不符合规则
                else if (!uemailaddress.matches(regEx)) {
                    errorInfoView.setText("邮箱地址格式不正确!");
                    return;
                }

                // 密码不能为空
                if (upassword.equals("")) {
                    errorInfoView.setText("密码不能为空!");
                    return;
                }

                // 合法邮箱和密码，准备登录检测
                new Thread(){
                    @Override
                    public void run() {
                        super.run();

                        try {
                            String postString = "uemailaddress=" + uemailaddress
                                    + "&upassword=" + upassword;
                            resultFromPost = HttpUtils.post(SIGN_IN_URL, postString);
                            Log.d("MyDebug", "[result]: " + resultFromPost);
                            // 返回信息中有"failed"，为出错，具体错误需再次判断
                            if (resultFromPost == null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        errorInfoView.setText("网络连接错误！");
                                        return;
                                    }
                                });
                            }
                            if (resultFromPost.contains("failed")) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                    String[] resultArr = resultFromPost.split("_");
                                        if (resultFromPost.contains("emailaddress")) {
                                            errorInfoView.setText("该邮箱未注册！");
                                            return;
                                        }
                                        if (resultFromPost.contains("password")) {
                                            errorInfoView.setText("密码错误！");
                                            return;
                                        }

                                    }
                                });

                            }
                            // 返回信息中不含有"failed"字样，说明登录成功
                            else {
                                // 获取该邮箱所对应的个人信息
                                postString = "postreason=get&uemailaddress=" + uemailaddress;
                                resultFromPost = HttpUtils.post(MODIFYUSERINFO_SERVLET_URL, postString);
                                Log.d("MyDebug", resultFromPost);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (resultFromPost.contains("failed")) { // 服务器mysql获取用户信息失败
                                            errorInfoView.setText("网络有误！");
                                            Log.d("MyDebug", "mysqling on server is failed");
                                        }
                                        else { // 获取成功，设置对应显示
                                            final String[] userInfoArr = resultFromPost.split("\\|");
                                            if (userInfoArr[0].contains("success")) { // 获取成功
                                                SharedPreferences.Editor spEditor = getSharedPreferences("UserInFo", Context.MODE_PRIVATE).edit();
                                                spEditor.putString("uname", userInfoArr[1]);
                                                spEditor.putString("uemailaddress", uemailaddress);
                                                spEditor.putString("upassword", upassword);
                                                spEditor.putString("uorganization", userInfoArr[2]);
                                                spEditor.putString("ucontactway", userInfoArr[3]);
                                                spEditor.commit();

                                                Intent intent = new Intent();
                                                intent.setClass(SignInActivity.this, MainActivity.class);
                                                startActivity(intent);

                                            }
                                            else {
                                                errorInfoView.setText("网络有误！");
                                            }
                                        }
                                    }
                                });

//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        SharedPreferences.Editor spEditor = getSharedPreferences("UserInFo", Context.MODE_PRIVATE).edit();
//                                        spEditor.putString("uemailaddress", uemailaddress);
//                                        spEditor.putString("upassword", upassword);
//                                        spEditor.commit();
//
//                                        Intent intent = new Intent();
//                                        intent.setClass(SignInActivity.this, MainActivity.class);
//                                        startActivity(intent);
//                                    }
//                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
                break;
            case R.id.sign_in_forget_password:
                Intent intent = new Intent();
                intent.setClass(this, ModifyPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_in_go_sign_up:
                Intent intentSignUp = new Intent();
                intentSignUp.setClass(this, SignUpActivity.class);
                startActivity(intentSignUp);
                break;
        }
    }

}
