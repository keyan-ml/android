package com.example.zwm.myapplication.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.util.HttpUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by zwm on 2017/10/4.
 */

public class FileUploadFragment extends Fragment implements View.OnClickListener {
    private static final String ROOT_URL_PATH = "http://182.254.247.94:8080/KeyanWeb";
    private static final String PUSERVLET_URL_PATH = ROOT_URL_PATH + "/puservlet";
    private final String TRANSESERVLET_URL_PATH = ROOT_URL_PATH + "/transeservlet";
    private static String RESULE_FILE_PATH;

    private TextView filePathView;
    private Button commitButton;
    private TextView errorInfoView;

    private String fileContent;
    private String filePath;

    private boolean truePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file_upload, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews(getActivity());
        initEvents(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                filePath = getRealFilePath(getContext(), uri);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (filePath.endsWith(".csv")) { // 选择了csv文件
                            truePath = true;
                            filePathView.setText(filePath.substring(19, filePath.length()));
                        }
                        else { // 选择的不是csv文件
                            filePathView.setText("请选择csv文件！");
                            truePath = false;
                        }

                    }
                });
                Log.d("MyDebug",  "uri：" + uri + "\n路径：" + filePath);
            }
        }
    }

    private void initViews(FragmentActivity activity) {
        truePath = false;

        filePathView = (TextView) activity.findViewById(R.id.file_upload_file_path);
        commitButton = (Button) activity.findViewById(R.id.file_upload_commit_button);

        errorInfoView = (TextView) activity.findViewById(R.id.file_upload_error_info);
    }

    private void initEvents(FragmentActivity activity) {
        filePathView.setOnClickListener(this);
        commitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.file_upload_file_path:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    errorInfoView.setText("找不到文件管理器！请您下载文件管理器。");
                    e.printStackTrace();
                }
                break;
            case R.id.file_upload_commit_button:
                if (truePath) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorInfoView.setText("提交成功！\n正在处理...");
                        }
                    });
                    fileUpload(getActivity());
                    Log.d("MyDebug", "提交文件" + filePathView.getText());
                }
                break;
        }
    }

    private void fileUpload(final FragmentActivity activity) {
        try {
            BufferedReader bufr = new BufferedReader( new FileReader(filePath) );
            String str = "";
            fileContent = "";
            while ((str = bufr.readLine()) != null) {
                fileContent += str;
            }
            final String postString = "sents=" + fileContent;
            new Thread(){
                @Override
                public void run() {
                    super.run();

                    try {
                        String result = HttpUtils.post(PUSERVLET_URL_PATH, postString);
                        Log.d("MyDebug", "result: " + result);
                        if (result != null) {
                            String[] partArr = result.split("\\|");
                            String[] counts = partArr[0].split(" ");

                            String[] sents = fileContent.split("[。？！]");
                            for (int i = 0; i < sents.length; i++) {
                                if (!sents[i].equals("")) {
                                    sents[i] = "-1|" + sents[i];
                                }
                            }

                            boolean transE = false;
                            String[] pairs = null;
                            if (!counts[0].equals("0")) {
                                String[] positions = partArr[1].split(" ");
                                for (String p : positions) {
                                    int index = Integer.parseInt(p);
                                    if (!sents[index].equals("")) {
                                        sents[index] = sents[index].substring(1, sents[index].length());
                                    }
                                }

                                String postString2 = "positions=" + partArr[1]
                                        + "&sents=" + fileContent;
                                result = HttpUtils.post(TRANSESERVLET_URL_PATH, postString2);
                                pairs = result.split("\\|");
                                if (pairs[0].equals("ERROR")) { // 出错
                                    transE = false;
                                }
                                else {
                                    transE = true;
                                }

                            }
                            RESULE_FILE_PATH = "/storage/emulated/0/_ml-NLP";
                            SharedPreferences sp = activity.getSharedPreferences("Path", Context.MODE_PRIVATE);
                            if (sp != null) {
                                String defaultStoragePath = sp.getString("DefaultStoragePath", null);
                                if (defaultStoragePath != null) {
                                    RESULE_FILE_PATH = defaultStoragePath;
                                }
                            }

                            File dir = new File(RESULE_FILE_PATH);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            Log.d("MyDebug", RESULE_FILE_PATH);

                            BufferedWriter bufw = new BufferedWriter(new FileWriter(RESULE_FILE_PATH + "/result.txt"));

                            bufw.write("# PU-Learning");
                            bufw.flush();
                            bufw.newLine();
                            for (String sent : sents) {
                                if (!sent.equals("")) {
                                    bufw.write(sent);
                                    bufw.flush();
                                    bufw.newLine();
                                }
                            }

                            if (transE) {
                                bufw.newLine();
                                bufw.newLine();
                                bufw.newLine();
                                bufw.write("# TransE");
                                bufw.flush();
                                bufw.newLine();

                                for (String pair : pairs) {
                                    bufw.write(pair);
                                    bufw.flush();
                                    bufw.newLine();
                                }
                            }
                            bufw.close();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    errorInfoView.setText("处理完成!\n结果保存至：" + RESULE_FILE_PATH + "/result.txt");
                                }
                            });
                            Log.d("MyDebug", "分析完成，结果已保存。");


                        }
                        else {
                            Log.d("MyDebug", "服务器返回信息为null");
                        }
                    } catch (Exception e) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                errorInfoView.setText("出错！\n请检查您的输入文件。");
                            }
                        });
                        Log.d("MyDebug", "出错");
                        e.printStackTrace();
                    }
                }
            }.start();

        } catch (Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    errorInfoView.setText("抱歉！提交失败");
                }
            });
            e.printStackTrace();
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        }
        else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.MediaColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.MediaColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
