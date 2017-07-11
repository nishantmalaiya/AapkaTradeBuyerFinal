package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.editcompanyshop.EditCompanyShopActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.ProductManagementActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.AddProductActivity;
import com.aapkatrade.buyer.shopdetail.ShopDetailActivity;
import com.aapkatrade.buyer.shopdetail.shop_all_product.ShopAllProductActivity;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by PPC16 on 10-Mar-17.
 */


public class CompanyShopListAdapter extends RecyclerView.Adapter<CompanyShopDataHolder> {

    private Context context;
    private LinkedList<CompanyShopData> companyShopLinkedList;
    private AppSharedPreference appSharedpreference;
    private ProgressBarHandler progressHandler;
    public CompanyShopListAdapter(Context context, LinkedList<CompanyShopData> companyShopLinkedList)
    {
        this.context = context;
        this.companyShopLinkedList = companyShopLinkedList;
        appSharedpreference = new AppSharedPreference(context);
        progressHandler = new ProgressBarHandler(context);
    }


    @Override
    public CompanyShopDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CompanyShopDataHolder(LayoutInflater.from(context).inflate(R.layout.row_company_shop_mgt, parent, false));
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final CompanyShopDataHolder holder, final int position)
    {

        if(context instanceof ProductManagementActivity)
        {
            holder.rowRightIconDescription.setVisibility(View.VISIBLE);
            holder.rowRightIconDescription.setText("Add Product");
            holder.rowRightIcon.setImageDrawable(AndroidUtils.setImageColor(context, R.drawable.ic_add_product, R.color.orange));
        }

        String strCurrentDate = companyShopLinkedList.get(position).getCreated();


        String date =   AndroidUtils.formateDateFromstring(strCurrentDate);
        StringBuilder stringBuilder = new StringBuilder(companyShopLinkedList.get(position).getName());
        stringBuilder.append("<br>").append("<font color=\"#7dbd00\"><i>").append(date).append("</i></font>").append("<br>").append("Products : ").append(companyShopLinkedList.get(position).getProductCount());
        String tvData = stringBuilder.toString();
        AndroidUtils.showErrorLog(context,position+"---->  ", tvData);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvCompanyShop.setText(Html.fromHtml(tvData, Html.FROM_HTML_MODE_LEGACY));
        } else {
            //noinspection deprecation
            holder.tvCompanyShop.setText(Html.fromHtml(tvData));
        }
        Ion.with(context)
                .load(companyShopLinkedList.get(position).getCompany_image())
                .withBitmap().asBitmap()
                .setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        if (result != null)
                            holder.company_image.setImageBitmap(result);
                    }
                });

        holder.rowRightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof ProductManagementActivity){
                    Intent intentAddProduct = new Intent(context, AddProductActivity.class);
                    intentAddProduct.putExtra("shopId", companyShopLinkedList.get(position).getCompanyId());
                    context.startActivity(intentAddProduct);
                }
                else if(context instanceof CompanyShopManagementActivity){
                    Intent intentAddProduct = new Intent(context, EditCompanyShopActivity.class);
                    intentAddProduct.putExtra("shopId", companyShopLinkedList.get(position).getCompanyId());
                    context.startActivity(intentAddProduct);
                }
            }
        });

        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(context instanceof CompanyShopManagementActivity) {
                    Intent intentShopDetail = new Intent(context, ShopDetailActivity.class);
                    intentShopDetail.putExtra("product_id", companyShopLinkedList.get(position).getCompanyId());
                    context.startActivity(intentShopDetail);
                } else if(context instanceof ProductManagementActivity && Integer.parseInt(companyShopLinkedList.get(position).getProductCount())>0){
                    Intent intentShopAllProduct = new Intent(context, ShopAllProductActivity.class);
                    intentShopAllProduct.putExtra("shopId", companyShopLinkedList.get(position).getCompanyId());
                    context.startActivity(intentShopAllProduct);
                }
            }
        });
    }




    @Override
    public int getItemCount() {
        return companyShopLinkedList.size();
    }



}