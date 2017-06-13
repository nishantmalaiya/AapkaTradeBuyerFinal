package com.aapkatrade.buyer.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.aapkatrade.buyer.home.cart.CartAdapter;
import com.aapkatrade.buyer.home.cart.MyCartActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressDialogHandler;
import com.aapkatrade.buyer.shopdetail.productdetail.ProductDetailActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

@SuppressLint("ValidFragment")
public class CustomQuantityDialog extends DialogFragment
{

    EditText etManualQuantity;
    TextView okTv, CancelTv, tvsubtotal, textView_qty;
    public static CommonInterface commonInterface;
    Context context;
    int pos;
    String price;
    ProgressDialogHandler progressDialogHandler;
    AppSharedPreference appSharedPreference;


    public CustomQuantityDialog(Context context) {
        this.context = context;
    }

    public CustomQuantityDialog(Context context, TextView textView, int position, String price, TextView textView_qty) {
        this.context = context;
        this.tvsubtotal = textView;
        this.textView_qty = textView_qty;
        this.pos = position;
        this.price = price;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_more_quantity, container, false);
        //noinspection ConstantConditions
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        initView(v);

        return v;
    }


    private void initView(View v) {
        context = getContext();
        progressDialogHandler = new ProgressDialogHandler(context);
        appSharedPreference = new AppSharedPreference(context);
        etManualQuantity = (EditText) v.findViewById(R.id.editText);
        okTv = (TextView) v.findViewById(R.id.okDialog);
        CancelTv = (TextView) v.findViewById(R.id.cancelDialog);

        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etManualQuantity.getText().toString().trim().equals("")) {
                    etManualQuantity.setError("Please Enter Number");

                }
                  else {
                    if (Integer.parseInt(etManualQuantity.getText().toString().trim()) > 0)
                    {
                        if (context instanceof ProductDetailActivity) {

                            commonInterface.getData(etManualQuantity.getText().toString().trim());
                        }

                        else if (CartAdapter.itemList != null && CartAdapter.itemList.size() >= pos) {
                            callwebserviceUpdateCart(CartAdapter.itemList.get(pos).id, 1, etManualQuantity.getText().toString(), CartAdapter.itemList.get(pos).product_id);
                        }

                       /* if (callwebserviceUpdateCart(CartAdapter.itemList.get(pos).id,1,etManualQuantity.getText().toString(),CartAdapter.itemList.get(pos).product_id))
                        {
                            textView_qty.setText(etManualQuantity.getText().toString().trim());
                            double cart_price = Double.valueOf(price) *Integer.valueOf(etManualQuantity.getText().toString().trim());
                            System.out.println("cart_price dailog----------"+cart_price);
                            tvsubtotal.setText(context.getResources().getText(R.string.Rs)+String.valueOf(cart_price));
                            commonInterface.getData(Integer.parseInt(etManualQuantity.getText().toString().trim()));

                        }
                        else
                        {
                           System.out.println("call webservice is not ");
                        }*/

                        dismiss();
                    } else {


                    }
                    AndroidUtils.showErrorLog(getActivity(), "ok button");

                }


            }
        });

        CancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AndroidUtils.showErrorLog(getActivity(), "cancel button");
                dismiss();
            }
        });

        etManualQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (etManualQuantity.getText().toString().trim().equals("")) {
                    etManualQuantity.setError("Please Enter Number");
                } else {
                    if (s.length() != 0) {
                        if (Integer.parseInt(s.toString()) == 0) {
                            etManualQuantity.setError("Please Enter Valid Quantity");
                        } else {
                            //tvQuantity.setText(s);
                        }
                    }
                }
            }
        });


    }


    public void callwebserviceUpdateCart(String cart_id, final int position, String cart_quantity, String cart_product_id) {

        progressDialogHandler.show();

        String cart_url = context.getResources().getString(R.string.webservice_base_url) + "/cart_update";

        String cart_user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");
        if (cart_user_id.equals("notlogin")) {
            cart_user_id = "";
        }

        AndroidUtils.showErrorLog(context, "cart detail", cart_id + "******" + cart_product_id + "******" + cart_quantity + "******" + cart_user_id + "***" + AppConfig.getCurrentDeviceId(context));

        Ion.with(context)
                .load(cart_url)
                .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("id", cart_id)
                .setBodyParameter("product_id", cart_product_id)
                .setBodyParameter("quantity", cart_quantity)
                .setBodyParameter("user_id", cart_user_id)
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {



                        if (result != null) {

                            String error_message = result.get("error").getAsString();

                            if (error_message.equals("false")) {
                                String message = result.get("message").getAsString();

                                if (message.equals("Product quantity exceeded")) {
                                    progressDialogHandler.hide();
                                    AndroidUtils.showToast(context, "Product quantity exceeded.");
                                } else if (message.equals("Failed to update cart")) {
                                    progressDialogHandler.hide();

                                    AndroidUtils.showToast(context, "Failed to update cart.");

                                } else if (message.equals("Invalid Device ID!")) {
                                    progressDialogHandler.hide();
                                    AndroidUtils.showToast(context, "Invalid Device ID!");
                                } else {
                                    JsonObject jsonresult = result.getAsJsonObject("result");

                                    String total_amount = jsonresult.get("total_amount").getAsString();
                                    String cart_count = jsonresult.get("total_qty").getAsString();

                                    System.out.println("cart_count--------------"+cart_count);

                                    appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), Integer.valueOf(cart_count));

                                    //HomeActivity.tvCartCount.setText(String.valueOf(appSharedPreference.getSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0)));

                                    MyCartActivity.tvPriceItemsHeading.setText("Price (" + cart_count + " items)");
                                    MyCartActivity.tvPriceItems.setText(context.getResources().getText(R.string.rupay_text) + total_amount);
                                    MyCartActivity.tvAmountPayable.setText(context.getResources().getText(R.string.rupay_text) + total_amount);
                                    MyCartActivity.tvLastPayableAmount.setText(context.getResources().getText(R.string.rupay_text) + total_amount);

                                    System.out.println("cart updated " + result.toString());

                                    textView_qty.setText(etManualQuantity.getText().toString().trim());
                                    double cart_price = Double.valueOf(price) * Integer.valueOf(etManualQuantity.getText().toString().trim());
                                    System.out.println("cart_price dailog----------" + cart_price);
                                    tvsubtotal.setText(context.getResources().getText(R.string.rupay_text) + String.valueOf(cart_price));

                                    System.out.println("etManualQuantity.getText()---------"+etManualQuantity.getText());

                                    //commonInterface.getData(Integer.parseInt(etManualQuantity.getText().toString().trim()));

                                    //notifyDataSetChanged();
                                    progressDialogHandler.hide();

                                }

                            } else {
                                progressDialogHandler.hide();
                                AndroidUtils.showToast(context, "Server is not responding. Please try again.");
                            }
                        } else {
                            progressDialogHandler.hide();
                            AndroidUtils.showToast(context, "Server is not responding. Please try again.");
                        }


                    }
                });


    }


}
