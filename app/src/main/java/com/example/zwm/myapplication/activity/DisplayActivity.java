package com.example.zwm.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.model.PublicVariable;
import com.example.zwm.myapplication.model.Report;
import com.example.zwm.myapplication.util.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class DisplayActivity extends AppCompatActivity implements View.OnClickListener {
    private final String ROOT_URL_PATH = PublicVariable.URL_ROOT_PATH;
    private final String ANDROID_ECHARTS_SERVLET_URL = ROOT_URL_PATH + "/androidecharts";
//    private final String TRANSESERVLET_URL_PATH = URL_ROOT_PATH + "/transeservlet";
    private final String PIE_HTML = ROOT_URL_PATH + "/android/pie.html";
    private final String FORCE_HTML = ROOT_URL_PATH + "/android/force.html";
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
//    private LinearLayout tabCDLayout;
    private LinearLayout tabPULayout;
    private LinearLayout tabTransELayout;
    private LinearLayout tabReportLayout;
//    private LinearLayout cdLegendPos;
//    private LinearLayout cdLegendNeg;

    private ListView reportListView;
    private List<Report> reportList;
    private ReportItemAdapter reportItemAdapter;
    private boolean posHasShowed;
    private boolean negHasShowed;

    private TextView puErrorView;
    private TextView transeErrorView;

    private WebView pieView;
    private WebView forceView;
    private LinearLayout pieViewLayout;
    private LinearLayout forceViewLayout;
//    private TextView reportView;
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
    private String resultFromTransE;
    private String[] sentArr;
    private boolean transESuccess;

//    private int countPos;
//    private int countAll;
//    private int countTransE;

    private String reportContent;

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
//        inputText = intent.getStringExtra("inputText");
//        sentArr = inputText.toString().split("[。！？]"); // 分句
        resultFromPU = intent.getStringExtra("resultFromPU");
        resultFromTransE = intent.getStringExtra("resultFromTransE");
        // 获取参数

        backView = (ImageView) findViewById(R.id.display_back_view);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
//        tabCDLayout = (LinearLayout) findViewById(R.id.tab_cd);
        tabPULayout = (LinearLayout) findViewById(R.id.tab_pu);
        tabTransELayout = (LinearLayout) findViewById(R.id.tab_transe);
        tabReportLayout = (LinearLayout) findViewById(R.id.tab_report);

        LayoutInflater inflater = getLayoutInflater(); // 将layout文件映射为view对象
//        View tabCDView = inflater.inflate(R.layout.tab_cd, null);
        View tabPUView = inflater.inflate(R.layout.tab_pu, null);
        View tabTransEView = inflater.inflate(R.layout.tab_transe, null);
        View tabReportView = inflater.inflate(R.layout.tab_report, null);

//        viewList.add(tabCDView);
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

//        cdLegendPos = (LinearLayout) tabCDView.findViewById(R.id.cd_legend_pos);
//        cdLegendNeg = (LinearLayout) tabCDView.findViewById(R.id.cd_legend_neg);
//        listView = (ListView) tabCDView.findViewById(R.id.container_of_cd);
//        list = new ArrayList<Map<String, String>>();

        pieViewLayout = (LinearLayout) tabPUView.findViewById(R.id.pie_web_view_layout);
        forceViewLayout = (LinearLayout) tabTransEView.findViewById(R.id.force_web_view_layout);
//        reportView = (TextView) tabReportView.findViewById(R.id.report_content);
        reportListView = (ListView) tabReportView.findViewById(R.id.report_list_view);
        reportList = new ArrayList<Report>();

        // 初始化View

        posHasShowed = true;
        negHasShowed = true;
    }

    private void initEvents() {
        backView.setOnClickListener(this);

//        tabCDLayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
        tabPULayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));

//        tabCDLayout.setOnClickListener(this);
        tabPULayout.setOnClickListener(this);
        tabTransELayout.setOnClickListener(this);
        tabReportLayout.setOnClickListener(this);

//        cdLegendPos.setOnClickListener(this);
//        cdLegendNeg.setOnClickListener(this);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //
            }

            @Override
            public void onPageSelected(int position) {
                resetImage();
                switch (position) {
//                    case 0:
//                        tabCDLayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
//                        break;
                    case 0:
                        tabPULayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                        break;
                    case 1:
                        tabTransELayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                        break;
                    case 2:
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
                String[] partArr = resultFromPU.split("\\|"); // 饼图
                countArr = partArr[0].split(" ");
                positionOfPos = partArr[1];
//                countPos = Integer.parseInt(countArr[0].trim());
//                countAll = countPos + Integer.parseInt(countArr[1].trim());

                pieView.getSettings().setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
                pieView.addJavascriptInterface(new JsObject(), "androidJsObject");
                pieView.loadUrl(PIE_HTML); // 加载需要显示的网页//设置Web视图
                pieView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        puErrorView.setText("正在加载... 请稍候！");
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        puErrorView.setText("加载完成！");
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Thread.sleep(1000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            puErrorView.setVisibility(View.INVISIBLE); // 隐藏错误信息显示区域
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                });
                // 饼图

                Log.d("MyDebug", "force部分开始");
//                forceView.getSettings().setAllowFileAccess(true); // 细粒度
                forceView.getSettings().setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
                forceView.addJavascriptInterface(new JsObject(), "androidJsObjectForce");
                forceView.loadUrl(FORCE_HTML); // 加载需要显示的网页
                forceView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        transeErrorView.setText("正在加载... 请稍候！");
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        transeErrorView.setVisibility(View.INVISIBLE); // 隐藏错误信息显示区域
                        transeErrorView.setText("加载完成！");
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Thread.sleep(1000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            transeErrorView.setVisibility(View.INVISIBLE); // 隐藏错误信息显示区域
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                });
                // 细粒度

                new Thread(){ // 报告
                    @Override
                    public void run() {
                        super.run();

                        String postString = "myId=" + PublicVariable.sessionid +
                                "&postReason=getReport";
                        reportContent = HttpUtils.post(ANDROID_ECHARTS_SERVLET_URL, postString);
                        if (!reportContent.equals("null")) {
                            String[] carItems = reportContent.split("\\|");
                            for (String item : carItems) {
//                                Log.d("MyDebug", item);
                                String[] parts = item.split("&&");
                                Report report = new Report();
                                report.setImgDrawable(R.drawable.audi); // 图片
                                report.setCarType(parts[0]); // 车型
//                                String[] shortingArr = parts[1].split("&"); // 组织缺点
//                                String shortingStr = "";
//                                for (int i = 0; i < shortingArr.length; i++) {
//                                    shortingStr += shortingArr[i] + " ";
//                                }
//                                report.setShorting(shortingStr); // 缺点
                                report.setShorting(parts[1]); // 缺点
                                report.setSummary(parts[2]); // 总结
                                reportList.add(report);
                            }
                            reportItemAdapter = new ReportItemAdapter(DisplayActivity.this, reportList);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                reportView.setText(reportContent);
                                if (reportContent.equals("null")) {
                                    //
                                }
                                else {
                                    reportListView.setAdapter(reportItemAdapter);
                                }
                            }
                        });
                    }
                }.start(); // 报告
            }
        } catch (Exception e) {
            //
            Log.d("MyDebug", "网络有误！");
            e.printStackTrace();
        }
    }

    class JsObject {
        @JavascriptInterface
        public String getMyId() {
            return PublicVariable.sessionid;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.display_back_view:
                finish();
                break;
//            case R.id.tab_cd:
//                resetImage();
//                tabCDLayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
//                viewPager.setCurrentItem(0);
//                break;
            case R.id.tab_pu:
                resetImage();
                tabPULayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                viewPager.setCurrentItem(0);
                break;
            case R.id.tab_transe:
                resetImage();
                tabTransELayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                viewPager.setCurrentItem(1);
                break;
            case R.id.tab_report:
                resetImage();
                tabReportLayout.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                viewPager.setCurrentItem(2);
                break;
//            case R.id.cd_legend_pos:
//                if (posHasShowed && negHasShowed) {
//                    posHasShowed = false;
//                    hideLegendColor();
//                    ((TextView) findViewById(R.id.cd_legend_neg_color)).setBackgroundColor(
//                            getResources().getColor(R.color.cd_legend_neg_selected));
//                    ((TextView) findViewById(R.id.cd_legend_neg_text)).setTextColor(
//                            getResources().getColor(R.color.cd_legend_text));
//                    showNeg();
//                }
//                else if (posHasShowed && !negHasShowed) {
//                    posHasShowed = false;
//                    hideLegendColor();
//                    showNone();
//                }
//                else if (!posHasShowed && negHasShowed) {
//                    posHasShowed = true;
//                    showLegendColor();
//                    showAll();
//                }
//                else {
//                    posHasShowed = true;
//                    hideLegendColor();
//                    ((TextView) findViewById(R.id.cd_legend_pos_color)).setBackgroundColor(
//                            getResources().getColor(R.color.cd_legend_pos_selected));
//                    ((TextView) findViewById(R.id.cd_legend_pos_text)).setTextColor(
//                            getResources().getColor(R.color.cd_legend_text));
//                    showPos();
//                }
//                break;
//            case R.id.cd_legend_neg:
//                if (negHasShowed && posHasShowed) {
//                    negHasShowed = false;
//                    hideLegendColor();
//                    ((TextView) findViewById(R.id.cd_legend_pos_color)).setBackgroundColor(
//                            getResources().getColor(R.color.cd_legend_pos_selected));
//                    ((TextView) findViewById(R.id.cd_legend_pos_text)).setTextColor(
//                            getResources().getColor(R.color.cd_legend_text));
//                    showPos();
//                }
//                else if (negHasShowed && !posHasShowed) {
//                    negHasShowed = false;
//                    hideLegendColor();
//                    showNone();
//                }
//                else if (!negHasShowed && posHasShowed) {
//                    negHasShowed = true;
//                    showLegendColor();
//                    showAll();
//                }
//                else {
//                    negHasShowed = true;
//                    hideLegendColor();
//                    ((TextView) findViewById(R.id.cd_legend_neg_color)).setBackgroundColor(
//                            getResources().getColor(R.color.cd_legend_neg_selected));
//                    ((TextView) findViewById(R.id.cd_legend_neg_text)).setTextColor(
//                            getResources().getColor(R.color.cd_legend_text));
//                    showNeg();
//                }
        }
    }

    private void resetImage() {
//        tabCDLayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
        tabPULayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
        tabTransELayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
        tabReportLayout.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
    }


//    private void showLegendColor() {
//        ((TextView) findViewById(R.id.cd_legend_pos_color)).setBackgroundColor(
//                getResources().getColor(R.color.cd_legend_pos_selected));
//        ((TextView) findViewById(R.id.cd_legend_pos_text)).setTextColor(
//                getResources().getColor(R.color.cd_legend_text));
//        ((TextView) findViewById(R.id.cd_legend_neg_color)).setBackgroundColor(
//                getResources().getColor(R.color.cd_legend_neg_selected));
//        ((TextView) findViewById(R.id.cd_legend_neg_text)).setTextColor(
//                getResources().getColor(R.color.cd_legend_text));
//    }
//
//    private void hideLegendColor() {
//        ((TextView) findViewById(R.id.cd_legend_pos_color)).setBackgroundColor(
//                getResources().getColor(R.color.cd_legend_not_selected));
//        ((TextView) findViewById(R.id.cd_legend_pos_text)).setTextColor(
//                getResources().getColor(R.color.cd_legend_not_selected));
//        ((TextView) findViewById(R.id.cd_legend_neg_color)).setBackgroundColor(
//                getResources().getColor(R.color.cd_legend_not_selected));
//        ((TextView) findViewById(R.id.cd_legend_neg_text)).setTextColor(
//                getResources().getColor(R.color.cd_legend_not_selected));
//    }
//
//    private void showAll() {
//        list.clear();
//        for (int i = 0; i < sentArr.length; i++) {
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("type", "neg");
//            map.put("text", sentArr[i]);
//            list.add(map);
//        }
//        if (!positionOfPos.equals("")) { // 有负面信息
//            positionArr = positionOfPos.split(" ");
//            for (int i = 0; i < positionArr.length; i++) {
//                int index = Integer.parseInt(positionArr[i]);
//                Map<String, String> map = list.get(index);
//                map.put("type", "pos");
//                list.remove(index);
//                list.add(index, map);
//            }
//        }
//        int a = 0;
//        while (a < list.size()) {
//            Map<String, String> map = list.get(a);
//            if (map.get("text").equals("")) {
//                list.remove(a);
//            }
//            else {
//                a++;
//            }
//        }
//        cdItemAdapter = new CdItemAdapter(this, list);
//        listView.setAdapter(cdItemAdapter);
//    }
//
//    private void showPos() {
//        list.clear(); // 重新组织显示数据
//        positionArr = positionOfPos.split(" ");
//        for (int i = 0; i < positionArr.length; i++) {
//            int index = Integer.parseInt(positionArr[i]);
//            if (!sentArr[index].equals("")) {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("type", "pos");
//                map.put("text", sentArr[index]);
//                list.add(map);
//            }
//        }
//        cdItemAdapter = new CdItemAdapter(this, list);
//        listView.setAdapter(cdItemAdapter);
//    }
//
//    private void showNeg() {
//        list.clear();
//        for (int i = 0; i < sentArr.length; i++) {
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("type", "neg");
//            map.put("text", sentArr[i]);
//            list.add(map);
//        }
//        if (!positionOfPos.equals("")) { // 有负面信息，则去掉所有的负面信息
//            int offset = 0;
//            positionArr = positionOfPos.split(" ");
//            for (int i = 0; i < positionArr.length; i++) {
//                int index = Integer.parseInt(positionArr[i]) - offset++;
//                list.remove(index);
//            }
//        }
//        int a = 0;
//        while (a < list.size()) { // 去掉空句子
//            Map<String, String> map = list.get(a);
//            if (map.get("text").equals("")) {
//                list.remove(a);
//            }
//            else {
//                a++;
//            }
//        }
//        cdItemAdapter = new CdItemAdapter(this, list);
//        listView.setAdapter(cdItemAdapter);
//    }
//
//    private void showNone() {
//        list.clear();
//        cdItemAdapter = new CdItemAdapter(this, list);
//        listView.setAdapter(cdItemAdapter);
//    }

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

    class ReportItemAdapter extends BaseAdapter {
        private List<Report> data;
        private LayoutInflater layoutInflater;
        private Context context;

        public ReportItemAdapter(Context context, List<Report> data) {
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
            Report reportItem = data.get(i);
            view = layoutInflater.inflate(R.layout.tab_report_item, null);
            ((ImageView) view.findViewById(R.id.report_item_img)).setImageResource(reportItem.getImgDrawable());
            ((TextView) view.findViewById(R.id.report_item_car_type)).setText(reportItem.getCarType());
            ((TextView) view.findViewById(R.id.report_item_shorting)).setText(reportItem.getShorting());
            ((TextView) view.findViewById(R.id.report_item_summary)).setText(reportItem.getSummary());

            return view;
        }
    }

//    private void buildReport(int countPos, int countAll, int countTransT) {
//        String persentInfoStr = String.format("%.2f",  (float) countPos * 100 / countAll ) + "%";
//        reportView.setText("这篇文章总共有 " + countPos + " 个负面句子，" +
//                "占百分比 " + persentInfoStr + "。\n从中抽取出共 " + countTransT + " 组属性信息。");
//    }

//    class CdItemAdapter extends BaseAdapter {
//        private List<Map<String, String>> data;
//        private LayoutInflater layoutInflater;
//        private Context context;
//
//        public CdItemAdapter(Context context, List<Map<String, String>> data) {
//            //传入的data，就是我们要在listview中显示的信息
//            this.context = context;
//            this.data = data;
//            this.layoutInflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public int getCount() {
//            return data.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return data.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            String type = data.get(i).get("type").trim();
//            String text = data.get(i).get("text");
//            switch (type) {
//                case "pos":
//                    view = layoutInflater.inflate(R.layout.tab_cd_item_pos, null);
//                    break;
//                case "neg":
//                    view = layoutInflater.inflate(R.layout.tab_cd_item_neg, null);
//            }
//
//            TextView textView = (TextView) view.findViewById(R.id.tab_cd_item);
//            textView.setText(text);
//
//            return view;
//        }
//    }
}
