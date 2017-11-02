package com.example.zwm.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.util.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayActivity extends AppCompatActivity implements View.OnClickListener {
    private final String ROOT_URL_PATH = "http://182.254.247.94:8080/KeyanWeb";
    private final String TRANSESERVLET_URL_PATH = ROOT_URL_PATH + "/transeservlet";
    private final String TAB_NORMAL_COLOR = "#bbe4fb";
    private final String TAB_SELECTED_COLOR = "#6A5ACD";
//    private final String CD_LEGEND_NOT_SELECTED_COLOR = "#DDDDDD";
//    private final String CD_LEGEND_POS_SELECTED_COLOR = "#FF9192";
//    private final String CD_LEGEND_NEG_SELECTED_COLOR = "#BFEFFF";

    private ViewPager viewPager;
    private List<View> viewList = new ArrayList<View>();
    private PagerAdapter pagerAdapter;

    // view
    private ImageView backView;
    private LinearLayout tabCDLayout;
    private LinearLayout tabPULayout;
    private LinearLayout tabTransELayout;
    private LinearLayout tabReportLayout;
    private LinearLayout cdLegendPos;
    private LinearLayout cdLegendNeg;

    private ListView listView;
    private List<Map<String, String>> list;
    private CdItemAdapter cdItemAdapter;
    private boolean posHasShowed;
    private boolean negHasShowed;

    private TextView puErrorView;
    private TextView transeErrorView;

    private WebView pieView;
    private WebView forceView;
    private LinearLayout pieViewLayout;
    private LinearLayout forceViewLayout;
    private TextView reportView;
    // view


    private ViewTreeObserver vto;

    private LinearLayout.LayoutParams layoutParamsOfViewLayout;

    private String[] countArr;
    private String positionOfPos;
    private String[] positionArr;

    private String forceVertexArr;
    private String forceArcArr;

    private String inputText;
    private String resultFromPU;
    private String[] sentArr;
    private boolean transESuccess;

    private int countPos;
    private int countAll;
    private int countTransE;

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
        sentArr = inputText.toString().split("[。！？]"); // 分句
        resultFromPU = intent.getStringExtra("resultFromPU");
        // 获取参数

        backView = (ImageView) findViewById(R.id.display_back_view);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabCDLayout = (LinearLayout) findViewById(R.id.tab_cd);
        tabPULayout = (LinearLayout) findViewById(R.id.tab_pu);
        tabTransELayout = (LinearLayout) findViewById(R.id.tab_transe);
        tabReportLayout = (LinearLayout) findViewById(R.id.tab_report);

        LayoutInflater inflater = getLayoutInflater(); // 将layout文件映射为view对象
        View tabCDView = inflater.inflate(R.layout.tab_cd, null);
        View tabPUView = inflater.inflate(R.layout.tab_pu, null);
        View tabTransEView = inflater.inflate(R.layout.tab_transe, null);
        View tabReportView = inflater.inflate(R.layout.tab_report, null);

        viewList.add(tabCDView);
        viewList.add(tabPUView);
        viewList.add(tabTransEView);
        viewList.add(tabReportView);

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
        listView = (ListView) tabCDView.findViewById(R.id.container_of_cd);
        list = new ArrayList<Map<String, String>>();

        pieViewLayout = (LinearLayout) tabPUView.findViewById(R.id.pie_web_view_layout);
        forceViewLayout = (LinearLayout) tabTransEView.findViewById(R.id.force_web_view_layout);
        reportView = (TextView) tabReportView.findViewById(R.id.report_content);
        // 初始化View

        posHasShowed = true;
        negHasShowed = true;
    }

    private void initEvents() {
        backView.setOnClickListener(this);

        tabCDLayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));

        tabCDLayout.setOnClickListener(this);
        tabPULayout.setOnClickListener(this);
        tabTransELayout.setOnClickListener(this);
        tabReportLayout.setOnClickListener(this);

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
                        break;
                    case 3:
                        tabReportLayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
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
                countPos = Integer.parseInt(countArr[0].trim());
                countAll = countPos + Integer.parseInt(countArr[1].trim());

                showAll();
//                // 分类标注
//                String[] sentArr = inputText.toString().split("[。！？]"); // 分句
//                for (int i = 0; i < sentArr.length; i++) {
//                    Map<String, String> map = new HashMap<String, String>();
//                    map.put("type", "neg");
//                    map.put("text", sentArr[i]);
//                    list.add(map);
//                }
//                if (!positionOfPos.equals("")) { // 有负面信息
//                    positionArr = positionOfPos.split(" ");
//                    for (int i = 0; i < positionArr.length; i++) {
//                        int index = Integer.parseInt(positionArr[i]);
//                        Map<String, String> map = list.get(index);
//                        map.put("type", "pos");
//                        list.remove(index);
//                        list.add(index, map);
//                    }
//                }
//                int a = 0;
//                while (a < list.size()) {
//                    Map<String, String> map = list.get(a);
//                    if (map.get("text").equals("")) {
//                        list.remove(a);
//                    }
//                    else {
//                        a++;
//                    }
//                }
//                cdItemAdapter = new CdItemAdapter(this, list);
//                listView.setAdapter(cdItemAdapter);
//                // 分类标注

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
                        countTransE = pairs.length;

                        if (pairs[0].equals("ERROR")) { // 服务器处理出错
                            transeErrorView.setText("没有符合条件的对象-属性元组!");
                            Log.d("MyDebug", "[TransE] Error!");
                            transESuccess = false;
                        }
                        else { // 处理成功
                            transESuccess = true;
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
                                if (transESuccess) {
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
                                buildReport(countPos, countAll, countTransE); // 整理报告，并显示
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
        switch (view.getId()) {
            case R.id.display_back_view:
                finish();
                break;
            case R.id.tab_cd:
                resetImage();
                tabCDLayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                viewPager.setCurrentItem(0);
                break;
            case R.id.tab_pu:
                resetImage();
                tabPULayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                viewPager.setCurrentItem(1);
                break;
            case R.id.tab_transe:
                resetImage();
                tabTransELayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                viewPager.setCurrentItem(2);
                break;
            case R.id.tab_report:
                resetImage();
                tabReportLayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                viewPager.setCurrentItem(3);
                break;
            case R.id.cd_legend_pos:
                if (posHasShowed && negHasShowed) {
                    posHasShowed = false;
                    hideLegendColor();
                    ((TextView) findViewById(R.id.cd_legend_neg_color)).setBackgroundColor(
                            getResources().getColor(R.color.cd_legend_neg_selected));
                    ((TextView) findViewById(R.id.cd_legend_neg_text)).setTextColor(
                            getResources().getColor(R.color.cd_legend_text));
                    showNeg();
                }
                else if (posHasShowed && !negHasShowed) {
                    posHasShowed = false;
                    hideLegendColor();
                    showNone();
                }
                else if (!posHasShowed && negHasShowed) {
                    posHasShowed = true;
                    showLegendColor();
                    showAll();
                }
                else {
                    posHasShowed = true;
                    hideLegendColor();
                    ((TextView) findViewById(R.id.cd_legend_pos_color)).setBackgroundColor(
                            getResources().getColor(R.color.cd_legend_pos_selected));
                    ((TextView) findViewById(R.id.cd_legend_pos_text)).setTextColor(
                            getResources().getColor(R.color.cd_legend_text));
                    showPos();
                }
                break;
            case R.id.cd_legend_neg:
                if (negHasShowed && posHasShowed) {
                    negHasShowed = false;
                    hideLegendColor();
                    ((TextView) findViewById(R.id.cd_legend_pos_color)).setBackgroundColor(
                            getResources().getColor(R.color.cd_legend_pos_selected));
                    ((TextView) findViewById(R.id.cd_legend_pos_text)).setTextColor(
                            getResources().getColor(R.color.cd_legend_text));
                    showPos();
                }
                else if (negHasShowed && !posHasShowed) {
                    negHasShowed = false;
                    hideLegendColor();
                    showNone();
                }
                else if (!negHasShowed && posHasShowed) {
                    negHasShowed = true;
                    showLegendColor();
                    showAll();
                }
                else {
                    negHasShowed = true;
                    hideLegendColor();
                    ((TextView) findViewById(R.id.cd_legend_neg_color)).setBackgroundColor(
                            getResources().getColor(R.color.cd_legend_neg_selected));
                    ((TextView) findViewById(R.id.cd_legend_neg_text)).setTextColor(
                            getResources().getColor(R.color.cd_legend_text));
                    showNeg();
                }
        }
    }

    private void resetImage() {
        tabCDLayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
        tabPULayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
        tabTransELayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
        tabReportLayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
    }

    private void showLegendColor() {
        ((TextView) findViewById(R.id.cd_legend_pos_color)).setBackgroundColor(
                getResources().getColor(R.color.cd_legend_pos_selected));
        ((TextView) findViewById(R.id.cd_legend_pos_text)).setTextColor(
                getResources().getColor(R.color.cd_legend_text));
        ((TextView) findViewById(R.id.cd_legend_neg_color)).setBackgroundColor(
                getResources().getColor(R.color.cd_legend_neg_selected));
        ((TextView) findViewById(R.id.cd_legend_neg_text)).setTextColor(
                getResources().getColor(R.color.cd_legend_text));
    }

    private void hideLegendColor() {
        ((TextView) findViewById(R.id.cd_legend_pos_color)).setBackgroundColor(
                getResources().getColor(R.color.cd_legend_not_selected));
        ((TextView) findViewById(R.id.cd_legend_pos_text)).setTextColor(
                getResources().getColor(R.color.cd_legend_not_selected));
        ((TextView) findViewById(R.id.cd_legend_neg_color)).setBackgroundColor(
                getResources().getColor(R.color.cd_legend_not_selected));
        ((TextView) findViewById(R.id.cd_legend_neg_text)).setTextColor(
                getResources().getColor(R.color.cd_legend_not_selected));
    }

    private void showAll() {
        list.clear();
        for (int i = 0; i < sentArr.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("type", "neg");
            map.put("text", sentArr[i]);
            list.add(map);
        }
        if (!positionOfPos.equals("")) { // 有负面信息
            positionArr = positionOfPos.split(" ");
            for (int i = 0; i < positionArr.length; i++) {
                int index = Integer.parseInt(positionArr[i]);
                Map<String, String> map = list.get(index);
                map.put("type", "pos");
                list.remove(index);
                list.add(index, map);
            }
        }
        int a = 0;
        while (a < list.size()) {
            Map<String, String> map = list.get(a);
            if (map.get("text").equals("")) {
                list.remove(a);
            }
            else {
                a++;
            }
        }
        cdItemAdapter = new CdItemAdapter(this, list);
        listView.setAdapter(cdItemAdapter);
    }

    private void showPos() {
        list.clear(); // 重新组织显示数据
        positionArr = positionOfPos.split(" ");
        for (int i = 0; i < positionArr.length; i++) {
            int index = Integer.parseInt(positionArr[i]);
            if (!sentArr[index].equals("")) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("type", "pos");
                map.put("text", sentArr[index]);
                list.add(map);
            }
        }
        cdItemAdapter = new CdItemAdapter(this, list);
        listView.setAdapter(cdItemAdapter);
    }

    private void showNeg() {
        list.clear();
        for (int i = 0; i < sentArr.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("type", "neg");
            map.put("text", sentArr[i]);
            list.add(map);
        }
        if (!positionOfPos.equals("")) { // 有负面信息，则去掉所有的负面信息
            int offset = 0;
            positionArr = positionOfPos.split(" ");
            for (int i = 0; i < positionArr.length; i++) {
                int index = Integer.parseInt(positionArr[i]) - offset++;
                list.remove(index);
            }
        }
        int a = 0;
        while (a < list.size()) { // 去掉空句子
            Map<String, String> map = list.get(a);
            if (map.get("text").equals("")) {
                list.remove(a);
            }
            else {
                a++;
            }
        }
        cdItemAdapter = new CdItemAdapter(this, list);
        listView.setAdapter(cdItemAdapter);
    }

    private void showNone() {
        list.clear();
        cdItemAdapter = new CdItemAdapter(this, list);
        listView.setAdapter(cdItemAdapter);
    }

    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && pieView.canGoBack()) {
            pieView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK) && forceView.canGoBack()) {
            forceView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (pieView != null) {
            pieView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            pieView.clearHistory();

            ((ViewGroup) pieView.getParent()).removeView(pieView);
            pieView.destroy();
            pieView = null;
        }
        if (forceView != null) {
            forceView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            forceView.clearHistory();

            ((ViewGroup) forceView.getParent()).removeView(forceView);
            forceView.destroy();
            forceView = null;
        }
        super.onDestroy();
    }


    private void buildReport(int countPos, int countAll, int countTransT) {
        String persentInfoStr = String.format("%.2f",  (float) countPos * 100 / countAll ) + "%";
        reportView.setText("这篇文章总共有 " + countPos + " 个负面句子，" +
                "占百分比 " + persentInfoStr + "。\n从中抽取出共 " + countTransT + " 组属性信息。");
    }

    class CdItemAdapter extends BaseAdapter {
        private List<Map<String, String>> data;
        private LayoutInflater layoutInflater;
        private Context context;

        public CdItemAdapter(Context context, List<Map<String, String>> data) {
            //传入的data，就是我们要在listview中显示的信息
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            String type = data.get(i).get("type").trim();
            String text = data.get(i).get("text");
            switch (type) {
                case "pos":
                    view = layoutInflater.inflate(R.layout.tab_cd_item_pos, null);
                    break;
                case "neg":
                    view = layoutInflater.inflate(R.layout.tab_cd_item_neg, null);
            }

            TextView textView = (TextView) view.findViewById(R.id.tab_cd_item);
            textView.setText(text);

            return view;
        }
    }
}
