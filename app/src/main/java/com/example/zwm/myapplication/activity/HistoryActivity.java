package com.example.zwm.myapplication.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.util.InputTextDBHelper;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView listView;
    private List<String> list;

    private InputTextDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initViews();
        initEvents();
    }

    private void initViews() {
        listView = (ListView) findViewById(R.id.history_list_view);
        list = new ArrayList<String>();

        try {
            SharedPreferences sp = getSharedPreferences("UserInFo", MODE_PRIVATE);
            dbHelper = new InputTextDBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "";
            Cursor cursor  = db.rawQuery("select * from input_text where uemailaddress=?;", new String[]{sp.getString("uemailaddress", "")});
            String str = "";
            while (cursor.moveToNext()) {
                list.add( cursor.getString(2) );
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvents() {
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.item_history, list));
    }
}
