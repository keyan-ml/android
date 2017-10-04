package com.example.zwm.myapplication.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.zwm.myapplication.R;

public class WelcomeActivity extends FragmentActivity implements View.OnClickListener {
    // View
    private Button signInButton;
    private Button signUpButton;
    // View

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

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

        ((Button) findViewById(R.id.test)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tempIntent = new Intent();
                tempIntent.setClass(WelcomeActivity.this, MainActivity.class);
                Log.d("MyDebug", "启动 ViewPagerActivity");
                startActivity(tempIntent);
            }
        });
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this, SiginInAndUpActivity.class);
        switch (view.getId()) {
            case R.id.goto_sign_in_button:
//                Log.d("MyDebug", "点击了登录");
                intent.putExtra("fragmentName", "SignInFragment");
                break;
            case R.id.goto_sign_up_button:
//                Log.d("MyDebug", "点击了注册");
                intent.putExtra("fragmentName", "SignUpFragment");
                break;
        }
        startActivity(intent);
    }

}
