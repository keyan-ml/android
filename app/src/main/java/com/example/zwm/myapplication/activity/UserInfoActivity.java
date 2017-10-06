package com.example.zwm.myapplication.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.util.HttpUtils;

public class UserInfoActivity extends AppCompatActivity {
    private static final String MODIFYUSERINFO_SERVLET_URL = "http://182.254.247.94:8080/KeyanWeb/modifyuserinfoservlet";

    private Button wantEditView;
    private EditText unameEditor;
    private EditText uemailaddressEditor;
    private EditText uorganizationEditor;
    private EditText ucontactwayEditor;
    private TextView errorInfoView;

    private LinearLayout unameLayout;
    private LinearLayout uorganizationLayout;
    private LinearLayout ucontactwayLayout;

    private String uemailaddress;

    private String postString;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initViews();
        initEvents();

    }

    private void initViews() {
        wantEditView = (Button) findViewById(R.id.user_info_want_edit);
        errorInfoView = (TextView) findViewById(R.id.user_info_error_info);

        unameEditor = (EditText) findViewById(R.id.user_info_uname);
        uemailaddressEditor = (EditText) findViewById(R.id.user_info_uemailaddress);
        uorganizationEditor = (EditText) findViewById(R.id.user_info_uorganization);
        ucontactwayEditor = (EditText) findViewById(R.id.user_info_ucontactway);

        unameLayout = (LinearLayout) findViewById(R.id.user_info_uname_layout);
        uorganizationLayout = (LinearLayout) findViewById(R.id.user_info_uorganization_layout);
        ucontactwayLayout = (LinearLayout) findViewById(R.id.user_info_ucontactway_layout);

        new Thread(){
            @Override
            public void run() { // 与服务器交互，获取相应个人信息
                super.run();

                try {
                    SharedPreferences sp = getSharedPreferences("UserInFo", MODE_PRIVATE);
                    uemailaddress = sp.getString("uemailaddress", "");

                    postString = "postreason=get&uemailaddress=" + uemailaddress;
                    result = HttpUtils.post(MODIFYUSERINFO_SERVLET_URL, postString);
                    Log.d("MyDebug", result);
                    final String[] userInfoArr = result.split("\\|");
                    if (result.contains("failed")) { // 服务器mysql获取用户信息失败
                        Log.d("MyDebug", "mysqling on server is failed");
                    }
                    else { // 获取成功，设置对应显示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (userInfoArr[0].contains("success")) { // 获取成功
                                    unameEditor.setText(userInfoArr[1]);
                                    uemailaddressEditor.setText(uemailaddress);
                                    uorganizationEditor.setText(userInfoArr[2]);
                                    ucontactwayEditor.setText(userInfoArr[3]);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.instance, "无法连接服务器", Toast.LENGTH_SHORT);
                }
            }
        }.start();
    }

    private void initEvents() {


        wantEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MyDebug", "clicked! want to edit");

                if (wantEditView.getText().equals("修改")) { // 编辑状态
                    unameEditor.setFocusable(true);
                    unameEditor.setFocusableInTouchMode(true);
                    unameEditor.setCursorVisible(true);
                    unameLayout.setBackgroundResource(R.drawable.border_drawer);

//                    uemailaddressEditor.setFocusable(true);
//                    uemailaddressEditor.setFocusableInTouchMode(true);
//                    uemailaddressEditor.setCursorVisible(true);

                    uorganizationEditor.setFocusable(true);
                    uorganizationEditor.setFocusableInTouchMode(true);
                    uorganizationEditor.setCursorVisible(true);
                    uorganizationLayout.setBackgroundResource(R.drawable.border_drawer);

                    ucontactwayEditor.setFocusable(true);
                    ucontactwayEditor.setFocusableInTouchMode(true);
                    ucontactwayEditor.setCursorVisible(true);
                    ucontactwayLayout.setBackgroundResource(R.drawable.border_drawer);

                    wantEditView.setText("保存");
                }
                else { // 编辑完成，请求提交
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();

                            try {
                                SharedPreferences sp = getSharedPreferences("UserInFo", MODE_PRIVATE);
                                uemailaddress = sp.getString("uemailaddress", "");

                                postString = "postreason=modify"
                                        + "&uemailaddress=" + uemailaddress
                                        + "&uname=" + new String(unameEditor.getText().toString().getBytes(), "UTF-8")
                                        +"&uorganization=" + new String(uorganizationEditor.getText().toString().getBytes(), "UTF-8")
                                        +"&ucontactway=" + new String(ucontactwayEditor.getText().toString().getBytes(), "UTF-8");
                                result = HttpUtils.post(MODIFYUSERINFO_SERVLET_URL, postString);
                                Log.d("MyDebug", result);
//                                final String[] userInfoArr = result.split("\\|");
                                if (result.contains("failed")) { // 服务器mysql获取用户信息失败
                                    Log.d("MyDebug", "mysqling on server is failed");
//                                    Toast.makeText(getParent(), "服务器出错", Toast.LENGTH_SHORT);
                                    errorInfoView.setText("保存失败（服务器出错）");
                                }
                                else { // 修改成功，界面重回不可编辑状态
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(UserInfoActivity.this, "保存成功", Toast.LENGTH_SHORT);

                                            unameEditor.setFocusable(false);
                                            unameEditor.setFocusableInTouchMode(false);
                                            unameEditor.setCursorVisible(false);
                                            unameEditor.setBackgroundResource(R.drawable.edit_text_false);

//                                    uemailaddressEditor.setFocusable(false);
//                                    uemailaddressEditor.setFocusableInTouchMode(false);
//                                    uemailaddressEditor.setCursorVisible(false);

                                            uorganizationEditor.setFocusable(false);
                                            uorganizationEditor.setFocusableInTouchMode(false);
                                            uorganizationEditor.setCursorVisible(false);
                                            uorganizationEditor.setBackgroundResource(R.drawable.edit_text_false);

                                            ucontactwayEditor.setFocusable(false);
                                            ucontactwayEditor.setFocusableInTouchMode(false);
                                            ucontactwayEditor.setCursorVisible(false);
                                            ucontactwayEditor.setBackgroundResource(R.drawable.edit_text_false);

                                            wantEditView.setText("修改");
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.instance, "无法连接服务器", Toast.LENGTH_SHORT);
                            }
                        }
                    }.start();
                } // else
            } // onClick()
        });
    }
}
