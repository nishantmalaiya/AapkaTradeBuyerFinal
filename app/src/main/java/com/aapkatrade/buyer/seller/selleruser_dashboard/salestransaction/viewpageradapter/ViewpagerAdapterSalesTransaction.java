package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.viewpageradapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.BillPaymentActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.BillPaymentAdapter;
import com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.SalesTransactionData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.SalesTransactionMachine;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by PPC21 on 13-Jan-17.
 */

public class ViewpagerAdapterSalesTransaction extends PagerAdapter {

    private Context mContext;

    CardView cardview_salesTransaction;
    ArrayList<SalesMachineResultData> machineDatas;
    RecyclerView RecyclerMachine;
    LinearLayoutManager linearLayoutManager;
    private SalesTransactionRecyclerAdapter salesTransactionAdapter;
    ViewPager viewpagerSalesTransaction;

    public static CommonInterface commonInterface;

    public ViewpagerAdapterSalesTransaction(Context mContext, ArrayList<SalesMachineResultData> machineDatas, ViewPager viewpagerSalesTransaction) {

        this.mContext = mContext;
        this.machineDatas = machineDatas;
        this.viewpagerSalesTransaction = viewpagerSalesTransaction;


    }


    public int getCount() {
        return machineDatas != null ? machineDatas.size() : -1;

    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.viewpager_sales_transaction, container, false);
        TextView TvSalesResultData = (TextView) itemView.findViewById(R.id.tv_salesTransactionResultData);
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        StringBuilder stringBuilder = new StringBuilder("<br><font size=\"20\" color=" + "#D9C356>Machine No. " + machineDatas.get(position).MachineNo + "/font>").append("<font size =\"15\"color=" + "#062B3D" + "><br>  " + machineDatas.get(position).ToDate + "</font size=\"15\" color=#00aaa0" + "> </font>" + "<font size=\"15\" color=\"#ffffff\">To</font>" + "<font size=\"15\" color=\"#062B3D\">" + machineDatas.get(position).FromDate + "</font>");
        stringBuilder.append("<br>").append("Txn Amount: " + mContext.getString(R.string.rupay_text) + machineDatas.get(position).TxnAmount).append("<br>").append("Sales Amount : ").append(" " + mContext.getString(R.string.rupay_text)).append(machineDatas.get(position).SalesAmount).append("<br>");
        String tvData = stringBuilder.toString();
        CircleIndicator circleIndicator = (CircleIndicator) itemView.findViewById(R.id.indicator_custom_sales_transaction);
        circleIndicator.setViewPager(viewpagerSalesTransaction);
        TvSalesResultData.setText(Html.fromHtml(tvData));


        TextView tv_salesTransactionResultStatus = (TextView) itemView.findViewById(R.id.tv_salesTransactionResultStatus);
        tv_salesTransactionResultStatus.setText("COMPLETED");
        RecyclerMachine = (RecyclerView) itemView.findViewById(R.id.recycleview_viewpager_salesReport);

        cardview_salesTransaction = (CardView) itemView.findViewById(R.id.cardview_salesTransaction);


        setUpViewBackground(cardview_salesTransaction);

        setUpRecycleView(RecyclerMachine, position);

        container.addView(itemView);

        return itemView;
    }

    private void setUpRecycleView(RecyclerView recyclerMachine, int position) {


        recyclerMachine.setLayoutManager(linearLayoutManager);


        salesTransactionAdapter = new SalesTransactionRecyclerAdapter(mContext, machineDatas.get(position).salesMachineResultDatas,machineDatas.get(position).FromDate,machineDatas.get(position).ToDate);
        recyclerMachine.setAdapter(salesTransactionAdapter);


    }


    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((NestedScrollView) object);
    }

    private void setUpViewBackground(CardView view) {

        int[] androidColors = mContext.getResources().getIntArray(R.array.random_color);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

        view.setCardBackgroundColor(randomAndroidColor);


    }


}