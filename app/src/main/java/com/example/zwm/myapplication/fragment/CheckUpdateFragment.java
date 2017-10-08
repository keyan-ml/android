package com.example.zwm.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.util.HttpUtils;

public class CheckUpdateFragment extends Fragment {
    private static final String ROOT_URL_PATH = "http://182.254.247.94:8080/KeyanWeb";
    private static final String CHECKUPDATE_SERVLET_URL_PATH = ROOT_URL_PATH + "/checkupdateservlet";

    private ImageView backView;
    private TextView showInfoView;

    private String version;
    private String postString;
    private String result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check_update, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews(getActivity());
        initEvents(getActivity());
    }

    private void initViews(FragmentActivity activity) {
        backView = (ImageView) activity.findViewById(R.id.check_update_back_view);
        showInfoView = (TextView) activity.findViewById(R.id.check_update_show_info);

        version = "1.0";
    }

    private void initEvents(final FragmentActivity activity) {
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    postString = "version=" + version;
                    result = HttpUtils.post(CHECKUPDATE_SERVLET_URL_PATH, postString);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result == null || result.contains("failed")) {
                                showInfoView.setText("获取版本号失败！");
                                return;
                            }
                            if (result.contains("true")) {
                                showInfoView.setText("已是最新版");
                            }
                            else {
                                showInfoView.setText("最新版本为" + result + "\n请及时更新");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
