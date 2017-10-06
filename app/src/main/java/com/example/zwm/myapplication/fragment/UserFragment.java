package com.example.zwm.myapplication.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.activity.FeedBackActivity;
import com.example.zwm.myapplication.activity.HistoryActivity;
import com.example.zwm.myapplication.activity.SettingsActivity;
import com.example.zwm.myapplication.activity.SignInActivity;
import com.example.zwm.myapplication.activity.UserInfoActivity;

import org.w3c.dom.Text;

/**
 * Created by zwm on 2017/10/4.
 */

public class UserFragment extends Fragment implements View.OnClickListener {
    private TextView unameView;
    private TextView uemailaddressView;

    private LinearLayout historyView;
    private LinearLayout userInfoView;
    private LinearLayout settingsView;
    private LinearLayout feedbackView;
    private Button signOutBtn;

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
        unameView = (TextView) activity.findViewById(R.id.user_uname);
        uemailaddressView = (TextView) activity.findViewById(R.id.user_uemailaddress);

        historyView = (LinearLayout) activity.findViewById(R.id.user_history);
        userInfoView = (LinearLayout) activity.findViewById(R.id.user_user_info);
        feedbackView = (LinearLayout) activity.findViewById(R.id.user_feedback);
        settingsView = (LinearLayout) activity.findViewById(R.id.user_settings);
        signOutBtn = (Button) activity.findViewById(R.id.user_sign_out_button);

    }

    private void  initEvents(FragmentActivity activity) {
        SharedPreferences sp = activity.getSharedPreferences("UserInFo", Context.MODE_PRIVATE);
        unameView.setText(sp.getString("uname", ""));
        uemailaddressView.setText(sp.getString("uemailaddress", ""));

        historyView.setOnClickListener(this);
        userInfoView.setOnClickListener(this);
        feedbackView.setOnClickListener(this);
        settingsView.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.user_history:
                Log.d("MyDebug", "click user_history");
                intent.setClass(getActivity(), HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.user_user_info:
                Log.d("MyDebug", "click user_user_info");
                intent.setClass(getActivity(), UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.user_feedback:
                Log.d("MyDebug", "click user_feedback");
                intent.setClass(getActivity(), FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.user_settings:
                Log.d("MyDebug", "click user_settings");
                intent.setClass(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.user_sign_out_button:
                Log.d("MyDebug", "click sign out button");
                showSignOutDialog();
        }
    }

    private void showSignOutDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("确定要退出登录吗？");
        dialog.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (SignInActivity.instance != null) {
                    SignInActivity.instance.finish();
                    SignInActivity.instance = null;
                }

                SharedPreferences.Editor spEditor = getActivity().getSharedPreferences("UserInFo", Context.MODE_PRIVATE).edit();
                spEditor.clear();
                spEditor.commit();

                Intent intent = new Intent();
                intent.setClass(getActivity(), SignInActivity.class);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("点错了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
        });
        dialog.show();
    }
}
