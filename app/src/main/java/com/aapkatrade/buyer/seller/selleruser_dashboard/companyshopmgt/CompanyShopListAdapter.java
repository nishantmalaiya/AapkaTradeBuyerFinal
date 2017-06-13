package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.serviceenquiryList.ServiceEnquiryActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.serviceenquiryList.ServiceEnquiryData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.serviceenquiryList.ServiceEnquiryHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by PPC16 on 10-Mar-17.
 */


public class CompanyShopListAdapter extends RecyclerView.Adapter<CompanyShopDataHolder> {

    private Context context;
    private LinkedList<CompanyShopData> companyShopLinkedList;
    private AppSharedPreference appSharedpreference;
    private ProgressBarHandler progressHandler;
    public CompanyShopListAdapter(Context context, LinkedList<CompanyShopData> companyShopLinkedList) {
        this.context = context;
        this.companyShopLinkedList = companyShopLinkedList;
        appSharedpreference = new AppSharedPreference(context);
        progressHandler = new ProgressBarHandler(context);
//        AndroidUtils.showErrorLog(context, " The size of list is  ", companyShopLinkedList.size());
    }



    @Override
    public CompanyShopDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CompanyShopDataHolder(LayoutInflater.from(context).inflate(R.layout.row_company_shop_mgt, parent, false));
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(CompanyShopDataHolder holder, int position) {
        StringBuilder stringBuilder = new StringBuilder(companyShopLinkedList.get(position).getName());
        stringBuilder.append("<br>").append("<font color=\"#7dbd00\"><i>").append(companyShopLinkedList.get(position).getCreated()).append("</i></font>").append("<br>").append("Products : ").append(companyShopLinkedList.get(position).getProductCount());
        String tvData = stringBuilder.toString();
        AndroidUtils.showErrorLog(context,position+"---->  ", tvData);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvCompanyShop.setText(Html.fromHtml(tvData, Html.FROM_HTML_MODE_LEGACY));
        } else {
            //noinspection deprecation
            holder.tvCompanyShop.setText(Html.fromHtml(tvData));
        }
        holder.rlAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.rlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }




    @Override
    public int getItemCount() {
        return companyShopLinkedList.size();
    }


}