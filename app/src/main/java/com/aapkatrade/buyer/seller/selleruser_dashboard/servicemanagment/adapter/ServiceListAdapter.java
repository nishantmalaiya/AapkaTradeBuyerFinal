package com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.entity.ProductListData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.viewholder.ProductListViewHolder;
import com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.viewholder.ServiceListViewHolder;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by PPC16 on 7/10/2017.
 */

public class ServiceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private final LayoutInflater inflater;
    private List<ProductListData> itemList;
    private Context context;
    ServiceListViewHolder viewHolder;
    AppSharedPreference appSharedPreference;
    String userId;
    ProgressBarHandler progressBarHandler;


    public ServiceListAdapter(Context context, List<ProductListData> itemList)
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

        viewHolder = new ServiceListViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {

        final ProductListViewHolder homeHolder = (ProductListViewHolder) holder;

        homeHolder.productName.setText(itemList.get(position).product_name);
        homeHolder.tvProductCategoryName.setText(itemList.get(position).category_name);
        homeHolder.tvProductShopName.setText(itemList.get(position).shop_name);
        homeHolder.tvProductStateName.setText(itemList.get(position).State_name);

        Ion.with(context)
                .load(itemList.get(position).product_image)
                .withBitmap().asBitmap()
                .setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        if (result != null)
                            homeHolder.imgProduct.setImageBitmap(result);
                    }
                });


        homeHolder.relativeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });

        homeHolder.relativeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        homeHolder.relativeUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

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


   /* private void callwebserviceDeleteCart(String product_id, final int position)
    {
        progressBarHandler.show();

        String login_url = context.getResources().getString(R.string.webservice_base_url) + "/delete_product";

        Ion.with(context)
                .load(login_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("id", product_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null)
                        {

                            String error_message = result.get("error").getAsString();

                            if (error_message.equals("false")) {
                                System.out.println("result--------------" + result);
                                JsonObject jsonObject = result.getAsJsonObject("result");
                                String total_amount = jsonObject.get("total_amount").getAsString();
                                String cart_count = jsonObject.get("total_qty").getAsString();

                                if (cart_count.equals("0")) {
                                    MyCartActivity.cardviewProductDeatails.setVisibility(View.INVISIBLE);
                                    MyCartActivity.cardBottom.setVisibility(View.INVISIBLE);
                                } else {
                                    MyCartActivity.cardviewProductDeatails.setVisibility(View.VISIBLE);
                                    MyCartActivity.cardBottom.setVisibility(View.VISIBLE);

                                }
                                appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), Integer.valueOf(cart_count));

                                HomeActivity.tvCartCount.setText(String.valueOf(appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));

                                MyCartActivity.tvPriceItemsHeading.setText(new StringBuilder("Price ( ").append(cart_count).append(" Items )"));
                                MyCartActivity.tvPriceItems.setText(new StringBuilder(context.getResources().getText(R.string.rupay_text)).append(total_amount));
                                MyCartActivity.tvAmountPayable.setText(new StringBuilder(context.getResources().getText(R.string.rupay_text)).append(total_amount));
                                MyCartActivity.tvLastPayableAmount.setText(new StringBuilder(context.getResources().getText(R.string.rupay_text)).append(total_amount));

                                place_order.remove(position);
                                itemList.remove(position);
                                notifyDataSetChanged();
                                progressBarHandler.hide();

                            } else {
                                progressBarHandler.hide();
                                AndroidUtils.showToast(context, "Server is not responding. Please try again.");
                            }
                        } else {
                            AndroidUtils.showToast(context, "Server is not responding. Please try again.");
                            progressBarHandler.hide();
                        }

                    }
                });

    }*/


}
