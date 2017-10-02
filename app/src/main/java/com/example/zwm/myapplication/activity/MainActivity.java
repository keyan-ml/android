package com.example.zwm.myapplication.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.fragment.SignInFragment;
import com.example.zwm.myapplication.fragment.SignUpFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    // View
    private Button signInButton;
    private Button signUpButton;
    // View

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initEvents();

    }


    private void initViews() {
        signInButton = (Button) findViewById(R.id.goto_sign_in_button);
        signUpButton = (Button) findViewById(R.id.goto_sign_up_button);

    }

    private void initEvents() {
        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SiginInAndUpActivity.class);
        switch (view.getId()) {
            case R.id.goto_sign_in_button:
//                Log.d("MyDebug", "点击了登录");
                intent.putExtra("fragmentName", "SignInFragment");
                break;
            case R.id.goto_sign_up_button:
//                Log.d("MyDebug", "点击了注册");
                intent.putExtra("fragmentName", "SignUpFragment");
        }
        startActivity(intent);
    }

}
