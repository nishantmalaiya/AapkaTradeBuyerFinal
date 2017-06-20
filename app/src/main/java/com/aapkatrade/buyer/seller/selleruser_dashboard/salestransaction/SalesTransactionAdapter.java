package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;

import java.util.List;

/**
 * Created by PPC17 on 13-Jun-17.
 */

public class SalesTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;
    private List<SalesTransactionData> itemList;
    public Activity context;
    private SalesTransactionListHolder viewHolder;
    private ProgressBarHandler progressBarHandler;

    public SalesTransactionAdapter(Activity context, List<SalesTransactionData> itemList) {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        progressBarHandler = new ProgressBarHandler(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.billhistory_row, parent, false);

        viewHolder = new SalesTransactionListHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        final SalesTransactionListHolder homeHolder = (SalesTransactionListHolder) holder;


        StringBuilder stringBuilder = new StringBuilder(itemList.get(position).paymentMode);
        stringBuilder.append("<br>").append("<font color=\"#7dbd00\"><i>").append(itemList.get(position).paymentDate).append("</i></font>").append("<br>").append("<b>Request Ref. No : </b>").append(" ").append(itemList.get(position).RequestRefNo).append("<br>").append("<b>Bank Ref. No : </b>").append(" ").append(itemList.get(position).BankRefNo)
        ;
        String tvData = stringBuilder.toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            homeHolder.tvBillhistories.setText(Html.fromHtml(tvData, 1));
        } else {
            //noinspection deprecation
            homeHolder.tvBillhistories.setText(Html.fromHtml(tvData));
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            homeHolder.tvBillhistories.setText(Html.fromHtml(tvData, 1));
        }
        homeHolder.tvBillhistoryAmount.setText(itemList.get(position).paymentAmount);
        homeHolder.tvPaymentStatus.setText(itemList.get(position).paymentStatus);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
