package com.example.zwm.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView inputText;
    private TextView resultText;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputText = (TextView) findViewById(R.id.m_input_text);
        resultText = (TextView) findViewById(R.id.test_http);
        btn = (Button) findViewById(R.id.commit_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultText.setText(inputText.getText());
            }
        });

//        JSONObject json = new JSONObject();
//        json.put();
    }
}
