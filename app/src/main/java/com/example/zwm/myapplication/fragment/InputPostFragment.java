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
//        inputTextView.setText(
//                "宝马的油耗太高了。" +
//                "红色的很嗲，白色敞篷的也喜欢的~。" +
//                "奇瑞汽车下线了这是结果，而不是奇瑞委屈的到处说自己一腔热血工业报国无门。" +
//                "奥迪的吉普中看不中用~苦逼的赶路孩子只能窝在最后座的小小空间里~。" +
//                "新览胜的灯更是无力吐槽。比亚迪回复玻璃本性，没有默德萨克是最大败笔。" +
//                "个人比较讨厌开别克的，因为经常发现别克大白天的开着车灯甚至远光，每次被晃发现又是个别克，心中一万只羊驼奔腾，最近在上海各种原因各种机会开了各种别克，才发现原来几乎都是特么自动开灯的，别克的设计师大概都是偏远山区出来的吧，默认不是自动会死啊。" +
//                "速腾内饰也不好看。" +
//                "比亚迪核心业务前景遭质疑-华尔街日报姐早就说了，千万不要买他们家的车子。" +
//                "byd的策略根本就是有问题，为了圈钱，不是为了造车……"
//        );
//        inputTextView.setText(
//                "好的品质也要配合好的服务才有更多的顾客，奥迪4s店的态度都这样傲慢？" +
//                "奥迪a1现有款都是两门的，上下车及其不方便，空间也太小，腿伸不开。" +
//                "已经对奥迪a4l失望之极，无缘无故刹车油管会破裂的，已经第三次刹车失灵了，还不算在质保范围内~。" +
//                "国产的奔驰c甲醛超标4倍。" +
//                "丰田呢，车款辆辆千篇一律，没点儿新鲜感，车的质量也不咋的。" +
//                "合资的奥迪，一年，左侧前车窗降落时吱吱响。" +
//                "王传福过于沉浸在业绩的光环中，缺乏了后续的创新，使得比亚迪跌入过于依赖单一产品的窘迫局面，导致销量没有支撑点。" +
//                "可是我的车目前还没有给我任何回应，我觉得雪佛兰不仅是服务态度，这个公司的诚信都有严重的问题。" +
//                "福特好费油好费油，路好堵好堵，想回家可怜的小思乡情节又来了。" +
//                "一汽也不是什么好鸟，变速箱的问题不也还没有解决？"
//        );
//        inputTextView.setText("漳州奥迪的服务态度真的是不敢恭维…。" +
//                "宝马后期保养太坑爹配件一般情况是不会到位的。" +
//                "奥迪座椅舒适度不足。" +
//                "而且奥迪国内定价不厚道…。" +
//                "原地打方向盘很重没有奥迪一贯的轻盈！" +
//                "电镀奥迪r8反光厉害容易追尾！" +
//                "奥迪前大灯越改越小气。" +
//                "还有大众的设计外形太死板不太喜欢。" +
//                "我对奥迪已经失去信心了。" +
//                "奥迪不给力啊！" +
//                "奥迪算个鸟。" +
//                "奥迪坐进去驾驭感不足。" +
//                "宝马坐着不舒服。" +
//                "宝马真的过敏!"
//        );
        inputTextView.setText("售后就更别提了，一买奔驰深似海啊！老款现在优惠40以上，奔驰的车越买越寒心。" +
                "我最后拿到的并非低价，我承认我只为了颜色就买了，但随后我就开始非常讨厌奔驰价格战这件事儿！" +
                "总觉得奔驰在中国水土不服。" +
                "同为豪华车“bba阵营”，奔驰的销量绝对值和相对增幅，都远远落后于对手。" +
                "没想到奥迪这个在中国卖的火的不得了的车，其实不堪一击～～美国最新的汽车碰撞试验。" +
                "本来嘛，奥迪就在中国火了一把，在国外就档次一般，而且小毛病又那么多。" +
                "已经对奥迪失望之极，无缘无故刹车油管会破裂的，已经第三次刹车失灵了，还不算在质保范围内。" +
                "现在的宝马无语喔！" +
                "我现在贼讨厌宝马。" +
                "为什么宝马后面都那么小，坐着不舒服。" +
                "而且宝马后期保养太坑爹配件一般情况是不会到位的。" +
                "王传福过于沉浸在业绩的光环中，缺乏了后续的创新，使得比亚迪跌入过于依赖单一造型的窘迫局面，导致销量没有支撑点。" +
                "比亚迪要倒闭的，垃圾企业，家族式管理，狂妄自大吹牛皮却又没什么水平。" +
                "比亚迪的所谓电池，军网上科普过，完全就是忽悠。" +
                "比亚迪经常吹牛说是要在哪里开个啥大厂，整合上下游之类的，一般这种企业在中国都是很难做大，主要是无法专一，只想通吃。" +
                ""
        );

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
                                positionOfPos = partArr[1].trim();
                                transESents = "";
                                String postString = "myId=" + PublicVariable.sessionid;
                                if (positionOfPos.equals("null")) {
                                    postString += "&sents=null";
                                }
                                else {
                                    String[] sents = inputText.trim().split("[。！？]");
                                    String[] positions = positionOfPos.trim().split(" ");
                                    for (int i = 0; i < positions.length; i++) {
                                        String str = sents[ Integer.parseInt(positions[i]) ].trim();
                                        if (!str.equals("")) {
                                            transESents += str + "。";
                                        }
                                    }
                                    postString += "&sents=" + transESents;
                                }
//                                postString = "myId=" + PublicVariable.sessionid +
//                                        "&sents=" + transESents;
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
