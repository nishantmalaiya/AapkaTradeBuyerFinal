package  com.aapkatrade.buyer.user_dashboard.address.viewpager;

import android.content.Context;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aapkatrade.buyer.Home.cart.CartData;
import com.aapkatrade.buyer.Home.cart.CartHolder;
import com.aapkatrade.buyer.R;

import com.aapkatrade.buyer.dialogs.CustomQuantityCheckout;

import com.aapkatrade.buyer.general.AppConfig;

import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;

import com.aapkatrade.buyer.general.interfaces.CommonInterface;

import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;

import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by PPC16 on 4/25/2017.
 */


public class CartCheckOutAdapter extends RecyclerView.Adapter<CartHolder> implements View.OnClickListener
{

    public final LayoutInflater inflater;
    public static List<CartData> itemList;
    public Context context;
    public TextView textViewQuantity;
    public DroppyMenuPopup droppyMenu;
    public LinearLayout linearLayoutQuantity;
    public EditText editText;
    public AppSharedPreference appSharedPreference;
    public ProgressBarHandler progressBarHandler;
    public static ArrayList<CartData>  place_order = new ArrayList<>();
    public  static int cart_popup_position =0;



    public CartCheckOutAdapter(Context context, List<CartData> itemList)
    {

        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appSharedPreference = new AppSharedPreference(context);
        progressBarHandler = new ProgressBarHandler(context);
        System.out.println("itemlist_cartdata-----------------"+itemList.size());

    }

    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CartHolder(inflater.inflate(R.layout.row_my_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(final CartHolder holder, final int position)
    {

        linearLayoutQuantity = holder.dropdown_ll;

        textViewQuantity = holder.textView64;

        Ion.with(holder.productimage)
                .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                .load(itemList.get(position).product_image);

        //linearLayoutQuantity.setOnClickListener(this);
        place_order.add(new CartData(itemList.get(position).id,itemList.get(position).productName,itemList.get(position).quantity,itemList.get(position).price,itemList.get(position).product_image,itemList.get(position).product_id,itemList.get(position).subtotal_price));

        textViewQuantity.setText(itemList.get(position).quantity);

        // holder.tvProductShopName.setText(itemList.get(position).);

        holder.tvProductName.setText(itemList.get(position).productName);

        holder.tvProductPrice.setText(context.getResources().getText(R.string.rupay_text)+itemList.get(position).price);

        holder.tvProductSubtotalPrice.setText(context.getResources().getText(R.string.rupay_text)+itemList.get(position).subtotal_price);

        System.out.println("itemlist-------------"+itemList.get(position).product_image);

        final DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(context, linearLayoutQuantity);
        droppyBuilder.addMenuItem(new DroppyMenuItem("1"))
                .addMenuItem(new DroppyMenuItem("2"))
                .addMenuItem(new DroppyMenuItem("3"))
                .addMenuItem(new DroppyMenuItem("4"))
                .addMenuItem(new DroppyMenuItem("5"))
                .addSeparator()
                .addMenuItem(new DroppyMenuItem("More"));


        CustomQuantityCheckout.commonInterface = new CommonInterface()
        {
            @Override
            public Object getData(Object object)
            {


                return null;
            }


        };


        droppyBuilder.setOnClick(new DroppyClickCallbackInterface()
        {

            @Override
            public void call(View v, int id)
            {
                double cart_price= 0;

                switch (id)
                {
                    case 0:
                        itemList.get(position).setQuantity("1");
                        //holder.textView64.setText(itemList.get(position).quantity);
                        cart_price = Double.valueOf(itemList.get(position).price) *1;
                        /// itemList.set(position, new CartData(itemList.get(position).id,itemList.get(position).productName,"1",cart_price,itemList.get(position).product_image,itemList.get(position).product_id));
                        //holder.tvProductSubtotalPrice.setText(context.getResources().getText(R.string.rupay_text)+String.valueOf(cart_price));
                        place_order.add(position,new CartData(itemList.get(position).id,itemList.get(position).productName,"1",String.valueOf(cart_price),itemList.get(position).product_image,itemList.get(position).product_id,itemList.get(position).subtotal_price));
                        //callWebServiceUpdateCart(itemList.get(position).id,position,"1");

                        callwebservice__update_cart(itemList.get(position).id,position,"1",itemList.get(position).product_id,holder,cart_price);

                      /*  if (callWebServiceUpdateCart(itemList.get(position).id,position,"1",itemList.get(position).product_id,holder,cart_price))
                        {
                            holder.tvProductSubtotalPrice.setText(context.getResources().getText(R.string.rupay_text)+String.valueOf(cart_price));
                            holder.textView64.setText(itemList.get(position).quantity);
                        }*/
                        break;
                    case 1:
                        itemList.get(position).setQuantity("2");
                        //holder.textView64.setText(itemList.get(position).quantity);
                        cart_price = Double.valueOf(itemList.get(position).price) *2;
                        System.out.println("cart_price----------"+cart_price);
                        //holder.tvProductSubtotalPrice.setText(context.getResources().getText(R.string.rupay_text)+String.valueOf(cart_price));
                        place_order.add(position,new CartData(itemList.get(position).id,itemList.get(position).productName,"2",String.valueOf(cart_price),itemList.get(position).product_image,itemList.get(position).product_id,itemList.get(position).subtotal_price));
                        //callWebServiceUpdateCart(itemList.get(position).id,position,"2");

                        callwebservice__update_cart(itemList.get(position).id,position,"2",itemList.get(position).product_id,holder,cart_price);


                        /* if (callWebServiceUpdateCart(itemList.get(position).id,position,"2",itemList.get(position).product_id))
                        {
                            holder.tvProductSubtotalPrice.setText(context.getResources().getText(R.string.rupay_text)+String.valueOf(cart_price));
                            holder.textView64.setText(itemList.get(position).quantity);
                        }*/

                        break;
                    case 2:
                        itemList.get(position).setQuantity("3");
                        //holder.textView64.setText(itemList.get(position).quantity);
                        cart_price = Double.valueOf(itemList.get(position).price) *3;
                        //holder.tvProductSubtotalPrice.setText(context.getResources().getText(R.string.rupay_text)+String.valueOf(cart_price));
                        place_order.add(position,new CartData(itemList.get(position).id,itemList.get(position).productName,"3",String.valueOf(cart_price),itemList.get(position).product_image,itemList.get(position).product_id,itemList.get(position).subtotal_price));
                        //callWebServiceUpdateCart(itemList.get(position).id,position,"3");

                        callwebservice__update_cart(itemList.get(position).id,position,"3",itemList.get(position).product_id,holder,cart_price);

                       /*  if (callWebServiceUpdateCart(itemList.get(position).id,position,"3",itemList.get(position).product_id))
                        {
                            holder.tvProductSubtotalPrice.setText(context.getResources().getText(R.string.rupay_text)+String.valueOf(cart_price));
                            holder.textView64.setText(itemList.get(position).quantity);
                        }*/


                        break;

                    case 3:
                        itemList.get(position).setQuantity("4");
                        //holder.textView64.setText(itemList.get(position).quantity);
                        cart_price = Double.valueOf(itemList.get(position).price) *4;
                        //holder.tvProductSubtotalPrice.setText(context.getResources().getText(R.string.rupay_text)+String.valueOf(cart_price));
                        place_order.add(position,new CartData(itemList.get(position).id,itemList.get(position).productName,"4",String.valueOf(cart_price),itemList.get(position).product_image,itemList.get(position).product_id,itemList.get(position).subtotal_price));
                        //callWebServiceUpdateCart(itemList.get(position).id,position,"4");

                        callwebservice__update_cart(itemList.get(position).id,position,"4",itemList.get(position).product_id,holder,cart_price);

                       /* if (callWebServiceUpdateCart(itemList.get(position).id,position,"4",itemList.get(position).product_id))
                        {
                            holder.tvProductSubtotalPrice.setText(context.getResources().getText(R.string.rupay_text)+String.valueOf(cart_price));
                            holder.textView64.setText(itemList.get(position).quantity);
                        }*/
                        break;
                    case 4:
                        itemList.get(position).setQuantity("5");
                        //holder.textView64.setText(itemList.get(position).quantity);
                        cart_price = Double.valueOf(itemList.get(position).price) *5;
                        //holder.tvProductSubtotalPrice.setText(context.getResources().getText(R.string.rupay_text)+String.valueOf(cart_price));
                        place_order.add(position,new CartData(itemList.get(position).id,itemList.get(position).productName,"5",String.valueOf(cart_price),itemList.get(position).product_image,itemList.get(position).product_id,itemList.get(position).subtotal_price));
                        //callWebServiceUpdateCart(itemList.get(position).id,position,"5");

                        callwebservice__update_cart(itemList.get(position).id,position,"5",itemList.get(position).product_id,holder,cart_price);

                       /* if (callWebServiceUpdateCart(itemList.get(position).id,position,"5",itemList.get(position).product_id))
                        {
                            holder.tvProductSubtotalPrice.setText(context.getResources().getText(R.string.rupay_text)+String.valueOf(cart_price));
                            holder.textView64.setText(itemList.get(position).quantity);
                        }*/


                        break;
                    case 5:
                        showPopup("Quantity",holder.tvProductSubtotalPrice,position,itemList.get(position).price, holder.textView64);
                        cart_popup_position = position;
                        break;

                }
            }
        });

        droppyMenu = droppyBuilder.build();

        holder.buttonAddtoCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String id = itemList.get(position).id;
                callwebservice__delete_cart(id,position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onClick(View v)
    {

    }

    public void showPopup(String description,TextView textView_subtotal, final int pos,String price,TextView textView_qty)
    {

        CustomQuantityCheckout customQuantityDialog = new CustomQuantityCheckout(context,textView_subtotal,pos,price,textView_qty);
        FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        customQuantityDialog.show(fm, "Quantity");

    }

    private void showMessage(String s)
    {
        AndroidUtils.showErrorLog(context, s, Toast.LENGTH_SHORT);
    }


    private void callwebservice__delete_cart(String product_id, final int position)
    {
        progressBarHandler.show();

        String login_url = context.getResources().getString(R.string.webservice_base_url) + "/cart_remove";

        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        System.out.println("device_id------------"+android_id);

        String user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");

        if (user_id.equals("notlogin"))
        {
            user_id="";
        }

        Ion.with(context)
                .load(login_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("id", product_id)
                .setBodyParameter("device_id", android_id)
                .setBodyParameter("user_id",user_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result!=null)
                        {
                            String error_message = result.get("error").getAsString();

                            if (error_message.equals("false"))
                            {

                                System.out.println("result--------------" + result);
                                JsonObject jsonObject = result.getAsJsonObject("result");
                                String total_amount = jsonObject.get("total_amount").getAsString();
                                String cart_count = jsonObject.get("total_qty").getAsString();

                                if (cart_count.equals("0"))
                                {
                                    CartCheckoutActivity.cardviewProductDeatails.setVisibility(View.INVISIBLE);
                                    CartCheckoutActivity.linearAddressLayout.setVisibility(View.INVISIBLE);
                                    // CartCheckoutActivity.cardBottom.setVisibility(View.INVISIBLE);

                                }
                                else
                                {
                                    CartCheckoutActivity.cardviewProductDeatails.setVisibility(View.VISIBLE);
                                    CartCheckoutActivity.linearAddressLayout.setVisibility(View.VISIBLE);
                                    //  CartCheckoutActivity.cardBottom.setVisibility(View.VISIBLE);

                                }

                                appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), Integer.valueOf(cart_count));

                                CartCheckoutActivity.tvPriceItemsHeading.setText("Price (" + cart_count + " items)");
                                CartCheckoutActivity.tvPriceItems.setText(context.getResources().getText(R.string.rupay_text) + total_amount);
                                CartCheckoutActivity.tvAmountPayable.setText(context.getResources().getText(R.string.rupay_text) + total_amount);
                                // CartCheckoutActivity.tvLastPayableAmount.setText(context.getResources().getText(R.string.rupay_text)+total_amount);

                                place_order.remove(position);
                                itemList.remove(position);
                                notifyDataSetChanged();

                                progressBarHandler.hide();

                            }
                            else
                             {
                                 progressBarHandler.hide();
                                Toast.makeText(context,"Server is not responding please try ",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            progressBarHandler.hide();
                            Toast.makeText(context,"Server is not responding please try ",Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    private void callwebservice__update_cart(String id, final int position, String quantity, String product_id, final CartHolder cartHolder, final double cart_price)
    {


        progressBarHandler.show();

        String login_url = context.getResources().getString(R.string.webservice_base_url) + "/cart_update";

        String user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");

        if (user_id.equals("notlogin"))
        {
            user_id="";
        }

        Ion.with(context)
                .load(login_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("id",id)
                .setBodyParameter("product_id",product_id)
                .setBodyParameter("quantity", quantity)
                .setBodyParameter("user_id",user_id)
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result!=null)
                        {
                            String error_message = result.get("error").getAsString();

                            if (error_message.equals("false"))
                            {
                                String message = result.get("message").getAsString();

                                if (message.equals("Product quantity exceeded"))
                                {
                                    progressBarHandler.hide();
                                    Toast.makeText(context, " Product quantity exceeded", Toast.LENGTH_SHORT).show();

                                }
                                else if (message.equals("Failed to update cart"))
                                {

                                    progressBarHandler.hide();
                                    Toast.makeText(context, "Failed to update cart", Toast.LENGTH_SHORT).show();


                                }
                                else if (message.equals("Invalid Device ID!"))
                                {

                                    progressBarHandler.hide();
                                    Toast.makeText(context, "Invalid Device ID!", Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    JsonObject jsonresult = result.getAsJsonObject("result");

                                    String total_amount = jsonresult.get("total_amount").getAsString();
                                    String cart_count = jsonresult.get("total_qty").getAsString();
                                    appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), Integer.valueOf(cart_count));

                                    CartCheckoutActivity.tvPriceItemsHeading.setText("Price (" + cart_count + " item)");
                                    CartCheckoutActivity.tvPriceItems.setText(context.getResources().getText(R.string.rupay_text) + total_amount);
                                    CartCheckoutActivity.tvAmountPayable.setText(context.getResources().getText(R.string.rupay_text) + total_amount);
                                    //CartCheckoutActivity.tvLastPayableAmount.setText(context.getResources().getText(R.string.rupay_text)+total_amount);

                                    System.out.println("cart updated " + result.toString());

                                    cartHolder.tvProductSubtotalPrice.setText(context.getResources().getText(R.string.rupay_text)+String.valueOf(cart_price));
                                    cartHolder.textView64.setText(itemList.get(position).quantity);

                                    //notifyDataSetChanged();
                                    progressBarHandler.hide();


                                }
                            }
                            else
                            {
                                progressBarHandler.hide();
                                Toast.makeText(context, "Server is not responding please try ", Toast.LENGTH_SHORT).show();

                            }

                        }
                        else
                        {
                            progressBarHandler.hide();
                            Toast.makeText(context, "Server is not responding please try ", Toast.LENGTH_SHORT).show();

                        }

                    }
                });


    }



}

