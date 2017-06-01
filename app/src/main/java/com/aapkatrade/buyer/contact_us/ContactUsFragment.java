package com.aapkatrade.buyer.contact_us;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ContactUsFragment extends Fragment {

    EditText etSubject, etUserName, etMobileNo, etEmail, etQuery;
    Button buttonSave;
    ProgressBarHandler progress_handler;
    ImageView imgPhone, imgEmail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        progress_handler = new ProgressBarHandler(getContext());
        initView(view);
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String subject = etSubject.getText().toString();
                String username = etUserName.getText().toString();
                String mobileno = etMobileNo.getText().toString();
                String email = etEmail.getText().toString();
                String query = etQuery.getText().toString();
                if (!subject.equals("")) {

                    if (!username.equals("")) {

                        if (!mobileno.equals("")) {

                            if (mobileno.length() == 10) {

                                if (!email.equals("")) {

                                    if (Validation.isValidEmail(email)) {
                                        if (!query.equals("")) {

                                            callContactUsWebService(subject, username, mobileno, email, query);

                                        } else {

                                            AndroidUtils.showToast(getContext(), "Please Enter Query");
                                        }

                                    } else {
                                        AndroidUtils.showToast(getContext(), "Please Enter Valid Email Address");
                                    }
                                } else {
                                    AndroidUtils.showToast(getContext(), "Please Enter Email Address");
                                }

                            } else {
                                AndroidUtils.showToast(getContext(), "Please Enter 10 digit Mobile Number");
                            }

                        } else {
                            AndroidUtils.showToast(getContext(), "Please Enter Mobile Number");
                        }

                    } else {
                        AndroidUtils.showToast(getContext(), "Please Enter User Name");
                    }

                } else {
                    AndroidUtils.showToast(getContext(), "Please Enter Subject");
                }

            }
        });

        return view;
    }

    private void initView(View v) {
        imgPhone = (ImageView) v.findViewById(R.id.imgPhone);
        imgEmail = (ImageView) v.findViewById(R.id.imgEmail);
        AndroidUtils.setImageColor(imgEmail, getActivity(), R.color.black);
        AndroidUtils.setImageColor(imgPhone, getActivity(), R.color.black);
        etSubject = (EditText) v.findViewById(R.id.etSubject);
        etUserName = (EditText) v.findViewById(R.id.etUserName);
        etMobileNo = (EditText) v.findViewById(R.id.etMobileNo);
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        etQuery = (EditText) v.findViewById(R.id.etQuery);
        buttonSave = (Button) v.findViewById(R.id.buttonSave);


    }


    private void callContactUsWebService(String subject, String username, String mobile, String email, String query) {
        progress_handler.show();
        Ion.with(getActivity())
                .load(getResources().getString(R.string.webservice_base_url) + "/contact_us")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_name", username)
                .setBodyParameter("email", email)
                .setBodyParameter("mobile_num", mobile)
                .setBodyParameter("message", query)
                .setBodyParameter("subject", subject)
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(getActivity()))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {

                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null) {

                            progress_handler.hide();
                        } else {
                            JsonObject jsonObject = result.getAsJsonObject();


                            if(jsonObject.get("error").getAsString().contains("false"))
                            {
                                String message = jsonObject.get("message").getAsString();
                                AndroidUtils.showToast(getActivity(),message);

                            }
                            Log.e("message", jsonObject.toString());

                            etSubject.setText("");
                            etUserName.setText("");
                            etMobileNo.setText("");
                            etEmail.setText("");
                            etQuery.setText("");

                            progress_handler.hide();

                        }
                    }
                });
    }


}
