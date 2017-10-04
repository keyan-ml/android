package com.example.zwm.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.activity.MainActivity;
import com.example.zwm.myapplication.util.HttpUtils;

public class SignUpFragment extends Fragment {
    // view
    private EditText unameView;
    private EditText uemailaddressView;
    private EditText upasswordView;
    private EditText upasswordConfirmedView;
    private EditText uorganizationView;
    private EditText ucontactwayView;
    private TextView errorInfoView;
    private Button signUpButton;
    // view

    private static final String SIGN_UP_URL = "http://182.254.247.94:8080/KeyanWeb/signupservlet";

    private String uname;
    private String uemailaddress;
    private String upassword;
    private String upasswordconfirmed;
    private String uorganization;
    private String ucontactway;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = getActivity();

        initViews(activity);
        initEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    private void initViews(FragmentActivity activity) {
        unameView = (EditText) activity.findViewById(R.id.sign_up_uname);
        uemailaddressView = (EditText) activity.findViewById(R.id.sign_up_uemailaddress);
        upasswordView = (EditText) activity.findViewById(R.id.sign_up_upassword);
        upasswordConfirmedView = (EditText) activity.findViewById(R.id.sign_up_uemailaddress_confirmed);
        uorganizationView = (EditText) activity.findViewById(R.id.sign_up_uorganization);
        ucontactwayView = (EditText) activity.findViewById(R.id.sign_up_ucontactway);
        errorInfoView = (TextView) activity.findViewById(R.id.sign_up_error_info);
        signUpButton = (Button) activity.findViewById(R.id.sign_up_button);
    }

    private void initEvents() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MyDebug", "Clicked");
                try {
                    uname = unameView.getText().toString();
                    uemailaddress = uemailaddressView.getText().toString();
                    upassword = upasswordView.getText().toString();
                    upasswordconfirmed = upasswordConfirmedView.getText().toString();
                    uorganization = uorganizationView.getText().toString();
                    ucontactway = ucontactwayView.getText().toString();

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

                    // 确认密码不能为空
                    if (upasswordconfirmed.equals("")) {
                        errorInfoView.setText("请再次填写密码!");
                        return;
                    }

                    // 两次密码必须一致
                    if (!upassword.equals(upasswordconfirmed)) {
                        errorInfoView.setText("两次密码必须一致!");
                        return;
                    }

                    // 单位不能为空
                    if (uorganization.equals("")) {
                        errorInfoView.setText("单位不能为空!");
                        return;
                    }

                    // 联系方式不能为空
                    if (ucontactway.equals("")) {
                        errorInfoView.setText("联系方式不能为空!");
                        return;
                    }

                    // 没有以上问题（通过所有判断），现在可以上传至服务器，进行最后一道判断：邮箱是否已存在（即是否可用）
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();

                            String postString = "uname=" + uname
                                    + "&uemailaddress=" + uemailaddress
                                    + "&upassword=" + upassword
                                    + "&uorganization=" + uorganization
                                    + "&ucontactway=" + ucontactway;

                            String result = HttpUtils.post(SIGN_UP_URL, postString);
                            Log.d("MyDebug", "[result]: " + result);
                            // 返回信息为"failed"，说明邮箱已存在，作提示
                            if (result.contains("failed")) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        uemailaddressView.setText("该邮箱已存在!");
                                        uemailaddressView.setTextColor(Color.parseColor("#ff0000"));
                                    }
                                });
                            }
                            // 返回信息不是"failed"，即"success"，本次注册成功，随后跳转到功能界面
                            else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferences.Editor spEditor = getActivity().getSharedPreferences("UserInFo", Context.MODE_PRIVATE).edit();
                                        spEditor.putString("uemailaddress", uemailaddress);
                                        spEditor.putString("upassword", upassword);
                                        spEditor.commit();

                                        Intent intent = new Intent();
                                        intent.setClass(getActivity(), MainActivity.class);
                                        getActivity().startActivity(intent);
                                    }
                                });
                            } // else
                        } // run()
                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("MyDebug", "完毕");
            }
        });
    }
}
