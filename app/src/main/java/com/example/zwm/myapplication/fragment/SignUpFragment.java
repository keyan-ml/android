package com.example.zwm.myapplication.fragment;

import android.content.Context;
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
import com.example.zwm.myapplication.model.User;
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
    private EditText uorganizationView;
    private EditText ucontactwayView;
    private Button commit;
    // view

    private static final String SIGN_UP_URL = "http://182.254.247.94:8080/KeyanWeb/signupservlet";

//    private User user;
    private String uname;
    private String uemailaddress;
    private String upassword;
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
        uorganizationView = (EditText) activity.findViewById(R.id.sign_up_uorganization);
        ucontactwayView = (EditText) activity.findViewById(R.id.sign_up_ucontactway);
        commit = (Button) activity.findViewById(R.id.sign_up_button);
    }

    private void initEvents() {
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MyDebug", "Clicked");
                try {
                    uname = unameView.getText().toString();
                    uemailaddress = uemailaddressView.getText().toString();
                    upassword = upasswordView.getText().toString();
                    uorganization = uorganizationView.getText().toString();
                    ucontactway = ucontactwayView.getText().toString();

                    if (uemailaddress.equals("")) {
                        uemailaddressView.setText("邮箱地址不能为空!");
                        uemailaddressView.setTextColor(Color.parseColor("#ff0000"));
                    }

                    new Thread(){
                        @Override
                        public void run() {
                            super.run();

                            String postString = "uname=" + uname
                                    + "&uemailaddress=" + uemailaddress
                                    + "&upassword=" + upassword
                                    + "&uorganization=" + uorganization
                                    + "&ucontactway=" + ucontactway;

                            String resultFromSignUpServlet = HttpUtils.post(SIGN_UP_URL, postString);
                            Log.d("MyDebug", resultFromSignUpServlet);
                        }
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
