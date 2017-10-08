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
import android.widget.ImageView;
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
    private ImageView backView;
    private LinearLayout tabCDLayout;
    private LinearLayout tabPULayout;
    private LinearLayout tabTransELayout;
    private LinearLayout cdLegendPos;
    private LinearLayout cdLegendNeg;
    private LinearLayout cdContainerView;

    private TextView puErrorView;
    private TextView transeErrorView;

    private WebView pieView;
    private WebView forceView;
    private LinearLayout pieViewLayout;
    private LinearLayout forceViewLayout;
    // view


    private ViewTreeObserver vto;

    private LinearLayout.LayoutParams layoutParamsOfcdContainer;
    private LinearLayout.LayoutParams layoutParamsOfViewLayout;

    private int cdItemHeignt;
    private List<TextView> cdTextViewList;
    private HashMap<Integer, TextView> cdTextViewMap;
    private String[] countArr;
    private String positionOfPos;
    private String[] positionArr;
    //    private List<TextView> cdShowList;

    private String forceVertexArr;
    private String forceArcArr;

    private String inputText;
    private String resultFromPU;

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

        backView = (ImageView) findViewById(R.id.display_back_view);

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
        puErrorView = (TextView) tabPUView.findViewById(R.id.pu_error_info);
        transeErrorView = (TextView) tabTransEView.findViewById(R.id.transe_error_info);

        pieView = (WebView) tabPUView.findViewById(R.id.pie_web_view);
        forceView = (WebView) tabTransEView.findViewById(R.id.force_web_view);

        cdLegendPos = (LinearLayout) tabCDView.findViewById(R.id.cd_legend_pos);
        cdLegendNeg = (LinearLayout) tabCDView.findViewById(R.id.cd_legend_neg);
        cdContainerView = (LinearLayout) tabCDView.findViewById(R.id.container_of_cd);
        pieViewLayout = (LinearLayout) tabPUView.findViewById(R.id.pie_web_view_layout);
        forceViewLayout = (LinearLayout) tabTransEView.findViewById(R.id.force_web_view_layout);
        // 初始化View

        cdTextViewMap = new HashMap<Integer, TextView>();
        cdTextViewList = new ArrayList<TextView>();
    }

    private void initEvents() {
//        // 去掉分类标注部分的所有（上次）记录
//        cdContainerView.removeAllViewsInLayout();
        backView.setOnClickListener(this);

        tabCDLayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));

        tabCDLayout.setOnClickListener(this);
        tabPULayout.setOnClickListener(this);
        tabTransELayout.setOnClickListener(this);

        cdLegendPos.setOnClickListener(this);
        cdLegendNeg.setOnClickListener(this);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //
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
                //
            }
        });

        try {
            if (resultFromPU != null && !resultFromPU.equals("null")) { // 处理成功且成功返回
                String[] partArr = resultFromPU.split("\\|");
                countArr = partArr[0].split(" ");
                positionOfPos = partArr[1];

                layoutParamsOfcdContainer = (LinearLayout.LayoutParams) cdContainerView.getLayoutParams();
                /**
                 * 将文本输入框中的文本分句,逐句处理。先按负例处理，跳过空句子，后由分析结果信息重新处理正例句子
                 */
                String[] sentArr = inputText.toString().split("[。！？]"); // 分句
                for (int i = 0; i < sentArr.length; i++) {
                    TextView tv = new TextView(DisplayActivity.this);
                    tv.setWidth(layoutParamsOfcdContainer.width); // 宽度
                    tv.setText(sentArr[i]); // 文本
                    tv.setTextColor(Color.parseColor("#000000")); // 文本颜色
                    tv.setTextSize(16f); // 文本字体大小
                    tv.setBackgroundResource(R.drawable.neg_distribution_border); // 背景色
                    cdTextViewList.add(tv);

//                        if (!sentArr[i].equals("")) { // 跳过空句子
//                            TextView tv = new TextView(DisplayActivity.this);
//                            tv.setWidth(layoutParamsOfcdContainer.width); // 宽度
//                            tv.setText(sentArr[i]); // 文本
//                            tv.setTextColor(Color.parseColor("#000000")); // 文本颜色
//                            tv.setTextSize(16f); // 文本字体大小
//                            tv.setBackgroundResource(R.drawable.neg_distribution_border); // 背景色
//                            cdTextViewMap.put(i, tv);cdTextViewList
//                        }
                }
                if (!positionOfPos.equals("")) { // 有负面信息
                    // 分类颜色标注
                    positionArr = positionOfPos.split(" ");

                    /**
                     * 对所有的正例句子重新处理，修改为正确的底色
                     */
                    for (int i = 0; i < positionArr.length; i++) {
                        int index = Integer.parseInt(positionArr[i]);
                        TextView tv = cdTextViewList.get(index);
                        tv.setBackgroundResource(R.drawable.pos_distribution_border);
                        cdTextViewList.remove(index);
                        cdTextViewList.add(index, tv);

//                            String text = sentArr[Integer.parseInt(positionArr[i])]; // 取出被分类为正例的一个句子文本
//                            if (!text.equals("")) { // 跳过空句子
//                                TextView tv = cdTextViewMap.get(Integer.parseInt(positionArr[i]));
//                                tv.setBackgroundResource(R.drawable.pos_distribution_border);
//                                cdTextViewMap.put(Integer.parseInt(positionArr[i]), tv);
//                            }
                    }
                }

                showAllSents();
//                    cdContainerView.removeAllViews();
//                    // 调整句子的显示顺序
//                    Set<Integer> keySet = cdTextViewMap.keySet();
//                    ArrayList<Integer> keyList = new ArrayList<Integer>(keySet);
//                    Collections.sort(keyList);
//                    for (int position : keyList) {
//                        cdContainerView.addView( cdTextViewMap.get(position) );
//                    }

                // 分类颜色标注

                /**
                 * 饼图
                 */
                layoutParamsOfViewLayout = (LinearLayout.LayoutParams) pieViewLayout.getLayoutParams();
                // 增加整体布局监听
                vto = pieViewLayout.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        layoutParamsOfViewLayout.height = (int) (pieViewLayout.getWidth() * 1.2); // 通过整体布局监听，获得View宽度
                    }
                });
                puErrorView.setHeight(0); // 隐藏错误信息显示区域
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
                            transeErrorView.setText("没有符合条件的对象-属性元组!");
                            Log.d("MyDebug", "[TransE] Error!");
                            return;
                        }
                        else { // 处理成功
                            String[] tempPairs = pairs[0].split(" ");
                            forceVertexArr = "[{category: 0, name: \'" + tempPairs[0] + "\', value: 20}, " +
                                            "{category: 1, name: \'" + tempPairs[1] + "\', value: 20}";
                            forceArcArr = "[{source: \'" + tempPairs[0] + "\', target: \'" + tempPairs[1] + "\', weight: 5}";
                            for (int i = 1; i < pairs.length; i++) { // 先将头尾实体点信息及之间弧信息整理到两个数组中
                                tempPairs = pairs[i].split(" ");
                                forceVertexArr += ", {category: 0, name: \'" + tempPairs[0] + "\', value: 20}, " +
                                        "{category: 1, name: \'" + tempPairs[1] + "\', value: 20}";
                                forceArcArr += ", {source: \'" + tempPairs[0] + "\', target: \'" + tempPairs[1] + "\', weight: 5}";
                            }
                            forceVertexArr += "]";
                            forceArcArr += "]";
                        }
                            Log.d("MyDebug", "组装完成：\nvertexArr: " + forceVertexArr + "\narcArr: " + forceArcArr);

                        runOnUiThread(new Runnable() { // 在UI主线程中执行显示View的操作
                            @Override
                            public void run() {
                                transeErrorView.setHeight(0); // 隐藏错误信息显示区域

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
            // 没有负面信息（吧）
            else {
                //
            }
        } catch (Exception e) {
            //
            Log.d("MyDebug", "网络有误！");
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        resetImage();
        switch (view.getId()) {
            case R.id.display_back_view:
                finish();
                break;
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
                break;
            case R.id.cd_legend_pos:

        }
    }

    private void resetImage() {
        tabCDLayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
        tabPULayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
        tabTransELayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
    }

    private void showAllSents() {
        cdContainerView.removeAllViews();
        // 调整句子的显示顺序
        for (int i = 0; i < cdTextViewList.size(); i++) {
            TextView tv = cdTextViewList.get(i);
            if (!tv.getText().equals("")) {
                cdContainerView.addView(tv);
            }
        }
    }

//    private void showPos() {
//        if (countArr[0].trim().equals("0")) { // 没有负面，移除所有View级就行了
//
//        }
//    }
//
//    private void showNeg() {
//
//    }
}
