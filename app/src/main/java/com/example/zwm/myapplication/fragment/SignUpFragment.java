package com.example.zwm.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.zwm.myapplication.R;
import com.example.zwm.myapplication.activity.InputPostActivity;
import com.example.zwm.myapplication.util.HttpUtils;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link SignUpFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link SignUpFragment#newInstance} factory method to
// * create an instance of this fragment.
// *
// * 注册界面
// */
public class SignUpFragment extends Fragment {
    // view
    private EditText unameView;
    private EditText uemailaddressView;
    private EditText upasswordView;
    private EditText upasswordConfirmedView;
    private EditText uorganizationView;
    private EditText ucontactwayView;
    private Button signUpButton;
    // view

    private static final String SIGN_UP_URL = "http://182.254.247.94:8080/KeyanWeb/signupservlet";

//    private User user;
    private String uname;
    private String uemailaddress;
    private String upassword;
    private String upasswordconfirmed;
    private String uorganization;
    private String ucontactway;

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public SignUpFragment() {
//        // Required empty public constructor
//    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SignUpFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static SignUpFragment newInstance(String param1, String param2) {
//        SignUpFragment fragment = new SignUpFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = getActivity();

        initViews(activity);
        initEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    private void initViews(FragmentActivity activity) {
        unameView = (EditText) activity.findViewById(R.id.sign_up_uname);
        uemailaddressView = (EditText) activity.findViewById(R.id.sign_up_uemailaddress);
        upasswordView = (EditText) activity.findViewById(R.id.sign_up_upassword);
        upasswordConfirmedView = (EditText) activity.findViewById(R.id.sign_up_uemailaddress_confirmed);
        uorganizationView = (EditText) activity.findViewById(R.id.sign_up_uorganization);
        ucontactwayView = (EditText) activity.findViewById(R.id.sign_up_ucontactway);
        signUpButton = (Button) activity.findViewById(R.id.sign_up_button);
    }

    private void initEvents() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MyDebug", "Clicked");
                try {
                    uname = unameView.getText().toString();
                    uemailaddress = uemailaddressView.getText().toString();
                    upassword = upasswordView.getText().toString();
                    upasswordconfirmed = upasswordConfirmedView.getText().toString();
                    uorganization = uorganizationView.getText().toString();
                    ucontactway = ucontactwayView.getText().toString();

                    // 邮箱地址不能为空
                    String regEx = "[a-zA-Z_0-9]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
                    if (uemailaddress.equals("")) {
                        uemailaddressView.setText("邮箱地址不能为空!");
                        uemailaddressView.setTextColor(Color.parseColor("#ff0000"));
                        return;
                    }
                    // 邮箱地址不符合规则
                    else if (!uemailaddress.matches(regEx)) {
                        uemailaddressView.setText("邮箱地址格式不正确!");
                        uemailaddressView.setTextColor(Color.parseColor("#ff0000"));
                        return;
                    }

                    // 密码不能为空
                    if (upassword.equals("")) {
                        upasswordView.setText("密码不能为空!");
                        upasswordView.setTextColor(Color.parseColor("#ff0000"));
                        return;
                    }

                    // 确认密码不能为空
                    if (upasswordconfirmed.equals("")) {
                        upasswordConfirmedView.setText("请再次填写密码!");
                        upasswordConfirmedView.setTextColor(Color.parseColor("#ff0000"));
                        return;
                    }

                    // 两次密码必须一致
                    if (!upassword.equals(upasswordconfirmed)) {
                        upasswordConfirmedView.setText("两次密码必须一致!");
                        upasswordConfirmedView.setTextColor(Color.parseColor("#ff0000"));
                        return;
                    }

                    // 单位不能为空
                    if (uorganization.equals("")) {
                        uorganizationView.setText("单位不能为空!");
                        uorganizationView.setTextColor(Color.parseColor("#ff0000"));
                        return;
                    }

                    // 联系方式不能为空
                    if (ucontactway.equals("")) {
                        ucontactwayView.setText("联系方式不能为空!");
                        ucontactwayView.setTextColor(Color.parseColor("#ff0000"));
                        return;
                    }

                    // 没有以上问题（通过所有判断），现在可以上传至服务器，进行最后一道判断：邮箱是否已存在（即是否可用）
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();

                            String postString = "uname=" + uname
                                    + "&uemailaddress=" + uemailaddress
                                    + "&upassword=" + upassword
                                    + "&uorganization=" + uorganization
                                    + "&ucontactway=" + ucontactway;

                            String result = HttpUtils.post(SIGN_UP_URL, postString);
                            // 返回信息为"failed"，说明邮箱已存在，作提示
                            if (result.contains("failed")) {
                                Log.d("MyDebug", "[result-failed]: " + result);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        uemailaddressView.setText("该邮箱已存在!");
                                        uemailaddressView.setTextColor(Color.parseColor("#ff0000"));
                                    }
                                });
                            }
                            // 返回信息不是"failed"，即"success"，本次注册成功，随后跳转到功能界面
                            else {
                                Log.d("MyDebug", "[result-success]: " + result);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferences.Editor spEditor = getActivity().getSharedPreferences("SignInInFo", Context.MODE_PRIVATE).edit();
                                        spEditor.putString("uemailaddress", uemailaddress);
                                        spEditor.putString("upassword", upassword);
                                        spEditor.commit();

                                        Intent intent = new Intent();
                                        intent.setClass(getActivity(), InputPostActivity.class);
                                        getActivity().startActivity(intent);
                                    }
                                });
                            } // else
                        } // run()
                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("MyDebug", "完毕");
            }
        });
    }



    //    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
