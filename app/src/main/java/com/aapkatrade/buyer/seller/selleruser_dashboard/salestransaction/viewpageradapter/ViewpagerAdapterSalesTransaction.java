package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.viewpageradapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by PPC21 on 13-Jan-17.
 */

public class ViewpagerAdapterSalesTransaction extends PagerAdapter
{

    private Context mContext;
    CardView cardview_salesTransaction;
    ArrayList<SalesMachineResultData> machineDatas;
    RecyclerView RecyclerMachine;
    LinearLayoutManager linearLayoutManager;
    private SalesTransactionRecyclerAdapter salesTransactionAdapter;
    ViewPager viewpagerSalesTransaction;

    public static CommonInterface commonInterface;

    public ViewpagerAdapterSalesTransaction(Context mContext, ArrayList<SalesMachineResultData> machineDatas, ViewPager viewpagerSalesTransaction)
    {

        this.mContext = mContext;
        this.machineDatas = machineDatas;
        this.viewpagerSalesTransaction = viewpagerSalesTransaction;
    }


    public int getCount()
    {
        return machineDatas != null ? machineDatas.size() : -1;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public Object instantiateItem(ViewGroup container, int position)
    {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.viewpager_sales_transaction, container, false);
        TextView TvSalesResultData = (TextView) itemView.findViewById(R.id.tv_salesTransactionResultData);
        TextView tv_salesTransactionFromDateToDate = (TextView) itemView.findViewById(R.id.tv_salesTransactionFromDateToDate);
        TextView tv_salesTransactionTxtAmount = (TextView) itemView.findViewById(R.id.tv_salesTransactionTxtAmount);
        TextView tv_salesTransactionSalesAmount = (TextView) itemView.findViewById(R.id.tv_salesTransactionSalesAmount);

        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        ImageView imgstatus = (ImageView) itemView.findViewById(R.id.imgstatus);
        RelativeLayout rl_salestransactionstatus = (RelativeLayout) itemView.findViewById(R.id.rl_salestransactionstatus);

        AndroidUtils.setBackgroundStroke(rl_salestransactionstatus, mContext, R.color.white, GradientDrawable.OVAL, 10, 4, R.color.white);

        System.out.println("data--------------"+AndroidUtils.stringToCalender(machineDatas.get(position).ToDate ));

        StringBuilder stringBuilder = new StringBuilder("<font color=" + "#062B3D" + ">" +formateDateFromstring(machineDatas.get(position).FromDate)+ " </font>" + "<font  color=\"#ffffff\">&nbsp;To</font>" + "&nbsp;" + "<font  color=\"#062B3D\">" +formateDateFromstring(machineDatas.get(position).ToDate) + "</font>");

        String tvData = stringBuilder.toString();
        CircleIndicator circleIndicator = (CircleIndicator) itemView.findViewById(R.id.indicator_custom_sales_transaction);
        circleIndicator.setViewPager(viewpagerSalesTransaction);

        TvSalesResultData.setText("Machine ID. "+machineDatas.get(position).MachineNo);

        tv_salesTransactionFromDateToDate.setText(Html.fromHtml(tvData));
        tv_salesTransactionTxtAmount.setText("Txn Amount:"+ mContext.getString(R.string.rupay_text)+ machineDatas.get(position).TxnAmount);
        tv_salesTransactionSalesAmount.setText("Sales Amount:"+ mContext.getString(R.string.rupay_text)+ machineDatas.get(position).SalesAmount);

        TextView tv_salesTransactionResultStatus = (TextView) itemView.findViewById(R.id.tv_salesTransactionResultStatus);
        tv_salesTransactionResultStatus.setText("COMPLETED");
        RecyclerMachine = (RecyclerView) itemView.findViewById(R.id.recycleview_viewpager_salesReport);

        cardview_salesTransaction = (CardView) itemView.findViewById(R.id.cardview_salesTransaction);

        setUpViewBackground(cardview_salesTransaction);

        setUpRecycleView(RecyclerMachine, position);

        container.addView(itemView);

        return itemView;
    }

    private void setUpRecycleView(RecyclerView recyclerMachine, int position)
    {
        recyclerMachine.setLayoutManager(linearLayoutManager);
        salesTransactionAdapter = new SalesTransactionRecyclerAdapter(mContext, machineDatas.get(position).salesMachineResultDatas, machineDatas.get(position).FromDate, machineDatas.get(position).ToDate);
        recyclerMachine.setAdapter(salesTransactionAdapter);
    }


    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((NestedScrollView) object);
    }

    private void setUpViewBackground(CardView view)
    {
        int[] androidColors = mContext.getResources().getIntArray(R.array.random_color);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        view.setCardBackgroundColor(randomAndroidColor);
    }

    public static String formateDateFromstring(String inputDate)
    {

        System.out.println("inputDate------------"+inputDate);
        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            //LOGE(TAG, "ParseException - dateFormat");
        }

        return outputDate;

    }

}