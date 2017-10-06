package com.example.zwm.myapplication.activity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.StringSearch;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zwm.myapplication.R;

public class TestActivity extends AppCompatActivity {
    private TextView showView;
    private EditText inputView;
    private Button commitBtn;

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initViews();
        initEvents();
    }

    private void initViews() {
        showView = (TextView) findViewById(R.id.test_show_text);
        inputView = (EditText) findViewById(R.id.test_input_text);
        commitBtn = (Button) findViewById(R.id.test_commit_btn);

//        dbHelper = new SQLiteOpenHelper() {
//            @Override
//            public void onCreate(SQLiteDatabase sqLiteDatabase) {
//                //
//            }
//
//            @Override
//            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//
//            }
//        };
    }

    private void initEvents() {
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });
    }
}
