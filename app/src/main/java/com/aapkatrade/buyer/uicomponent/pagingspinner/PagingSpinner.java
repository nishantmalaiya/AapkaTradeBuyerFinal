package com.aapkatrade.buyer.uicomponent.pagingspinner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.dialogs.ChatDialogFragment;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.CompanyDropdownDatas;
import com.aapkatrade.buyer.uicomponent.pagingspinner.dialog.PagingSpinnerDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.StreamBody;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by PPC15 on 7/19/2017.
 */

public class PagingSpinner extends RelativeLayout {
    private Context context;
    private AppSharedPreference appSharedpreference;
    private View view;
    private ProgressBarHandler progressBarHandler;
    private RelativeLayout relativeLayoutRoot;
    private String shopType = "0", sellerId = "0";
    private PagingSpinnerDialog pagingSpinnerDialog;

    public PagingSpinner(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PagingSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public PagingSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @SuppressLint("NewApi")
    public PagingSpinner(final Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();


    }

    protected void init() {
        appSharedpreference = new AppSharedPreference(context);
        if (this.isInEditMode()) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layoutId(), this, true);
        progressBarHandler = new ProgressBarHandler(context);
        relativeLayoutRoot = (RelativeLayout) view.findViewById(R.id.linearLayoutRoot);
        AndroidUtils.setBackgroundStroke(relativeLayoutRoot, context, R.color.green, 10, 3);
        relativeLayoutRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.showErrorLog(context, "relativeLayoutRoot clicked.");
                pagingSpinnerDialog = new PagingSpinnerDialog(context, shopType, sellerId);
                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                pagingSpinnerDialog.show(fm, "PagingSpinnerDialog");
            }
        });


//        if()
    }


    private int layoutId() {
        return R.layout.paging_spinner_container;
    }

    public void setShopType(int shopType) {
        this.shopType = String.valueOf(shopType);
    }

    public String getShopType() {
        return Validation.isEmptyStr(this.shopType)?"0": this.shopType;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerId() {
        return Validation.isEmptyStr(this.sellerId)?"0": this.sellerId;
    }


}
