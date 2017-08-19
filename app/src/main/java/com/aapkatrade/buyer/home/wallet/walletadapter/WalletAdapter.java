package com.aapkatrade.buyer.home.wallet.walletadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.home.CommonData;
import com.aapkatrade.buyer.home.CommonHolder;
import com.aapkatrade.buyer.shopdetail.ShopDetailActivity;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by PPC17 on 17-Aug-17.
 */

public class WalletAdapter extends RecyclerView.Adapter<WalletTransactionHolder> {
    private List<WalletTransactionDatas> itemList;
    private Context context;


    public WalletAdapter(Context context, List<WalletTransactionDatas> itemList) {
        this.itemList = itemList;
        this.context = context;

    }

    @Override
    public WalletTransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WalletTransactionHolder(LayoutInflater.from(context).inflate(R.layout.row_wallet_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(final WalletTransactionHolder holder, final int position) {


        if(position%2==0)
        {

            AndroidUtils.setGradientColor(holder.cardview, android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.wallet_card_gradient_bottom), ContextCompat.getColor(context, R.color.wallet_card_gradient_Top), android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, 0);

            AndroidUtils.setBackgroundSolid(holder.imgvew_transaction_status, context, R.color.white, 15, GradientDrawable.OVAL);

        }
        else{


            AndroidUtils.setGradientColor(holder.cardview, android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.wallet_card2_gradient_bottom), ContextCompat.getColor(context, R.color.wallet_card2_gradient_Top), android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, 0);

            AndroidUtils.setBackgroundSolid(holder.imgvew_transaction_status, context, R.color.color_voilet, 15, GradientDrawable.OVAL);


        }


        Ion.with(holder.transaction_type_imgvew)
                .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                .load(itemList.get(position).transaction_image);



        holder.tv_transaction_amount.setText(context.getResources().getString(R.string.rupay_text)+" "+"200");
        holder.tv_transactionDate.setText("16th Aug,2017");



    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
