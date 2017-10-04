package com.example.zwm.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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

public class SignInFragment extends Fragment {
    // view
    private EditText uemailaddressView;
    private EditText upasswordView;
    private TextView errorInfoView;
    private Button signInButton;
    // view

    private static final String SIGN_IN_URL = "http://182.254.247.94:8080/KeyanWeb/signinservlet";

    private String uemailaddress;
    private String upassword;

    private String resultFromPost;

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
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    private void initViews(FragmentActivity activity) {
        uemailaddressView = (EditText) activity.findViewById(R.id.sign_in_uemailaddress);
        upasswordView = (EditText) activity.findViewById(R.id.sign_in_upassword);
        errorInfoView = (TextView) activity.findViewById(R.id.sign_in_error_info);
        signInButton = (Button) activity.findViewById(R.id.sign_in_button);
    }

    private void initEvents() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                        String postString = "uemailaddress=" + uemailaddress
                                + "&upassword=" + upassword;
                        resultFromPost = HttpUtils.post(SIGN_IN_URL, postString);
                        Log.d("MyDebug", "[result]: " + resultFromPost);
                        // 返回信息中有"failed"，为出错，具体错误需再次判断
                        if (resultFromPost.contains("failed")) {

                            getActivity().runOnUiThread(new Runnable() {
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
                        }

                    }
                }.start();
            }
        });
    }
}
