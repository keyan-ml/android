package com.example.zwm.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zwm.myapplication.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    public static Activity instance;

    private ImageView backBtn;
    private LinearLayout modifyPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        initEvents();
    }

    private void initViews() {
        instance = this;

        backBtn = (ImageView) findViewById(R.id.settings_back_view);
        modifyPasswordView = (LinearLayout) findViewById(R.id.settings_modify_password);
    }

    private void initEvents() {
        backBtn.setOnClickListener(this);
        modifyPasswordView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.settings_back_view:
                this.finish();
                break;
            case R.id.settings_modify_password:
                ModifyPasswordActivity.isComingFromSettings = true;
                intent.setClass(this, ModifyPasswordActivity.class);
                startActivity(intent);
        }
    }
}
