package com.aapkatrade.buyer.general.Utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.buyerregistration.entity.City;
import com.aapkatrade.buyer.home.buyerregistration.entity.State;
import com.aapkatrade.buyer.home.buyerregistration.spinner_adapter.SpCityAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by PPC15 on 8/18/2017.
 */

public class StateUtils {
    private static ArrayList<State> stateList;
    private static Context context;
    private static boolean isLoded = false;
    private static ProgressBarHandler progressBarHandler;

    public static ArrayList<State> getStateList(Context context) {
        StateUtils.context = context;
        if (stateList == null) {
            for (int i = 0; i < 3; i++) {
                loadStateAPI();
                if(isLoded){
                    return stateList;
                }
            }
        }
        return null;
    }


    private static void loadStateAPI() {
        progressBarHandler = new ProgressBarHandler(context);
        stateList = new ArrayList<>();
        progressBarHandler.show();
        Ion.with(context)
                .load("http://aapkatrade.com/slim/dropdown")
                .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "state")
                .setBodyParameter("id", "101")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        AndroidUtils.showErrorLog(context, "StateList  ", result);

                        if (result != null) {
                            JsonArray jsonResultArray = result.getAsJsonArray("result");

                            State stateEntity_init = new State("-1", "Please Select State");
                            stateList.add(stateEntity_init);

                            for (int i = 0; i < jsonResultArray.size(); i++) {
                                JsonObject jsonObject1 = (JsonObject) jsonResultArray.get(i);
                                State stateEntity = new State(jsonObject1.get("id").getAsString(), jsonObject1.get("name").getAsString());
                                stateList.add(stateEntity);
                            }
                            if (stateList.size() > 1) {
                                isLoded = true;
                            }

                        } else {
                            AndroidUtils.showErrorLog(context, "StateList  ", "  No state found.");
                        }
                    }

                });
    }
}
