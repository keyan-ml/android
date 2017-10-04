package com.example.zwm.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.activity.FeedBackActivity;

/**
 * Created by zwm on 2017/10/4.
 */

public class UserFragment extends Fragment implements View.OnClickListener {
    private TextView feedbackView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = getActivity();

        initViews(activity);
        initEvents(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    private void initViews(FragmentActivity activity) {
        feedbackView = (TextView) activity.findViewById(R.id.user_feedback);
    }

    private void  initEvents(FragmentActivity activity) {
        feedbackView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.user_feedback:
                Log.d("MyDebug", "click user_feedback");
                intent.setClass(getActivity(), FeedBackActivity.class);
        }
        startActivity(intent);
    }
}
