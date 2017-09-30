package com.example.zwm.myapplication.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zwm.myapplication.util.HttpUtils;
import com.example.zwm.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private static final String ROOT_URL_PATH = "http://182.254.247.94:8080/KeyanWeb";
    private static final String PUSERVLET_URL_PATH = ROOT_URL_PATH + "/puservlet";

    private EditText inputText;
    private TextView testHttp;
    private Button commitButton;
    private String postText;
    private String resultFromPU;
    private LinearLayout linearLayoutOfCD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputText = (EditText) findViewById(R.id.m_input_text);
        testHttp = (TextView) findViewById(R.id.test_http);
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

        linearLayoutOfCD = (LinearLayout) findViewById(R.id.container_of_cd);
        commitButton = (Button) findViewById(R.id.commit_button);
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 去掉分类标注部分的所有（上次）记录，以及用于展示的框框边线
                linearLayoutOfCD.removeAllViewsInLayout();
//                linearLayoutOfCD.setBackground(null);

                try {
                    postText = "sents=" + inputText.getText().toString();
                    new Thread(){
                        @Override
                        public void run() {
                            // 将输入文本发送至服务器，取得分析结果
                            resultFromPU = HttpUtils.post(PUSERVLET_URL_PATH, postText);
                            // 将输入文本发送至服务器，取得分析结果

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // for test 显示服务器返回的分类结果信息
                                    testHttp.setText(resultFromPU);
                                    // for test 显示服务器返回的分类结果信息

                                    if (!resultFromPU.equals("") && resultFromPU != null) {
                                        String[] partArr = resultFromPU.split("\\|");

                                        // 有负面信息
                                        if (partArr.length > 1) {
                                            if (!partArr[1].equals("")) {
//                                                Drawable pos_distribution_border = getResources().getDrawable(R.drawable.pos_distribution_border);
//                                                linearLayoutOfCD.setBackground(pos_distribution_border);
                                                String[] positionArr = partArr[1].split(" ");

                                                HashMap<Integer, TextView> textViewMapOfSents = new HashMap<Integer, TextView>();
                                                textViewMapOfSents.clear();

                                                // 将文本输入框中的文本分句,逐句处理。先按负例处理，跳过空句子，后由分析结果信息重新处理正例句子
                                                String[] sentArr = inputText.getText().toString().split("[。！？]");
                                                for (int i = 0; i < sentArr.length; i++) {
                                                    if (!sentArr[i].equals("")) {
                                                        TextView tv = new TextView(MainActivity.this);
//                                                        tv.setId(); // 怎么弄id？？？
                                                        tv.setWidth(linearLayoutOfCD.getWidth());
                                                        tv.setText(sentArr[i]);
                                                        tv.setTextColor(Color.parseColor("#000000"));
                                                        tv.setTextSize(16f);
                                                        tv.setBackgroundResource(R.drawable.neg_distribution_border);
                                                        textViewMapOfSents.put(i, tv);
                                                    }
                                                }

                                                // 为所有的正例句子添加底色
                                                for (int i = 0; i < positionArr.length; i++) {
                                                    String text = sentArr[ Integer.parseInt(positionArr[i]) ];
                                                    if (!text.equals("")) {
                                                        TextView tv = textViewMapOfSents.get( Integer.parseInt(positionArr[i]) );
                                                        tv.setBackgroundResource(R.drawable.pos_distribution_border);

//                                                        // 没到处理最后一个句子之前，都需要画一条分割线
//                                                        if (i < positionArr.length - 1) {
//                                                            TextView line = new TextView(MainActivity.this);
//                                                            line.setWidth(linearLayoutOfCD.getWidth());
//                                                            line.setHeight(1);
//                                                            line.setBackgroundColor(Color.parseColor("#000000"));
//                                                            linearLayoutOfCD.addView(line);
//                                                        }
                                                    }

                                                }
                                                Set<Integer> keySet = textViewMapOfSents.keySet();
                                                ArrayList<Integer> keyList = new ArrayList<Integer>(keySet);
                                                Collections.sort(keyList);
                                                for (int position : keyList) {
                                                    linearLayoutOfCD.addView(textViewMapOfSents.get(position));
                                                }
                                            }
                                        }


                                    }
                                    else {
                                        //
                                    }


                                }
                            });
                        }
                    }.start();

                } catch (Exception e) {
                    testHttp.setText(e.getMessage());
                }



            }
        });

    }

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
