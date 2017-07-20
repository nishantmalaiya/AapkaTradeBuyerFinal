package com.aapkatrade.buyer.uicomponent.pagingspinner.dialog;


import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressDialogHandler;
import com.aapkatrade.buyer.login.ActivityOTPVerify;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.CompanyDropdownDatas;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PagingSpinnerDialog extends DialogFragment {
    private Context context;
    private ImageView dialogClose;
    private ProgressDialogHandler progressDialogHandler;
    private ArrayList arrayList;
    private String shopType = "0", sellerId = "0";
    private int page = 0, totalPage = 0;

    public PagingSpinnerDialog(Context context, String shopType, String sellerId) {
        this.context = context;
        this.shopType = shopType;
        this.sellerId = sellerId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_track_order_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        initview(v);

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }

    private void initview(View v) {
        progressDialogHandler = new ProgressDialogHandler(context);
        dialogClose = (ImageView) v.findViewById(R.id.dialog_close);
    }


    private void callCompanyListWebservice(final int page) {
        progressDialogHandler.show();
        Ion.with(context)
                .load(context.getString(R.string.webservice_base_url).concat("/shoplist"))
                .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("seller_id", sellerId)
                .setBodyParameter("lat", "0")
                .setBodyParameter("long", "0")
                .setBodyParameter("apply", "1")
                .setBodyParameter("shoptype", this.shopType)
                .setBodyParameter("page", String.valueOf(page))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                progressDialogHandler.hide();
                if (result != null) {
                    if (result.get("error").getAsString().contains("false")) {
                        JsonArray jsonArray_response = result.getAsJsonArray("result");
                        totalPage = result.get("total_page").getAsInt();
                        if (page == 1) {
                            arrayList.add(new CompanyDropdownDatas("", "", "", ""));
                        }
                        for (int i = 0; i < jsonArray_response.size(); i++) {


                            JsonObject jsonObject = jsonArray_response.get(i).getAsJsonObject();
                            String companyId = jsonObject.get("id").getAsString();
                            String companyImageUrl = jsonObject.get("image_url").getAsString();
                            String companyName = jsonObject.get("name").getAsString();
                            String comapanyCategory = jsonObject.get("category_name").getAsString();

                            arrayList.add(new CompanyDropdownDatas(companyId, companyImageUrl, companyName, comapanyCategory));

                        }


                    }


                }


                AndroidUtils.showErrorLog(context, "ShopListResponse", result);


            }
        });
    }




}
