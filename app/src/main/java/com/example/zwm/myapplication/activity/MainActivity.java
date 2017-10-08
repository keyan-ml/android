package com.example.zwm.myapplication.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.fragment.InputPostFragment;
import com.example.zwm.myapplication.fragment.FileUploadFragment;
import com.example.zwm.myapplication.fragment.UserFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static Activity instance;

    private final String TAB_NORMAL_COLOR = "#696969";
    private final String TAB_SELECTED_COLOR = "#8FBC8F";

    private long pressTime;

    private ImageView tabInputPost;
    private ImageView tabFileUpload;
    private ImageView tabUser;

    // 三个Fragment
    private InputPostFragment inputPostFragment;
    private FileUploadFragment fileUploadFragment;
    private UserFragment userFragment;
    // 三个Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initEvents();
    }

    private void initViews() {
        instance = this;
        pressTime = 0;

        tabInputPost = (ImageView) findViewById(R.id.tab_input_post);
        tabFileUpload = (ImageView) findViewById(R.id.tab_file_upload);
        tabUser = (ImageView) findViewById(R.id.tab_user);

        inputPostFragment = null;
        fileUploadFragment = null;
        userFragment = null;

    }

    private void initEvents() {
        if (SignInActivity.instance != null) {
            SignInActivity.instance.finish(); // 结束登录界面activity
        }
        if (SignUpActivity.instance != null) {
            SignUpActivity.instance.finish(); // 结束注册界面activity
        }

        tabInputPost.setOnClickListener(this);
        tabFileUpload.setOnClickListener(this);
        tabUser.setOnClickListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (inputPostFragment == null) { // 初始化所有Fragment的对象句柄，并加入FrameLayout
            inputPostFragment = new InputPostFragment();
            transaction.add(R.id.fragment_content_main, inputPostFragment);
        }
        if (fileUploadFragment == null) {
            fileUploadFragment = new FileUploadFragment();
            transaction.add(R.id.fragment_content_main, fileUploadFragment);
        }
        if (userFragment == null) {
            userFragment = new UserFragment();
            transaction.add(R.id.fragment_content_main, userFragment);
        }

        hideFragments(transaction);

        tabInputPost.setImageResource(R.mipmap.input_post_selected_icon);
        transaction.show(inputPostFragment);
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (inputPostFragment != null) {
            tabInputPost.setImageResource(R.mipmap.input_post_icon);
            transaction.hide(inputPostFragment);
        }
        if (fileUploadFragment != null) {
            tabFileUpload.setImageResource(R.mipmap.file_upload_icon);
            transaction.hide(fileUploadFragment);
        }
        if (userFragment != null) {
            tabUser.setImageResource(R.mipmap.name_icon);
            transaction.hide(userFragment);
        }
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        hideFragments(transaction); // 隐藏所有Fragment

        switch (view.getId()) { // 根据点击的View的id，设置响应动作
            case R.id.tab_input_post:
                tabInputPost.setImageResource(R.mipmap.input_post_selected_icon);
                transaction.show(inputPostFragment);
                break;
            case R.id.tab_file_upload:
                tabFileUpload.setImageResource(R.mipmap.file_upload_selected_icon);
                transaction.show(fileUploadFragment);
                break;
            case R.id.tab_user:
                tabUser.setImageResource(R.mipmap.name_selected_icon);
                transaction.show(userFragment);
        }
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - pressTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                pressTime = System.currentTimeMillis();
                return true;
            }
            else { // 2秒内点击了两次
                SharedPreferences.Editor spEditor = getSharedPreferences("UserInFo", MODE_PRIVATE).edit();
                spEditor.clear();
                finish();
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
