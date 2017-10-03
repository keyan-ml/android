package com.example.zwm.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
// * {@link SignInFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link SignInFragment#newInstance} factory method to
// * create an instance of this fragment.
// *
// * 登录界面
// */
public class SignInFragment extends Fragment {
    // view
    private EditText uemailaddressView;
    private EditText upasswordView;
    private Button signInButton;
    // view

    private static final String SIGN_IN_URL = "http://182.254.247.94:8080/KeyanWeb/signinservlet";

    private String uemailaddress;
    private String upassword;

    private String resultFromPost;


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

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SignInFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static SignInFragment newInstance(String param1, String param2) {
//        SignInFragment fragment = new SignInFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    private void initViews(FragmentActivity activity) {
        uemailaddressView = (EditText) activity.findViewById(R.id.sign_in_uemailaddress);
        upasswordView = (EditText) activity.findViewById(R.id.sign_in_upassword);
        signInButton = (Button) activity.findViewById(R.id.sign_in_button);
    }

    private void initEvents() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 登录按钮
                uemailaddress = uemailaddressView.getText().toString();
                upassword = upasswordView.getText().toString();

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

                // 合法邮箱和密码，准备登录检测
                new Thread(){
                    @Override
                    public void run() {
                        super.run();

                        String postString = "uemailaddress=" + uemailaddress
                                + "&upassword=" + upassword;
                        resultFromPost = HttpUtils.post(SIGN_IN_URL, postString);
                        // 返回信息中有"failed"，为出错，具体错误需再次判断
                        if (resultFromPost.contains("failed")) {
                            Log.d("MyDebug", "[result-failed]: " + resultFromPost);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    String[] resultArr = resultFromPost.split("_");
                                    if (resultFromPost.contains("emailaddress")) {
                                        uemailaddressView.setText("该邮箱未注册！");
                                        uemailaddressView.setTextColor(Color.parseColor("#ff0000"));
                                        return;
                                    }
                                    if (resultFromPost.contains("password")) {
                                        upasswordView.setText("密码错误！");
                                        upasswordView.setTextColor(Color.parseColor("#ff0000"));
                                        return;
                                    }

                                }
                            });

                        }
                        // 返回信息中不含有"failed"字样，说明登录成功
                        else {
                            Log.d("MyDebug", "[result-success]: " + resultFromPost);
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
                        }

                    }
                }.start();
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
