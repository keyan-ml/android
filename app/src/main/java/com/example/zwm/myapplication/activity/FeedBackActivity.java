package com.example.zwm.myapplication.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.util.HttpUtils;

public class FeedBackActivity extends AppCompatActivity {
    private static final String ROOT_URL_PATH = "http://182.254.247.94:8080/KeyanWeb";
    private final String FEEDBACKSERVLET_URL_PATH = ROOT_URL_PATH + "/feedbackservlet";
    // view
    private EditText feedInfoView;
    private TextView errorInfoView;
    private Button feedbackCommitBtn;
    // view

    private String uemailaddress;
    private String feedinfo;
    private String inputtext;

    private String postString;
    private String resultFromPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        initViews();
        initEvents();
    }

    private void initViews() {
        feedInfoView = (EditText) findViewById(R.id.feedback_info);
        feedbackCommitBtn = (Button) findViewById(R.id.feedback_commit);
        errorInfoView = (TextView) findViewById(R.id.feedback_error_info);
    }

    private void initEvents() {
        feedbackCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("UserInFo", Context.MODE_PRIVATE);
                uemailaddress = sp.getString("uemailaddress", "");
                feedinfo = feedInfoView.getText().toString();
                inputtext = sp.getString("inputtext", "");
                Log.d("MyDebug", uemailaddress + ": " + feedinfo);
                postString = "uemailaddress=" + uemailaddress
                            + "&feedinfo=" + feedinfo
                            + "&inputtext=" + inputtext;

                if (uemailaddress.equals("")) {
                    errorInfoView.setText("您的邮箱有误！");
                    return;
                }
                if (feedinfo.equals("")) {
                    errorInfoView.setText("请填入您的反馈信息！");
                    return;
                }

                new Thread(){
                    @Override
                    public void run() {
                        super.run();

                        resultFromPost = HttpUtils.post(FEEDBACKSERVLET_URL_PATH, postString);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resultFromPost.contains("failed")) { // 提交失败
                                    if (resultFromPost.contains("emailaddress")) {
                                        errorInfoView.setText("您的邮箱有误！");
                                        return;
                                    }
//                                    if (resultFromPost.contains("feedinfo")) {
//                                        errorInfoView.setText("请填入您的反馈信息！");
//                                        return;
//                                    }
                                    if (resultFromPost.contains("mysql")) {
                                        errorInfoView.setText("服务器故障，请联系网络管理员！");
                                        return;
                                    }
                                    errorInfoView.setText("未知错误！");
                                }
                                else {
                                    errorInfoView.setText("提交成功！");
                                }

                            }
                        });
                    }
                }.start();

            }
        });

    }

}
