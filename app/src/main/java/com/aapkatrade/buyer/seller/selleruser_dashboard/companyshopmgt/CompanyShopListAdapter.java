package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt;

import android.content.Context;
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

    }



    @Override
    public CompanyShopDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CompanyShopDataHolder(LayoutInflater.from(context).inflate(R.layout.row_company_shop_mgt, parent, false));
    }

    @Override
    public void onBindViewHolder(CompanyShopDataHolder holder, int position) {
//        Iterator<CompanyShopData> itr=companyShopLinkedList.iterator();
//        while(itr.hasNext()){
//            AndroidUtils.showErrorLog(context,"************", itr.next());
//        }
        StringBuilder stringBuilder = new StringBuilder(companyShopLinkedList.get(position).getName());
        stringBuilder.append("<br>").append("<font color=\"#7dbd00\">").append(companyShopLinkedList.get(position).getCreated()).append("</font>").append("<br>").append("Products : ").append(companyShopLinkedList.get(position).getAddress());
        String tvData = stringBuilder.toString();
        AndroidUtils.showErrorLog(context,position+"---->  ", tvData);
        holder.tvCompanyShop.setText(Html.fromHtml(tvData));
//        holder.tvCompanyShop.setText("Hi List Data");
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