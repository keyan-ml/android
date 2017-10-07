package com.example.zwm.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.fragment.CheckUpdateFragment;
import com.example.zwm.myapplication.fragment.ClearSpaceFragment;
import com.example.zwm.myapplication.fragment.HelpFragment;
import com.example.zwm.myapplication.fragment.MakeDarkFragment;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    public static Activity instance;

    private ImageView backBtn;
    private LinearLayout modifyPasswordView;
    private LinearLayout makeDarkView;
    private LinearLayout clearSpaceView;
    private LinearLayout checkUpdateView;
    private LinearLayout helpView;

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
        makeDarkView = (LinearLayout) findViewById(R.id.settings_make_dark);
        clearSpaceView = (LinearLayout) findViewById(R.id.settings_clear_space_db);
        checkUpdateView = (LinearLayout) findViewById(R.id.settings_check_update);
        helpView = (LinearLayout) findViewById(R.id.settings_help);
    }

    private void initEvents() {
        backBtn.setOnClickListener(this);
        modifyPasswordView.setOnClickListener(this);
        makeDarkView.setOnClickListener(this);
        clearSpaceView.setOnClickListener(this);
        checkUpdateView.setOnClickListener(this);
        helpView.setOnClickListener(this);
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
                break;
            case R.id.settings_make_dark:
                ModifyPasswordActivity.isComingFromSettings = true;
                intent.setClass(this, SettingsFragmentsActivity.class);
                intent.putExtra("FragmentId", "MakeDarkFragment");
                startActivity(intent);
                break;
            case R.id.settings_clear_space_db:
                ModifyPasswordActivity.isComingFromSettings = true;
                intent.setClass(this, SettingsFragmentsActivity.class);
                intent.putExtra("FragmentId", "ClearSpaceFragment");
                startActivity(intent);
                break;
            case R.id.settings_check_update:
                ModifyPasswordActivity.isComingFromSettings = true;
                intent.setClass(this, SettingsFragmentsActivity.class);
                intent.putExtra("FragmentId", "CheckUpdateFragment");
                startActivity(intent);
                break;
            case R.id.settings_help:
                ModifyPasswordActivity.isComingFromSettings = true;
                intent.setClass(this, SettingsFragmentsActivity.class);
                intent.putExtra("FragmentId", "HelpFragment");
                startActivity(intent);
        }
    }
}
