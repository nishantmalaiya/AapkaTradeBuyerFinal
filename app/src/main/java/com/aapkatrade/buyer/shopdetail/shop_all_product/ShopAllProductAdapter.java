package com.aapkatrade.buyer.shopdetail.shop_all_product;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.shopdetail.productdetail.ProductDetailActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by PPC16 on 4/21/2017.
 */

public class ShopAllProductAdapter extends RecyclerView.Adapter<ShopAllProductHolder>
{

    final LayoutInflater inflater;
    List<ShopAllProductData> itemList;
    Context context;
    ShopAllProductHolder homeHolder;
    TextView textViewQuantity;
    LinearLayout linearLayoutQuantity;
    DroppyMenuPopup droppyMenu;
    private ProgressBarHandler progressBarHandler;
    AppSharedPreference appSharedPreference;


    public ShopAllProductAdapter(Context context, List<ShopAllProductData> itemList)
    {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appSharedPreference = new AppSharedPreference(context);
        progressBarHandler = new ProgressBarHandler(context);
    }

    @Override
    public ShopAllProductHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ShopAllProductHolder(inflater.inflate(R.layout.row_shop_service_list, parent, false));
    }


    @Override
    public void onBindViewHolder(final ShopAllProductHolder holder, final int position)
    {

        homeHolder = holder;
        AndroidUtils.showErrorLog(context, "___________PRODUCT ID---3--------->"+itemList.get(position).productId);

        holder.tvProductName.setText(itemList.get(position).productName);
        holder.tvProductPrice.setText(context.getResources().getText(R.string.rupay_text)+itemList.get(position).productPrice);

        System.out.println("itemlist------------"+itemList.get(position).productImage);

        Picasso.with(context).load(itemList.get(position).productImage)
                .placeholder(R.drawable.default_noimage)
                .error(R.drawable.default_noimage)
                .into(holder.productImage);


        linearLayoutQuantity=homeHolder.dropdown_ll;

        textViewQuantity=homeHolder.tv_qty;

       // textViewQuantity.setText(itemList.get(position).quantity.toString());

        //linearLayoutQuantity.setOnClickListener(this);

        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(context, linearLayoutQuantity);
        droppyBuilder.addMenuItem(new DroppyMenuItem("1"))
                .addMenuItem(new DroppyMenuItem("2"))
                .addMenuItem(new DroppyMenuItem("3"))
                .addMenuItem(new DroppyMenuItem("4"))
                .addMenuItem(new DroppyMenuItem("5"))
                .addSeparator()
                .addMenuItem(new DroppyMenuItem("More"));

        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                switch (id) {
                    case 0:
                       /* itemList.get(position).setQuantity("1");
                        homeHolder.textView64.setText(itemList.get(position).quantity.toString());*/
                        notifyDataSetChanged();
                        break;
                    case 1:
                       /* itemList.get(position).setQuantity("2");
                        homeHolder.textView64.setText(itemList.get(position).quantity.toString());*/
                        notifyDataSetChanged();
                        break;
                    case 2:
                       /* itemList.get(position).setQuantity("3");
                        homeHolder.textView64.setText(itemList.get(position).quantity.toString());*/
                        notifyDataSetChanged();

                        break;
                    case 3:
                        /*itemList.get(position).setQuantity("4");
                        homeHolder.textView64.setText(itemList.get(position).quantity.toString());*/
                        notifyDataSetChanged();
                        break;
                    case 4:
                       /* itemList.get(position).setQuantity("5");
                        homeHolder.textView64.setText(itemList.get(position).quantity.toString());*/
                        notifyDataSetChanged();
                        break;
                    case 5:
                       // showPopup("Quantity",position);
                        break;

                }
            }
        });

        droppyMenu = droppyBuilder.build();


        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                /*holder.addToCartButton.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.addToCartButton.setBackground(ContextCompat.getDrawable(context, R.drawable.add_to_cart_selected));
                */
                String product_id = itemList.get(position).productId;
                String product_name = itemList.get(position).productName;
                String price = itemList.get(position).productPrice;
                String quantity = homeHolder.tv_qty.toString();

                callwebservice__add_tocart(product_id,"",product_name,price,quantity);


            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                AndroidUtils.showErrorLog(context, "___________PRODUCT ID------------>"+itemList.get(position).productId);
                intent.putExtra("productId", itemList.get(position).productId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return itemList.size();
    }


    private void callwebservice__add_tocart(String product_id, String device_id, String product_name,String price, String qty)
    {
        progressBarHandler.show();
        System.out.println("price-----------------------"+price);

        String login_url = context.getResources().getString(R.string.webservice_base_url) + "/add_cart";

        String android_id = AppConfig.getCurrentDeviceId(context);

        System.out.println("devece_id------------"+android_id);

        String user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");
        if (user_id.equals("notlogin"))
        {
            user_id="";
        }

        Ion.with(context)
                .load(login_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id", user_id)
                .setBodyParameter("product_id", product_id)
                .setBodyParameter("device_id", android_id)
                .setBodyParameter("name",product_name)
                .setBodyParameter("price",price)
                .setBodyParameter("quantity","1")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result!=null)
                        {
                            System.out.println("result--------------" + result);
                            String message = result.get("message").getAsString();
                            JsonObject jsonObject = result.getAsJsonObject("result");

                            if (message.equals("This Item Already Exist....."))
                            {
                                progressBarHandler.hide();
                                Toast.makeText(context, "This Item Already Exist in Cart", Toast.LENGTH_SHORT).show();

                            }
                            else if (message.equals("Product Quantity exceeded")){

                                progressBarHandler.hide();
                                Toast.makeText(context, "Product is not Available in Stock", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {

                                Toast.makeText(context, "Product Successfully Added on Cart", Toast.LENGTH_SHORT).show();
                                String cart_count = jsonObject.get("total_qty").getAsString();
                                appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), Integer.valueOf(cart_count));

                                //int j = appSharedPreference.getSharedPrefInt("cart_count",0);
                                ShopAllProductActivity.tvCartCount.setText(String.valueOf(appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));
                                progressBarHandler.hide();



                            }


                        }
                        else
                        {

                            progressBarHandler.hide();
                            Toast.makeText(context,"Server is not responding please try again",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


}