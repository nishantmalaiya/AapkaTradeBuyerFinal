package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.viewpageradapter;

import android.content.Context;
import android.content.res.TypedArray;
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
import com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.SalesTransactionMachine;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by PPC21 on 13-Jan-17.
 */

public class ViewpagerAdapterSalesTransaction extends PagerAdapter {

    private Context mContext;
    ArrayList<SalesMachineResultData> SalesReportsList;
    CardView cardview_salesTransaction;
    ArrayList<ArrayList<SalesTransactionMachine>> machineDatas;
    RecyclerView RecyclerMachine;
    LinearLayoutManager linearLayoutManager;
    private SalesTransactionRecyclerAdapter salesTransactionAdapter;
    ViewPager viewpagerSalesTransaction;

    public static CommonInterface commonInterface;

    public ViewpagerAdapterSalesTransaction(Context mContext, ArrayList<SalesMachineResultData> SalesReportsList, ArrayList<ArrayList<SalesTransactionMachine>> machineDatas, ViewPager viewpagerSalesTransaction) {
        this.SalesReportsList = SalesReportsList;
        this.mContext = mContext;
        this.machineDatas = machineDatas;
        this.viewpagerSalesTransaction = viewpagerSalesTransaction;
        AndroidUtils.showErrorLog(mContext, "SalesReportList", SalesReportsList.size());

    }


    public int getCount() {
        return SalesReportsList != null ? SalesReportsList.size() : -1;

    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.viewpager_sales_transaction, container, false);
        TextView TvSalesResultData = (TextView) itemView.findViewById(R.id.tv_salesTransactionResultData);
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        StringBuilder stringBuilder = new StringBuilder("<font size=\"20\">Machine No. 15456234</font>");
        stringBuilder.append("<br>").append("<br>").append("Txn Amount: " + mContext.getString(R.string.rupay_text) + "6182.76").append("<br>").append("Sales Amount : ").append(" " + mContext.getString(R.string.rupay_text)).append("5216.53").append("<br>").append("<br>").append("07-june-2017 to 16-june-2017");
        String tvData = stringBuilder.toString();
        CircleIndicator circleIndicator=(CircleIndicator)itemView.findViewById(R.id.indicator_custom_sales_transaction);
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


        salesTransactionAdapter = new SalesTransactionRecyclerAdapter(mContext, machineDatas.get(0));
        recyclerMachine.setAdapter(salesTransactionAdapter);
        recyclerMachine.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int FirstVisibleItemCount = linearLayoutManager.findFirstVisibleItemPosition();
                AndroidUtils.showErrorLog(mContext,"FirstVisibleItemCount",FirstVisibleItemCount);
                if (totalItemCount > 0) {
                    if (FirstVisibleItemCount == 0) {

                        commonInterface.getData("Show");
                    }
                    else{

                        commonInterface.getData("Hide");
                    }
                }
            }

        });


    }


    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((NestedScrollView) object);
    }

    private void setUpViewBackground(CardView view) {
        TypedArray colors = mContext.getResources().obtainTypedArray(R.array.random_color);
        int choice = (int) (Math.random() * colors.length());
        view.setCardBackgroundColor(colors.getResourceId(choice, R.drawable.circle_sienna));
        colors.recycle();
    }


}