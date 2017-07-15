package com.aapkatrade.buyer.user_dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.dialogs.ServiceEnquiry;
import com.aapkatrade.buyer.dialogs.comingsoon.ComingSoonFragmentDialog;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;

import com.aapkatrade.buyer.login.LoginDashboard;


import com.aapkatrade.buyer.seller.selleruser_dashboard.billhistory.BillHistory;

import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.CompanyShopManagementActivity;

import com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.BillPaymentActivity;

import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.ProductManagementActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.SalesTransactionActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.serviceenquiryList.ServiceEnquiryActivity;

import com.aapkatrade.buyer.seller.selleruser_dashboard.bankdetails.BankDetailsActivity;

import com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.ServiceManagementActivity;
import com.aapkatrade.buyer.user_dashboard.changepassword.ChangePassword;

import com.aapkatrade.buyer.user_dashboard.my_profile.MyProfileActivity;
import com.aapkatrade.buyer.user_dashboard.order_list.OrderActivity;
import com.aapkatrade.buyer.user_dashboard.order_list.OrderManagementActivity;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by PPC16 on 10-Jan-17.
 */

public class DashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater inflater;
    private List<DashboardData> itemList;
    private Context context;
    private DashboardHolder viewHolder;
    private AppSharedPreference appSharedPreference;


    public DashboardAdapter(Context context, List<DashboardData> itemList) {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appSharedPreference = new AppSharedPreference(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_dashboard2, parent, false);
        viewHolder = new DashboardHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DashboardHolder homeHolder = (DashboardHolder) holder;

        homeHolder.tvDashboard.setText(itemList.get(position).dashboard_name.toString());

        if (itemList.get(position).isList) {
            homeHolder.tvquantity.setText(itemList.get(position).quantities.toString());
        } else {
            homeHolder.tvquantity.setVisibility(View.INVISIBLE);
        }

        Picasso.with(context).load(itemList.get(position).image).into(homeHolder.imageView);

        ((DashboardHolder) holder).imageView.setBackground(context.getResources().getDrawable(itemList.get(position).color));


        homeHolder.relativeDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemList.get(position).dashboard_name.equals("My Profile")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin").equals("notlogin")) {
                        Intent i = new Intent(context, LoginDashboard.class);
                        context.startActivity(i);


                    } else {
                        Intent my_profile = new Intent(context, MyProfileActivity.class);
                        context.startActivity(my_profile);

                    }

                } else if (itemList.get(position).dashboard_name.equals("Change Password")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin").equals("notlogin")) {
                        Intent i = new Intent(context, LoginDashboard.class);
                        context.startActivity(i);


                    } else {

                        Intent change_password = new Intent(context, ChangePassword.class);
                        context.startActivity(change_password);

                    }

                } else if (itemList.get(position).dashboard_name.equals("Bill Payment")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin").equals("notlogin")) {
                        Intent i = new Intent(context, LoginDashboard.class);
                        context.startActivity(i);


                    } else {

                        Intent change_password = new Intent(context, BillPaymentActivity.class);
                        context.startActivity(change_password);

                    }

                } else if (itemList.get(position).dashboard_name.equals("Enquiry Management")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin").equals("notlogin")) {
                        Intent i = new Intent(context, LoginDashboard.class);
                        context.startActivity(i);

                    } else {
                        Intent service_enquiry_list = new Intent(context, ServiceEnquiryActivity.class);
                        context.startActivity(service_enquiry_list);
                    }

                } else if (itemList.get(position).dashboard_name.equals("Order")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin").equals("notlogin")) {
                        Intent i = new Intent(context, LoginDashboard.class);
                        context.startActivity(i);


                    } else {

                        Intent list_product = new Intent(context, OrderManagementActivity.class);
                        context.startActivity(list_product);

                    }

                } else if (itemList.get(position).dashboard_name.equals("Cancel Order")) {


                    Intent list_product = new Intent(context, OrderActivity.class);
                    context.startActivity(list_product);


                    //    Associate Agreement
                } else if (itemList.get(position).dashboard_name.equals("Bank Details")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin").equals("notlogin")) {
                        Intent i = new Intent(context, LoginDashboard.class);
                        context.startActivity(i);


                    } else {
                        Intent bankDetails = new Intent(context, BankDetailsActivity.class);
                        context.startActivity(bankDetails);

                    }

                } else if (itemList.get(position).dashboard_name.equals("Company/Shop List")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin").equals("notlogin")) {
                        Intent i = new Intent(context, LoginDashboard.class);
                        context.startActivity(i);


                    } else {
//                        Intent bankDetails = new Intent(context, CompanyShopManagementActivity.class);
//                        context.startActivity(bankDetails);

                        ComingSoonFragmentDialog comingSoonFragmentDialog = new ComingSoonFragmentDialog(context);
                        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                        comingSoonFragmentDialog.show(fm, "enquiry");
                    }

                } else if (itemList.get(position).dashboard_name.equals("Bill History")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin").equals("notlogin")) {
                        Intent i = new Intent(context, LoginDashboard.class);
                        context.startActivity(i);


                    } else {
                        Intent billhistory = new Intent(context, BillHistory.class);
                        context.startActivity(billhistory);

                    }

                } else if (itemList.get(position).dashboard_name.equals("Sales Transaction")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin").equals("notlogin")) {
                        Intent i = new Intent(context, LoginDashboard.class);
                        context.startActivity(i);


                    } else {
                        Intent billhistory = new Intent(context, SalesTransactionActivity.class);
                        context.startActivity(billhistory);

                    }

                } else if (itemList.get(position).dashboard_name.equals("Product Management")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin").equals("notlogin")) {
                        Intent i = new Intent(context, LoginDashboard.class);
                        context.startActivity(i);


                    } else {
//                        Intent productManagementIntent = new Intent(context, ProductManagementActivity.class);
//                        context.startActivity(productManagementIntent);
                        ComingSoonFragmentDialog comingSoonFragmentDialog = new ComingSoonFragmentDialog(context);
                        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                        comingSoonFragmentDialog.show(fm, "enquiry");

                    }

                }

                else if (itemList.get(position).dashboard_name.equals("Service Management")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "notlogin").equals("notlogin")) {
                        Intent i = new Intent(context, LoginDashboard.class);
                        context.startActivity(i);


                    } else {
//                        Intent billhistory = new Intent(context, ServiceManagementActivity.class);
//                        context.startActivity(billhistory);
                        ComingSoonFragmentDialog comingSoonFragmentDialog = new ComingSoonFragmentDialog(context);
                        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                        comingSoonFragmentDialog.show(fm, "enquiry");
                    }

                }


            }
        });

    }


    @Override
    public int getItemCount() {
        return itemList.size();

    }


}
