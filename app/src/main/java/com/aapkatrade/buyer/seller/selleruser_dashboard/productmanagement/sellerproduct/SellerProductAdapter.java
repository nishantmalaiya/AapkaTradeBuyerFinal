package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.sellerproduct;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by PPC16 on 7/10/2017.
 */

public class SellerProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private final LayoutInflater inflater;
    private List<SellerProductData> itemList;
    private Context context;
    SellerProductHolder viewHolder;
    AppSharedPreference appSharedPreference;
    String userId;
    ProgressBarHandler progressBarHandler;


    public SellerProductAdapter(Context context, List<SellerProductData> itemList)
    {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appSharedPreference = new AppSharedPreference(context);

        userId = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");

        progressBarHandler = new ProgressBarHandler(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.row_seller_product_list, parent, false);

        viewHolder = new SellerProductHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {

        SellerProductHolder homeHolder = (SellerProductHolder) holder;

    }

    private void showMessage(String s)
    {
        AndroidUtils.showToast(context, s);
    }


    @Override
    public int getItemCount()
    {
        return itemList.size();
    }

    public String getCurrentTimeStamp()
    {
        return new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date());
    }


}
