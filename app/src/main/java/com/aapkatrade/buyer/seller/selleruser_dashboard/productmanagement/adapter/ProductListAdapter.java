package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.dialogs.Seller_Update_Product_Policy;
import com.aapkatrade.buyer.dialogs.ServiceEnquiry;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.ProductManagementActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.editproduct.EditProductActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.entity.ProductListData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.viewholder.ProductListViewHolder;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by PPC16 on 7/10/2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private final LayoutInflater inflater;
    private List<ProductListData> itemList;
    private Context context;
    ProductListViewHolder viewHolder;
    AppSharedPreference appSharedPreference;
    String userId;
    ProgressBarHandler progressBarHandler;

    public ProductListAdapter(Context context, List<ProductListData> itemList)
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
        View view = inflater.inflate(R.layout.row_seller_product_list2, parent, false);

        viewHolder = new ProductListViewHolder(view);

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

        homeHolder.btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent edit_product = new Intent(context, EditProductActivity.class);
                context.startActivity(edit_product);
            }
        });

        homeHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                callwebserviceDeleteCart(itemList.get(position).product_id,position);
            }
        });


        homeHolder.btnPolicyUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Seller_Update_Product_Policy seller_update_product_policy = new Seller_Update_Product_Policy(itemList.get(position).product_id, context);
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                seller_update_product_policy.show(manager, "enquiry");

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


    private void callwebserviceDeleteCart(String product_id, final int position)
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

                            if (error_message.equals("false"))
                            {
                                System.out.println("result--------------" + result);
                                JsonObject jsonObject = result.getAsJsonObject("result");

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

    }

}
