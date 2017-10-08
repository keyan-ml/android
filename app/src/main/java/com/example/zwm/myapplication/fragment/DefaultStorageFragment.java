package com.example.zwm.myapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zwm.myapplication.R;

import java.io.File;

public class DefaultStorageFragment extends Fragment implements View.OnClickListener {
    private ImageView backView;
    private EditText dirPathView;
    private Button commitButton;
    private TextView errorInfoView;

    private String dirPath = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_default_storage, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews(getActivity());
        initEvents(getActivity());
    }

    private void initViews(FragmentActivity activity) {
        backView = (ImageView) activity.findViewById(R.id.default_storage_back_view);
        dirPathView = (EditText) activity.findViewById(R.id.default_storage_dir_path);
        commitButton = (Button) activity.findViewById(R.id.default_storage_commit_button);
        errorInfoView = (TextView) activity.findViewById(R.id.default_storage_error_info);

        SharedPreferences sp =activity.getSharedPreferences("Path", Context.MODE_PRIVATE);
        dirPathView.setText(sp.getString("DefaultStoragePath", null));
    }

    private void initEvents(FragmentActivity activity) {
        backView.setOnClickListener(this);
        dirPathView.setOnClickListener(this);
        commitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.default_storage_back_view:
            getActivity().finish();
            break;
        case R.id.default_storage_commit_button:
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String inputPath = dirPathView.getText().toString();
                        if (inputPath.equals("")) {
                            errorInfoView.setText("路径不能为空！");
                            return;
                        }
                        dirPath = "/storage/emulated/0" + (inputPath.startsWith("/")?"":"/") + inputPath;
                        File dir = new File(dirPath);
                        if (!dir.exists()) dir.mkdirs();

                        SharedPreferences.Editor spEditor = getActivity().getSharedPreferences("Path", Context.MODE_PRIVATE).edit();
                        spEditor.putString("DefaultStoragePath", dirPath);
                        spEditor.commit();

                        errorInfoView.setText("保存成功！");
                    } catch (Exception e) {
                        errorInfoView.setText("保存失败！");
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
