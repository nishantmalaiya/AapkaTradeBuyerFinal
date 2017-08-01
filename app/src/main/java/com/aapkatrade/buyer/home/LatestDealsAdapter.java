package com.aapkatrade.buyer.home;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.BillPaymentActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.entity.BillPaymentList;
import com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.viewholder.BillPaymentViewHolder;
import com.aapkatrade.buyer.shopdetail.ShopDetailActivity;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PPC15 on 8/1/2017.
 */

public class LatestDealsAdapter extends RecyclerView.Adapter<CommonHolder> {
    private List<CommonData> itemList;
    private Context context;


    public LatestDealsAdapter(Context context, List<CommonData> itemList) {
        this.itemList = itemList;
        this.context = context;

    }

    @Override
    public CommonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommonHolder(LayoutInflater.from(context).inflate(R.layout.row_dashboard, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommonHolder holder, final int position) {


        Ion.with(holder.cimageview)
                .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                .load(itemList.get(position).imageurl);


        Log.e("imageurl", itemList.get(0).imageurl);

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("time Adapter", String.valueOf(System.currentTimeMillis()));

                Intent intent = new Intent(context, ShopDetailActivity.class);
                intent.putExtra("product_id", itemList.get(position).id);
                context.startActivity(intent);
                ((AppCompatActivity) context).overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });
        holder.tvProductName.setText(itemList.get(position).name);


    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


}