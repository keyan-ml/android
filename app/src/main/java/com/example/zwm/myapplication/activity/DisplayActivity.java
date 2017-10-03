package com.example.zwm.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.util.HttpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DisplayActivity extends AppCompatActivity implements View.OnClickListener {
    private final String ROOT_URL_PATH = "http://182.254.247.94:8080/KeyanWeb";
    private final String TRANSESERVLET_URL_PATH = ROOT_URL_PATH + "/transeservlet";
    private final String TAB_NORMAL_COLOR = "#bbe4fb";
    private final String TAB_SELECTED_COLOR = "#6A5ACD";

    private ViewPager viewPager;
    private List<View> viewList = new ArrayList<View>();;
    private PagerAdapter pagerAdapter;

    // view
    private LinearLayout tabCDLayout;
    private LinearLayout tabPULayout;
    private LinearLayout tabTransELayout;
    private LinearLayout cdContainerView;
    private WebView pieView;
    private WebView forceView;
    private LinearLayout pieViewLayout;
    private LinearLayout forceViewLayout;
    // view

    private ViewTreeObserver vto;
//    private int textViewWidth;

    private LinearLayout.LayoutParams layoutParamsOfcdContainer;
    private LinearLayout.LayoutParams layoutParamsOfViewLayout;

    private String inputText;
    private String resultFromPU;
    private String positionOfPos;
    private String forceVertexArr;
    private String forceArcArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        initViews();
        initEvents();

    }

    private void initViews() {
        // 获取参数
        Intent intent = getIntent();
        inputText = intent.getStringExtra("inputText");
        resultFromPU = intent.getStringExtra("resultFromPU");
        // 获取参数

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabCDLayout = (LinearLayout) findViewById(R.id.tab_cd);
        tabPULayout = (LinearLayout) findViewById(R.id.tab_pu);
        tabTransELayout = (LinearLayout) findViewById(R.id.tab_transe);

        LayoutInflater inflater = getLayoutInflater(); // 将layout文件映射为view对象
        View tabCDView = inflater.inflate(R.layout.tab_cd, null);
        View tabPUView = inflater.inflate(R.layout.tab_pu, null);
        View tabTransEView = inflater.inflate(R.layout.tab_transe, null);

        viewList.add(tabCDView);
        viewList.add(tabPUView);
        viewList.add(tabTransEView);

        // 创建adapter
        pagerAdapter = new PagerAdapter() {
            // 手动重写初始化和销毁这两个方法
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position)); // 从container中移除
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = viewList.get(position);
                container.addView(view); // 加入到container中
                return view;
            }
            // 手动重写初始化和销毁这两个方法

            @Override
            public int getCount() {
                return viewList.size(); // 返回个数
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object; // 这个就母鸡了
            }
        };

        try {
            viewPager.setAdapter(pagerAdapter); // 给ViewPager添加adapter
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 初始化View
        pieView = (WebView) tabPUView.findViewById(R.id.pie_web_view);
        forceView = (WebView) tabTransEView.findViewById(R.id.force_web_view);

        cdContainerView = (LinearLayout) tabCDView.findViewById(R.id.container_of_cd);
        pieViewLayout = (LinearLayout) tabPUView.findViewById(R.id.pie_web_view_layout);
        forceViewLayout = (LinearLayout) tabTransEView.findViewById(R.id.force_web_view_layout);
        // 初始化View
    }

    private void initEvents() {
//        // 去掉分类标注部分的所有（上次）记录
//        cdContainerView.removeAllViewsInLayout();
        tabCDLayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));

        tabCDLayout.setOnClickListener(this);
        tabPULayout.setOnClickListener(this);
        tabTransELayout.setOnClickListener(this);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetImage();
                switch (position) {
                    case 0:
                        tabCDLayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                        break;
                    case 1:
                        tabPULayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                        break;
                    case 2:
                        tabTransELayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (resultFromPU != null) { // 什么情况？？
            String[] partArr = resultFromPU.split("\\|");

            // 有负面信息
            if (partArr.length > 1) { // 该判断好像是多余的
                positionOfPos = partArr[1];

                if (!positionOfPos.equals("")) {
                    // 分类颜色标注
                    String[] positionArr = positionOfPos.split(" ");
                    HashMap<Integer, TextView> textViewMapOfSents = new HashMap<Integer, TextView>();

                    layoutParamsOfcdContainer = (LinearLayout.LayoutParams) cdContainerView.getLayoutParams();
                    /**
                     * 将文本输入框中的文本分句,逐句处理。先按负例处理，跳过空句子，后由分析结果信息重新处理正例句子
                     */
                    String[] sentArr = inputText.toString().split("[。！？]"); // 分句
                    for (int i = 0; i < sentArr.length; i++) {
                        if (!sentArr[i].equals("")) { // 跳过空句子
                            TextView tv = new TextView(DisplayActivity.this);
                            tv.setWidth(layoutParamsOfcdContainer.width); // 宽度
                            tv.setText(sentArr[i]); // 文本
                            tv.setTextColor(Color.parseColor("#000000")); // 文本颜色
                            tv.setTextSize(16f); // 文本字体大小
//                            tv.setBackgroundColor(Color.parseColor("#BFEFFF")); // 还没调试
                            tv.setBackgroundResource(R.drawable.neg_distribution_border); // 背景色
                            textViewMapOfSents.put(i, tv);
                        }
                    }

                    /**
                     * 对所有的正例句子重新处理，修改为正确的底色
                     */
                    for (int i = 0; i < positionArr.length; i++) {
                        String text = sentArr[ Integer.parseInt(positionArr[i]) ]; // 取出被分类为正例的一个句子文本
                        if (!text.equals("")) { // 跳过空句子
                            TextView tv = textViewMapOfSents.get( Integer.parseInt(positionArr[i]) );
                            tv.setBackgroundResource(R.drawable.pos_distribution_border);
                            textViewMapOfSents.put( Integer.parseInt(positionArr[i]), tv );
                        }
                    }
                    // 对所有的正例句子重新处理，修改为正确的底色

                    // 调整句子的显示顺序
                    Set<Integer> keySet = textViewMapOfSents.keySet();
                    ArrayList<Integer> keyList = new ArrayList<Integer>(keySet);
                    Collections.sort(keyList);
                    for (int position : keyList) {
                        cdContainerView.addView( textViewMapOfSents.get(position) );
                    }
                    // 调整句子的显示顺序
                    // 分类颜色标注


                    /**
                     * 饼图
                     */
                    final String[] countArr = partArr[0].split(" ");
                    layoutParamsOfViewLayout = (LinearLayout.LayoutParams) pieViewLayout.getLayoutParams();
                    // 增加整体布局监听
                    vto = pieViewLayout.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            layoutParamsOfViewLayout.height = (int) (pieViewLayout.getWidth() * 1.2); // 通过整体布局监听，获得View宽度

//                            Log.d("MyDebug", "width: " + pieViewLayout.getWidth() + ", height: " + pieViewLayout.getHeight());
                        }
                    });

                    pieViewLayout.setLayoutParams( layoutParamsOfViewLayout ); // 设置Layout布局参数
                    pieView.getSettings().setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
                    pieView.loadUrl("file:///android_asset/pie.html"); // 加载需要显示的网页
                    //设置Web视图
                    pieView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);

                            //在这里执行你想调用的js函数
                            pieView.post(new Runnable() {
                                String callJs = "javascript:showpie([{value: " + countArr[0] + ",name: '负面', selected:false},{value: " + countArr[1] + ",name: '正面'}])";

                                public void run() {
                                    pieView.loadUrl(callJs); // 执行js语句
                                }
                            });
                        }

                    });
                    // 饼图

                    /**
                     * 细粒度。需要向服务器发送TransE处理请求
                     */
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();

                            // 请求参数
                            String postString = "positions=" + positionOfPos
                                    + "&sents=" + inputText;

                            String result = HttpUtils.post(TRANSESERVLET_URL_PATH, postString); // 获取服务器返回信息
                            String[] pairs = result.split("\\|");

                            if (pairs[0].equals("ERROR")) { // 服务器处理出错
                                Log.d("MyDebug", "[TransE] Error!");
                                return;
                            }
                            else { // 处理成功
                                String[] vertexArr = new String[2 * pairs.length];
                                String[] arcArr = new String[pairs.length];
                                for (int i = 0; i < pairs.length; i++) { // 先将头尾实体点信息及之间弧信息整理到两个数组中
                                    String[] tempPairs = pairs[i].split(" ");
                                    vertexArr[2 * i] = "{category: 0, "
                                                    + "name: \'" + tempPairs[0] + "\', "
                                                    + "value: 20}";
                                    vertexArr[2 * i + 1] = "{category: 1, "
                                                        + "name: \'" + tempPairs[1] + "\', "
                                                        + "value: 20}";
                                    if (i == 0) {
                                        arcArr[i] = "{source: \'" + tempPairs[0] + "\', "
                                                + "target: \'" + tempPairs[1] + "\', "
                                                + "weight: 5}";
                                    }
                                    else {
                                        arcArr[i] = "{source: \'" + tempPairs[0] + "\', "
                                                + "target: \'" + tempPairs[1] + "\', "
                                                + "weight: 1}";
                                    }
                                }
                                forceVertexArr = "[" + vertexArr[0]; // 组装实体点信息
                                for (int i = 1; i < vertexArr.length; i++) {
                                    forceVertexArr += ", " + vertexArr[i];
                                }
                                forceVertexArr += "]";
                                forceArcArr = "[" + arcArr[0]; // 组装实体弧信息
                                for (int i = 1; i < arcArr.length; i++) {
                                    forceArcArr += ", " + arcArr[i];
                                }
                                forceArcArr += "]";
//                                Log.d("MyDebug", "vertexArr: " + forceVertexArr + "\narcArr: " + forceArcArr);
                            }

                            runOnUiThread(new Runnable() { // 在UI主线程中执行显示View的操作
                                @Override
                                public void run() {
                                    forceViewLayout.setLayoutParams( layoutParamsOfViewLayout ); // 设置Layout布局参数，主要设置高度值
                                    forceView.getSettings().setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
                                    forceView.loadUrl("file:///android_asset/force.html"); // 加载需要显示的网页

                                    forceView.setWebViewClient(new WebViewClient() {//设置Web视图
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            super.onPageFinished(view, url);

                                            // 在这里执行你想调用的js函数
                                            forceView.post(new Runnable() {
                                                String callJs = "javascript:showforce(" + forceVertexArr + ", " + forceArcArr + ")";

                                                public void run() {
                                                    forceView.loadUrl(callJs); // 执行js语句，显示force图
                                                }
                                            });
                                        }

                                    });
                                }
                            });

                        }
                    }.start();
                    // 细粒度

                }
            }


        }
        // 没有负面信息（吧）
        else {
            //
        }
    }

    @Override
    public void onClick(View view) {
        resetImage();
        switch (view.getId()) {
            case R.id.tab_cd:
                tabCDLayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                viewPager.setCurrentItem(0);
                break;
            case R.id.tab_pu:
                tabPULayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                viewPager.setCurrentItem(1);
                break;
            case R.id.tab_transe:
                tabTransELayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                viewPager.setCurrentItem(2);
        }
    }

    private void resetImage() {
        tabCDLayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
        tabPULayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
        tabTransELayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
    }
}
