package com.example.zwm.myapplication.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zwm12 on 2017/10/7.
 */

public class InputTextDBHelper extends SQLiteOpenHelper {
    private static final String name = "android_db"; // 数据库名称
    private static final int version = 1; // 数据库版本

//    public InputTextDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }
    public InputTextDBHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists input_text ( " +
                        "id int auto_increment primary key, " +
                        "uemailaddress varchar(44) not null, " +
                        "inputtext text not null, " +
                        "inputdatetime datetime);";
        db.execSQL(sql);
        Log.d("MyDebug", "新建表input_text");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }
}
