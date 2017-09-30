package com.example.zwm.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zwm.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class DisplayActivity extends SwipeBackActivity {
    private String inputText;
    private String resultFromPU;
    private TextView testHttp;
    private LinearLayout linearLayoutOfCD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent = getIntent();
        inputText = intent.getStringExtra("inputText");
        resultFromPU = intent.getStringExtra("resultFromPU");
        testHttp = (TextView) findViewById(R.id.test_http);

        linearLayoutOfCD = (LinearLayout) findViewById(R.id.container_of_cd);
        // 去掉分类标注部分的所有（上次）记录，以及用于展示的框框边线
        linearLayoutOfCD.removeAllViewsInLayout();
//        linearLayoutOfCD.setBackgroundResources(null);

        // for test 显示服务器返回的分类结果信息
        testHttp.setText(resultFromPU);
        // for test 显示服务器返回的分类结果信息

        if (!resultFromPU.equals("") && resultFromPU != null) {
            String[] partArr = resultFromPU.split("\\|");

            // 有负面信息
            if (partArr.length > 1) {
                if (!partArr[1].equals("")) {
                    String[] positionArr = partArr[1].split(" ");

                    HashMap<Integer, TextView> textViewMapOfSents = new HashMap<Integer, TextView>();
                    textViewMapOfSents.clear();

                    // 将文本输入框中的文本分句,逐句处理。先按负例处理，跳过空句子，后由分析结果信息重新处理正例句子
                    String[] sentArr = inputText.toString().split("[。！？]");
                    for (int i = 0; i < sentArr.length; i++) {
                        if (!sentArr[i].equals("")) {
                            TextView tv = new TextView(DisplayActivity.this);
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
                        }
                    }
                    // 为所有的正例句子添加底色

                    // 调整所有句子的显示顺序
                    Set<Integer> keySet = textViewMapOfSents.keySet();
                    ArrayList<Integer> keyList = new ArrayList<Integer>(keySet);
                    Collections.sort(keyList);
                    for (int position : keyList) {
                        linearLayoutOfCD.addView(textViewMapOfSents.get(position));
                    }
                    // 调整所有句子的显示顺序
                }
            }


        }
        // 有负面信息
        else {
            //
        }
    }
}
