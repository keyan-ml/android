package com.example.zwm.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.activity.DisplayActivity;
import com.example.zwm.myapplication.model.PublicVariable;
import com.example.zwm.myapplication.util.HttpUtils;
import com.example.zwm.myapplication.util.InputTextDBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InputPostFragment extends Fragment {
//    private final String URL_ROOT_PATH = "http://182.254.247.94:8080/KeyanWebBeta";
    private final String ROOT_URL_PATH = PublicVariable.URL_ROOT_PATH;
    private final String PUSERVLET_URL_PATH = ROOT_URL_PATH + "/puservlet";
    private final String TRANSESERVLET_URL_PATH = ROOT_URL_PATH + "/transeservlet";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private EditText inputTextView;
    private TextView errorInfoView;
    private Button commitButton;

    private String inputText;
    private String postInfo;
    private String positionOfPos;
    private String transESents;
    private String resultFromPU;
    private String resultFromTransE;
    private LinearLayout linearLayoutOfCD;

    private InputTextDBHelper dbHelper;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = getActivity();

        initViews(activity);
        initEvents(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_post, container, false);
    }

    private void initViews(FragmentActivity activity) {
        inputTextView = (EditText) activity.findViewById(R.id.m_input_text);
        errorInfoView = (TextView) activity.findViewById(R.id.input_post_error_info);
        commitButton = (Button) activity.findViewById(R.id.commit_button);

        dbHelper = new InputTextDBHelper(activity);
    }

    private void initEvents(final FragmentActivity activity) {
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    inputText = inputTextView.getText().toString();
                    if (inputText.equals("")) { // 输入为空
                        errorInfoView.setText("请输入文本！");
                        return;
                    }
                    errorInfoView.setText("正在分析，请稍候！");
                    postInfo = "myId=" + PublicVariable.sessionid +
                            "&sents=" + inputText;
                    SharedPreferences.Editor spEditor = activity.getSharedPreferences("UserInFo", Context.MODE_PRIVATE).edit();
                    spEditor.putString("inputtext", inputTextView.getText().toString()); // 保存当前用户最近一次输入的分析文本
                    spEditor.commit();

                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                resultFromPU = HttpUtils.post(PUSERVLET_URL_PATH, postInfo); // 将输入文本发送至服务器，取得分析结果
                                String[] partArr = resultFromPU.split("\\|");
                                positionOfPos = partArr[1];
                                String[] sents = inputText.trim().split("[。！？]");
                                String[] positions = positionOfPos.trim().split(" ");
                                transESents = "";
                                for (int i = 0; i < positions.length; i++) {
                                    String str = sents[ Integer.parseInt(positions[i]) ].trim();
                                    if (!str.equals("")) {
                                        transESents += str + "。";
                                    }
                                }
                                String postString = "myId=" + PublicVariable.sessionid +
                                        "&sents=" + transESents;
                                resultFromTransE = HttpUtils.post(TRANSESERVLET_URL_PATH, postString);

                                if (resultFromPU != null && resultFromTransE != null) { // 获得分析结果
                                    // 输入文本存入数据库
                                    SharedPreferences sp = activity.getSharedPreferences("UserInFo", Context.MODE_PRIVATE);
                                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                                    String sql = "insert into input_text values(null, ?, ?, ?);";
                                    db.execSQL(sql, new Object[]{
                                                        sp.getString("uemailaddress", ""),
                                                        inputTextView.getText().toString(),
                                                        new SimpleDateFormat(DATETIME_FORMAT).format(new Date()) });
                                    db.close();
                                    Log.d("MyDebug", "保存了");

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            errorInfoView.setText("");
                                        }
                                    });
                                    // 将输入文本和分析结果传送至 DisplayActivity 活动中，并启动该活动
                                    Intent intentForDisplayActivity = new Intent();
                                    intentForDisplayActivity.setClass(getActivity(), DisplayActivity.class);
//                                    intentForDisplayActivity.putExtra("inputText", inputTextView.getText().toString());
                                    intentForDisplayActivity.putExtra("resultFromPU", resultFromPU);
                                    intentForDisplayActivity.putExtra("resultFromTransE", resultFromTransE);
                                    startActivity(intentForDisplayActivity);
                                    // 将分析结果传送至 DisplayActivity 活动中，并启动该活动
                                }
                                else {
                                    // 服务器出问题了
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            errorInfoView.setText("服务器故障！");
                                        }
                                    });
                                    Log.d("MyDebug", "请求服务器出错");
                                    // 服务器出问题了
                                }
                            } catch (NumberFormatException e) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        errorInfoView.setText("处理出错！");
                                    }
                                });
                            } catch (Exception e) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        errorInfoView.setText("未知错误！");
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
