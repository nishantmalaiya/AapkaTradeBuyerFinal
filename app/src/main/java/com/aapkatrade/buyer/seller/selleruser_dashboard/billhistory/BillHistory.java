package com.aapkatrade.buyer.seller.selleruser_dashboard.billhistory;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class BillHistory extends AppCompatActivity {

    AppSharedPreference appSharedPreference;
    Context c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_history);


        initView();

        callWebserviceMachineList();
        callWebserviceBillHistory();


    }

    private void callWebserviceMachineList() {

        String MachineListWebservice = getString(R.string.webservice_base_url) + "/get_machine";


        Ion.with(this)
                .load(MachineListWebservice)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("seller_id", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                if (result != null) {

                    if (result.get("error").getAsString().contains("false"))

                    {

                        AndroidUtils.showErrorLog(c, "responseMachineList", result.toString());

                    } else {

                        AndroidUtils.showErrorLog(c, "responseMachineList", result.toString());
                    }


                }


            }
        });


    }

    private void initView() {
        c = this;
        appSharedPreference = new AppSharedPreference(this);
    }

    private void callWebserviceBillHistory() {

        String BillHistoryWebservice = getString(R.string.webservice_base_url) + "/sellerTransaction";


        Ion.with(this)
                .load(BillHistoryWebservice).setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("seller_id", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()))
                .setBodyParameter("machine_no", "15456324")
                .setBodyParameter("from_date", "2017-01-01")
                .setBodyParameter("to_date", "2017-06-13")
                .setBodyParameter("page", "1")
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                if (result != null) {

                    if (result.get("error").getAsString().contains("false"))

                    {

                        AndroidUtils.showErrorLog(c, "responseBillPayment", result.toString());

                    } else {

                        AndroidUtils.showErrorLog(c, "responseBillPayment2", result.toString());
                    }


                }


            }
        });


    }


}
