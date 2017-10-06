package com.example.zwm.myapplication.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.fragment.InputPostFragment;
import com.example.zwm.myapplication.fragment.OtherFragment;
import com.example.zwm.myapplication.fragment.UserFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static Activity instance;

    private final String TAB_NORMAL_COLOR = "#696969";
    private final String TAB_SELECTED_COLOR = "#8FBC8F";

    private Fragment currentFragment;

    private LinearLayout tabInputPost;
    private LinearLayout tabOther;
    private LinearLayout tabUser;

    // 两个Fragment
    private InputPostFragment inputPostFragment;
    private OtherFragment otherFragment;
    private UserFragment userFragment;
    // 两个Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initEvents();
    }

    private void initViews() {
        instance = this;

        tabInputPost = (LinearLayout) findViewById(R.id.tab_input_post);
        tabOther = (LinearLayout) findViewById(R.id.tab_other);
        tabUser = (LinearLayout) findViewById(R.id.tab_user);

        inputPostFragment = null;
        otherFragment = null;
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
        tabOther.setOnClickListener(this);
        tabUser.setOnClickListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (inputPostFragment == null) { // 初始化所有Fragment的对象句柄，并加入FrameLayout
            inputPostFragment = new InputPostFragment();
            transaction.add(R.id.fragment_content_main, inputPostFragment);
        }
        if (otherFragment == null) {
            otherFragment = new OtherFragment();
            transaction.add(R.id.fragment_content_main, otherFragment);
        }
        if (userFragment == null) {
            userFragment = new UserFragment();
            transaction.add(R.id.fragment_content_main, userFragment);
        }

        hideFragments(transaction);

        tabInputPost.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
        transaction.show(inputPostFragment);
//        currentFragment = inputPostFragment;
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (inputPostFragment != null) {
            tabInputPost.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
            transaction.hide(inputPostFragment);
        }
        if (otherFragment != null) {
            tabOther.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
            transaction.hide(otherFragment);
        }
        if (userFragment != null) {
            tabUser.setBackgroundColor(Color.parseColor(TAB_NORMAL_COLOR));
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
                tabInputPost.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                transaction.show(inputPostFragment);
                break;
            case R.id.tab_other:
                tabOther.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                transaction.show(otherFragment);
                break;
            case R.id.tab_user:
                tabUser.setBackgroundColor(Color.parseColor(TAB_SELECTED_COLOR));
                transaction.show(userFragment);
        }
        transaction.commit();
    }
}
