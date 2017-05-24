package com.aapkatrade.buyer.user_dashboard;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.login.LoginActivity;

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
                        Intent i = new Intent(context, LoginActivity.class);
                        context.startActivity(i);


                    } else {
                        Intent my_profile = new Intent(context, MyProfileActivity.class);
                        context.startActivity(my_profile);

                    }

                } else if (itemList.get(position).dashboard_name.equals("Change Password")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin").equals("notlogin")) {
                        Intent i = new Intent(context, LoginActivity.class);
                        context.startActivity(i);


                    } else {

                        Intent change_password = new Intent(context, ChangePassword.class);
                        context.startActivity(change_password);

                    }

                } else if (itemList.get(position).dashboard_name.equals("Order")) {
                    if (appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin").equals("notlogin")) {
                        Intent i = new Intent(context, LoginActivity.class);
                        context.startActivity(i);


                    } else {

                        Intent list_product = new Intent(context, OrderManagementActivity.class);
                        context.startActivity(list_product);

                    }

                } else if (itemList.get(position).dashboard_name.equals("Cancel Order")) {


                    Intent list_product = new Intent(context, OrderActivity.class);
                    context.startActivity(list_product);


                    //    Associate Agreement
                }


            }
        });

    }


    @Override
    public int getItemCount() {
        return itemList.size();

    }


}
