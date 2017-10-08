package com.example.zwm.myapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.util.InputTextDBHelper;

public class ClearSpaceFragment extends Fragment {
    private ImageView backBtn;
    private TextView textView;
    private Button confirmButton;

    private InputTextDBHelper dbHelper;

    private String uemailaddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clear_space, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews(getActivity());
        initEvents(getActivity());
    }

    private void initViews(FragmentActivity activity) {
        backBtn = (ImageView) activity.findViewById(R.id.clear_space_back_view);
        textView = (TextView) activity.findViewById(R.id.clear_space_text_view);
        confirmButton = (Button) activity.findViewById(R.id.clear_space_confirm_button);

        try {
            dbHelper = new InputTextDBHelper(activity);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            SharedPreferences sp = activity.getSharedPreferences("UserInFo", Context.MODE_PRIVATE);
            uemailaddress = sp.getString("uemailaddress", "");
            String sql = "select * from input_text where uemailaddress=?;";
            Cursor cursor = db.rawQuery(sql, new String[]{ uemailaddress });
            int a = 0, b = 0;
            while (cursor.moveToNext()) {
                a++; // 记录数
                b += 4 + cursor.getString(1).getBytes().length + cursor.getString(2).getBytes().length; // 字节数
            }
            cursor.close();
            db.close();

            String size = "";
            if (b < 1024) { // 单位B
                size = b + "B";
            }
            else if ((b /= 1024) < 1024) { // 单位KB
                size = b + "KB";
            }
            else if ((b /= 1024) < 1024) { // 单位MB
                size = b + "MB";
            }
            else if ((b /= 1024) < 1024) { // 单位GB
                size = b + "GB";
            }
            else {
                size = "无法显示";
            }

            String showStr = "查询得 " + a + " 条分析记录，占用存储空间共计 " + size + "。";
            textView.setText(showStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvents(final FragmentActivity activity) {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    String sql = "delete from input_text where uemailaddress=?;";
                    db.execSQL(sql, new Object[]{ uemailaddress });
                    db.close();

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("清理完毕！");
                        }
                    });
                } catch (Exception e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("清理失败！");
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
    }
}
