package com.aapkatrade.buyer.home.wallet.walletadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.ActivityCompat;
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
import com.aapkatrade.buyer.wallet.AddMoneyWalletSuccessActivity;
import com.koushikdutta.ion.Ion;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by PPC17 on 17-Aug-17.
 */

public class WalletAdapter extends RecyclerView.Adapter<WalletTransactionHolder>
{
    private List<WalletTransactionDatas> itemList;
    private Context context;

    public WalletAdapter(Context context, List<WalletTransactionDatas> itemList)
    {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public WalletTransactionHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new WalletTransactionHolder(LayoutInflater.from(context).inflate(R.layout.row_wallet_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(final WalletTransactionHolder holder, final int position)
    {

        if(itemList.get(position).transaction_status.equals("1"))
        {
          //  AndroidUtils.setGradientColor(holder.cardview, android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.wallet_card_gradient_bottom), ContextCompat.getColor(context, R.color.wallet_card_gradient_Top), android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, 0);
            AndroidUtils.setBackgroundSolid(holder.imgvew_transaction_status, context, R.color.green, 15, GradientDrawable.OVAL);
        }
        else
        {
         //   AndroidUtils.setGradientColor(holder.cardview, android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.wallet_card2_gradient_bottom), ContextCompat.getColor(context, R.color.wallet_card2_gradient_Top), android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, 0);
            AndroidUtils.setBackgroundSolid(holder.imgvew_transaction_status, context, R.color.color_voilet, 15, GradientDrawable.OVAL);
        }


       /*  Ion.with(holder.transaction_type_imgvew)
                .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                .load(ContextCompat.getDrawable(context, R.drawable.ic_applogo1));
       */

        holder.tvTransactionid.setText(itemList.get(position).transaction_id);
        holder.tv_transaction_amount.setText(context.getResources().getString(R.string.rupay_text)+" "+itemList.get(position).transaction_amount);
        holder.tv_transactionDate.setText(itemList.get(position).transaction_date);

        if (itemList.get(position).transaction_type.equals("2"))
        {
            holder.tv_transactiontypeName.setText("Added to Account");
            holder.transaction_type_imgvew.setImageDrawable(ActivityCompat.getDrawable(context, R.drawable.ic_add_money));

        }
        else
        {
            holder.tv_transactiontypeName.setText("Paid for Order");
            holder.transaction_type_imgvew.setImageDrawable(ActivityCompat.getDrawable(context, R.drawable.ic_paid_order));

        }


        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddMoneyWalletSuccessActivity.class);
                i.putExtra("transaction_amount",itemList.get(position).transaction_amount);
                i.putExtra("transaction_id",itemList.get(position).transaction_id);
                i.putExtra("transaction_date",itemList.get(position).transaction_date);
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
