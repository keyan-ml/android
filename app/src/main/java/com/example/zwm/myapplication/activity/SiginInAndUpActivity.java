package com.example.zwm.myapplication.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.fragment.SignInFragment;
import com.example.zwm.myapplication.fragment.SignUpFragment;

public class SiginInAndUpActivity extends AppCompatActivity {
    private FrameLayout fragmentContent;

    // 两个Fragment
    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;
    // 两个Fragment

    private String fragmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin_in_and_up);

        Intent intent = getIntent();
        fragmentName = intent.getStringExtra("fragmentName");

        initViews();
        initEvents(fragmentName);
    }


    private void initViews() {
        fragmentContent = (FrameLayout) findViewById(R.id.fragment_content_sign_in_and_up);

        signInFragment = null;
        signUpFragment = null;
    }

    private void initEvents(String fragmentName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        hideFragments(transaction); // 隐藏所有Fragment

        if (signInFragment == null) {
            signInFragment = new SignInFragment();
            transaction.add(R.id.fragment_content_sign_in_and_up, signInFragment);
        }

        if (signUpFragment == null) {
            signUpFragment = new SignUpFragment();
            transaction.add(R.id.fragment_content_sign_in_and_up, signUpFragment);
        }

        switch (fragmentName) {
            case "SignInFragment":
//                Log.d("MyDebug", "点击了登录");
                transaction.hide(signUpFragment);
                transaction.show(signInFragment);
                break;
            case "SignUpFragment":
//                Log.d("MyDebug", "点击了注册");
                transaction.hide(signInFragment);
                transaction.show(signUpFragment);
        }

        transaction.commit();
    }


//    private void hideFragments(FragmentTransaction transaction) {
//        if (signInFragment != null) {
//            transaction.hide(signInFragment);
//        }
//        if (signUpFragment != null) {
//            transaction.hide(signUpFragment);
//        }
//    }


}
