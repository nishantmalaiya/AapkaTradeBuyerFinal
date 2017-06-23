package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.viewpageradapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.SalesTransactionData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.SalesTransactionMachine;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by PPC17 on 21-Jun-17.
 */

public class SalesTransactionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;
    public ArrayList<SalesTransactionMachine> itemList;
    public Activity context;
    private SalesTransactionReyclerHolder viewHolder;
    private ProgressBarHandler progressBarHandler;

    public SalesTransactionRecyclerAdapter(Context context, ArrayList<SalesTransactionMachine> itemList) {
        this.itemList = itemList;
        this.context = (Activity) context;
        inflater = LayoutInflater.from(context);
        progressBarHandler = new ProgressBarHandler(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.sales_transaction_row, parent, false);

        viewHolder = new SalesTransactionReyclerHolder(view);


        return viewHolder;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        final SalesTransactionReyclerHolder homeHolder = (SalesTransactionReyclerHolder) holder;

        StringBuilder stringBuilder = new StringBuilder("<font size=\"15\" " + "color=" + ContextCompat.getColor(context, R.color.green) + ">Txn ID." + itemList.get(position).TxnId + "</font>");
        stringBuilder.append("<br>").append("<i>" + itemList.get(position).benificalPayoutDate).append("</i>" + "<br><b><font color=" + ContextCompat.getColor(context, R.color.black) + "> Txn Date:" + "</font></b> " + itemList.get(position).TxnDate).append("<br>" + "<font color=" + ContextCompat.getColor(context, R.color.black) + ">").append("Sales Amount : </font>").append(" " + context.getString(R.string.rupay_text)).append(itemList.get(position).SalesAmount).append("<br>").append("07-june-2017<b>to</b>16-june-2017");
        String tvData = stringBuilder.toString();
        homeHolder.SalesMachineTxnData.setText(Html.fromHtml(tvData));
        homeHolder.TxnAmount.setText(itemList.get(position).TxnAmount);
        homeHolder.TxnStatus.setText(itemList.get(position).paymentStatus);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}