package com.example.zwm.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.zwm.myapplication.util.HttpUtils;
import com.example.zwm.myapplication.R;


public class InputPostActivity extends AppCompatActivity {
    private static final String ROOT_URL_PATH = "http://182.254.247.94:8080/KeyanWeb";
    private static final String PUSERVLET_URL_PATH = ROOT_URL_PATH + "/puservlet";

    private EditText inputText;
    private Button commitButton;
    private String postInfo;
    private String resultFromPU;
    private LinearLayout linearLayoutOfCD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_post);


        initValues();
        initEvents();

    }


    private void initValues() {
        inputText = (EditText) findViewById(R.id.m_input_text);
        commitButton = (Button) findViewById(R.id.commit_button);

    }


    private void initEvents() {
        inputText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
                if ((view.getId() == R.id.m_input_text && canVerticalScroll(inputText))) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });


        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    postInfo = "sents=" + inputText.getText().toString();

                    new Thread(){
                        @Override
                        public void run() {
                            // 将输入文本发送至服务器，取得分析结果
                            resultFromPU = HttpUtils.post(PUSERVLET_URL_PATH, postInfo);
                            // 将输入文本发送至服务器，取得分析结果

                            if (resultFromPU != null) {
                                // 将输入文本和分析结果传送至 DisplayActivity 活动中，并启动该活动
                                Intent intentForDisplayActivity = new Intent();
                                intentForDisplayActivity.setClass(InputPostActivity.this, DisplayActivity.class);
                                intentForDisplayActivity.putExtra("inputText", inputText.getText().toString());
                                intentForDisplayActivity.putExtra("resultFromPU", resultFromPU);
                                startActivity(intentForDisplayActivity);
                                // 将分析结果传送至 DisplayActivity 活动中，并启动该活动
                            }
                            else {
                                // 服务器出问题了
                                Log.d("MyDebug", "请求服务器出错");
                                // 服务器出问题了
                            }

                        }
                    }.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    } // initEvents()




    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }


}
