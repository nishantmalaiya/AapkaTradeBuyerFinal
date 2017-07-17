package com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.entity.ProductListData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.viewholder.ProductListViewHolder;
import com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.editService.EditServiceActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.entity.ServiceListData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.viewholder.ServiceListViewHolder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by PPC16 on 7/10/2017.
 */

public class ServiceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater inflater;
    private List<ServiceListData> itemList;
    private Context context;
    ServiceListViewHolder viewHolder;
    AppSharedPreference appSharedPreference;
    String userId;
    ProgressBarHandler progressBarHandler;


    public ServiceListAdapter(Context context, List<ServiceListData> itemList) {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appSharedPreference = new AppSharedPreference(context);

        userId = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");

        progressBarHandler = new ProgressBarHandler(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_seller_service_list, parent, false);

        viewHolder = new ServiceListViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ServiceListViewHolder homeHolder = (ServiceListViewHolder) holder;

        homeHolder.serviceName.setText(itemList.get(position).service_name);
        homeHolder.tvServiceCategoryName.setText(itemList.get(position).service_category_name);
        homeHolder.tvServiceShopName.setText(itemList.get(position).service_shop_name);


        Ion.with(context)
                .load(itemList.get(position).service_image)
                .withBitmap().asBitmap()
                .setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        if (result != null)
                            homeHolder.imgService.setImageBitmap(result);
                    }
                });


        homeHolder.imgviewServiceEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context wrapper = new ContextThemeWrapper(context, R.style.MyPopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, homeHolder.imgviewServiceEdit);
                //inflating menu from xml resource
                popup.inflate(R.menu.edit_service_list);

                Object menuHelper;
                Class[] argTypes;
                try {
                    Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                    fMenuHelper.setAccessible(true);
                    menuHelper = fMenuHelper.get(popup);
                    argTypes = new Class[]{boolean.class};
                    menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
                } catch (Exception e) {
                    // Possible exceptions are NoSuchMethodError and NoSuchFieldError
                    //
                    // In either case, an exception indicates something is wrong with the reflection code, or the
                    // structure of the PopupMenu class or its dependencies has changed.
                    //
                    // These exceptions should never happen since we're shipping the AppCompat library in our own apk,
                    // but in the case that they do, we simply can't force icons to display, so log the error and
                    // show the menu normally.

                    Log.w("", "error forcing menu icons to show", e);
                    popup.show();
                    return;
                }


                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.service_edit:


                                calleditserviceActivity(position);
                                //handle menu1 click
                                break;
                            case R.id.service_delete:


                                call_delete_service_webservice(itemList.get(position).service_id, position);
                                //handle menu2 click
                                break;

                        }
                        return false;
                    }


                });
                //displaying the popup
                popup.show();
            }
        });

       /* homeHolder.relativeEdit.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }

    private void calleditserviceActivity(int position) {
        Intent editserviceintent = new Intent(context, EditServiceActivity.class);
        editserviceintent.putExtra("serviceid",itemList.get(position).service_id);
        editserviceintent.putExtra("servicename",itemList.get(position).service_name);
        editserviceintent.putExtra("service_category_name",itemList.get(position).service_category_name);
        editserviceintent.putExtra("service_image",itemList.get(position).service_image);
        editserviceintent.putExtra("service_shop_name",itemList.get(position).service_shop_name);
        context.startActivity(editserviceintent);


    }

    private void call_delete_service_webservice(String service_id, final int position) {


        String delete_service_webservice_url = context.getString(R.string.webservice_base_url) + "/delete_service";


        progressBarHandler.show();

        Ion.with(context)
                .load(delete_service_webservice_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("service_id", service_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        AndroidUtils.showErrorLog(context, "order_list_response", result.toString());

                        if (result == null) {
                            progressBarHandler.hide();
                        } else {
                            String error = result.get("error").getAsString();
                            if (error.contains("false")) {

                                AndroidUtils.showToast(context, "Service Deleted Successfully");
                                itemList.remove(position);
                                progressBarHandler.hide();
                                notifyDataSetChanged();


                            }
                        }


                    }
                });


    }


    private void showMessage(String s) {
        AndroidUtils.showToast(context, s);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public String getCurrentTimeStamp() {
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
