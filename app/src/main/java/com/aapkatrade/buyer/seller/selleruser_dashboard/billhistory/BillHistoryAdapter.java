package com.aapkatrade.buyer.seller.selleruser_dashboard.billhistory;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;

import java.util.List;

/**
 * Created by PPC17 on 13-Jun-17.
 */

public class BillHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;
    private List<BillHistoryData> itemList;
    public Activity context;
    private BillHistoryListHolder viewHolder;
    private ProgressBarHandler progressBarHandler;

    public BillHistoryAdapter(Activity context, List<BillHistoryData> itemList) {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        progressBarHandler = new ProgressBarHandler(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.billhistory_row, parent, false);

        viewHolder = new BillHistoryListHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        final BillHistoryListHolder homeHolder = (BillHistoryListHolder) holder;


        StringBuilder stringBuilder = new StringBuilder("<b>" + "PAYMENT MODE: " + "<font color=\"#7dbd00\"><i>" + itemList.get(position).paymentMode + "</i></font>" + "</b>");
        stringBuilder.append("<br>").append("<b>" + "PAYMENT DATE: " + "<font color=\"#7dbd00\"><i>").append(itemList.get(position).paymentDate).append("</i></font>" + "</b>");
        stringBuilder.append("<br>").append("<b>" + "PAYMENT STATUS: " + "<font color=\"#7dbd00\"><i>").append(itemList.get(position).paymentStatus).append("</i></font>" + "</b>");
        stringBuilder.append("<br>").append("<b>" + "PAYMENT AMOUNT: " + "<font color=\"#7dbd00\"><i>").append(context.getString(R.string.rupay_text)).append(itemList.get(position).paymentAmount).append("</i></font>" + "</b>");
        String tvData = stringBuilder.toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            homeHolder.tvBillhistories.setText(Html.fromHtml(tvData, 1));
        } else {
            //noinspection deprecation
            homeHolder.tvBillhistories.setText(Html.fromHtml(tvData));
        }
homeHolder.tvPaymentDate.setText(itemList.get(position).paymentDate.substring(0,10));
        homeHolder.tvPaymentTime.setText(itemList.get(position).paymentDate.substring(11));

        homeHolder.tvBillhistoryAmount.setText(context.getString(R.string.rupay_text)+itemList.get(position).paymentAmount);
        homeHolder.tvPaymentStatus.setText(itemList.get(position).paymentStatus);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
