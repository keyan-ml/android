package com.example.zwm.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zwm.myapplication.R;

public class FirstWelcomeActivity extends AppCompatActivity {
    public static Activity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_welcome);


        instance = this;
        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.setClass(FirstWelcomeActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        }.start();
    }

}
