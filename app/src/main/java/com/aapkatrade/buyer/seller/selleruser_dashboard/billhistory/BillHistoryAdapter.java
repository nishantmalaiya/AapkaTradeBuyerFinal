package com.aapkatrade.buyer.seller.selleruser_dashboard.billhistory;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.BillPaymentActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.BillPaymentListData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.BillPaymentListHolder;

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







    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
