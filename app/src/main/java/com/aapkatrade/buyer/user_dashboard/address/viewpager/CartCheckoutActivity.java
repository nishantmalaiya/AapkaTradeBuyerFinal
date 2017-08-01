package com.aapkatrade.buyer.user_dashboard.address.viewpager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.home.cart.CartData;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;

import com.aapkatrade.buyer.payment.PaymentActivity;
import com.aapkatrade.buyer.payment.PaymentCompletionActivity;
import com.aapkatrade.buyer.payumoney_paymentgatway.PayMentGateWay;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.payUMoney.sdk.SdkSession.hashCal;

public class CartCheckoutActivity extends AppCompatActivity
{

    RelativeLayout relativePayment;
    AppSharedPreference app_sharedpreference;
    ArrayList<CartData> cartDataArrayList = new ArrayList<>();
    private Context context;
    private ImageView locationImageView;
    public static TextView tvContinue, tvPriceItemsHeading, tvPriceItems, tvLastPayableAmount, tvAmountPayable,addressPhone,tvDelivery;
    RelativeLayout buttonContainer;
    RecyclerView mycartRecyclerView;
    CartCheckOutAdapter cartAdapter;
    private ProgressBarHandler progressBarHandler;
    public static CardView cardviewProductDeatails;
    public static LinearLayout linearAddressLayout;
    TextView addressLine1, addressLine2, addressLine3;
    String userid;
    int page = 1;
    LinearLayoutManager linearLayoutManager;
    public static final String TAG = "PayUMoneySDK Sample";
    String order_number,phone_number;
    Button button_changeAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart_checkout);

        context = CartCheckoutActivity.this;

        setuptoolbar();

        progressBarHandler = new ProgressBarHandler(context);

        app_sharedpreference = new AppSharedPreference(getApplicationContext());

        initView();

        setup_layout();

        mycartRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();
                if (totalItemCount > 0) {
                    if ((totalItemCount - 1) == lastVisibleItemCount) {
                        page = page + 1;
                        cartList(String.valueOf(page));
                    }
                }
            }
        });


    }


    private void initView()
    {

        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        linearAddressLayout = (LinearLayout) findViewById(R.id.linearAddressLayout);

        cardviewProductDeatails = (CardView) findViewById(R.id.cardviewProductDeatails);

        addressLine1 = (TextView) findViewById(R.id.addressLine1);

        addressLine2 = (TextView) findViewById(R.id.addressLine2);

        addressLine3 = (TextView) findViewById(R.id.addressLine3);

        addressPhone = (TextView) findViewById(R.id.addressPhone);

        addressLine1.setText(app_sharedpreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_NAME.toString(), ""));

        addressLine2.setText(app_sharedpreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS.toString(), ""));

        addressLine3.setText(app_sharedpreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_CITY.toString(), "")+"-"+app_sharedpreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PINCODE.toString(), ""));

        addressPhone.setText(app_sharedpreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PHONE.toString(), ""));

        locationImageView = (ImageView) findViewById(R.id.locationImageView);

        tvLastPayableAmount = (TextView) findViewById(R.id.tvLastPayableAmount);

        tvPriceItemsHeading = (TextView) findViewById(R.id.tvPriceItemsHeading);

        tvAmountPayable = (TextView) findViewById(R.id.tvAmountPayable);

        tvPriceItems = (TextView) findViewById(R.id.tvPriceItems);

        tvDelivery = (TextView) findViewById(R.id.tvDelivery);

        tvPriceItems.setText(getApplicationContext().getResources().getText(R.string.rupay_text) + "4251");

        tvAmountPayable.setText(getApplicationContext().getResources().getText(R.string.rupay_text) + "4251");

        AndroidUtils.setImageColor(locationImageView, context, R.color.green);

    }


    private void setup_layout() {

        relativePayment = (RelativeLayout) findViewById(R.id.relativePayment);

        mycartRecyclerView = (RecyclerView) findViewById(R.id.order_list);

        button_changeAddress = (Button) findViewById(R.id.button_changeAddress);

        button_changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cartList("0");

       relativePayment.setOnClickListener(new View.OnClickListener()
       {
            @Override
            public void onClick(View v)
            {
                String userid = app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");

                for (int i=0 ; i< cartDataArrayList.size(); i++)
                {
                    if (cartDataArrayList.get(i).available_status.toString().equals("false"))
                    {
                       AndroidUtils.showToast(context,"Please Remove Items those not deliver to this pincode");
                        break;
                    }

                    if (i== cartDataArrayList.size()-1)
                    {
                        callwebservice__save_order(userid);
                        break;
                    }

                }

               //     makePayment();
            }
        });


    }


    private void setuptoolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    private void cartList(String pageNumber)
    {
        progressBarHandler.show();

        String user_id = app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");

        if (user_id.equals("notlogin"))
        {
            user_id = "";
        }

        Ion.with(CartCheckoutActivity.this)
                .load(getResources().getString(R.string.webservice_base_url) + "/list_cart")
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))
                .setBodyParameter("user_id", user_id)
                .setBodyParameter("pincode",app_sharedpreference.getSharedPref(SharedPreferenceConstants.SHIPPING_ADDRESS_PINCODE.toString(), ""))
                .setBodyParameter("user_type",app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), ""))
                .setBodyParameter("page", pageNumber)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        if (result != null) {
                            AndroidUtils.showErrorLog(context, "-jsonObject------------" + result.toString());

                            JsonObject jsonObject = result.getAsJsonObject("result");
                            String product_total_amount = jsonObject.get("product_total_amount").getAsString();
                            String cart_count = jsonObject.get("total_qty").getAsString();
                            String total_amount = jsonObject.get("total_amount").getAsString();
                            String total_shipping_charge = jsonObject.get("total_shipping").getAsString();

                            tvPriceItemsHeading.setText("Price (" + cart_count + " item)");
                            tvPriceItems.setText(getApplicationContext().getResources().getText(R.string.rupay_text) + product_total_amount);
                            tvAmountPayable.setText(getApplicationContext().getResources().getText(R.string.rupay_text) + total_amount);
                            tvDelivery.setText(getApplicationContext().getResources().getText(R.string.rupay_text) + total_shipping_charge);

                            // tvLastPayableAmount.setText(getApplicationContext().getResources().getText(R.string.rupay_text)+total_amount);

                            JsonArray jsonProductList = jsonObject.getAsJsonArray("items");
                            if (jsonProductList != null && jsonProductList.size() > 0)
                            {
                                for (int i = 0; i < jsonProductList.size(); i++)
                                {
                                    JsonObject jsonproduct = (JsonObject) jsonProductList.get(i);
                                    String Id = jsonproduct.get("id").getAsString();
                                    String productName = jsonproduct.get("name").getAsString();
                                    String productqty = jsonproduct.get("quantity").getAsString();
                                    String price = jsonproduct.get("price").getAsString();
                                    String subtotal_price = jsonproduct.get("sub_total").getAsString();
                                    System.out.println("price--------------------" + price);

                                    String productImage = jsonproduct.get("image_url").getAsString();
                                    String product_id = jsonproduct.get("product_id").getAsString();
                                    String available_status = jsonproduct.get("available_status").getAsString();
                                    String shipping_cost = jsonproduct.get("shipping_cost").getAsString();

                                    cartDataArrayList.add(new CartData(Id, productName, productqty, price, productImage, product_id, subtotal_price,available_status,shipping_cost));
                                }
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                mycartRecyclerView.setLayoutManager(mLayoutManager);
                                cartAdapter = new CartCheckOutAdapter(context, cartDataArrayList);
                                mycartRecyclerView.setAdapter(cartAdapter);

                                linearAddressLayout.setVisibility(View.VISIBLE);
                                cardviewProductDeatails.setVisibility(View.VISIBLE);

                            }
                        } else {
                            AndroidUtils.showErrorLog(context, "-jsonObject------------NULL RESULT FOUND");
                        }

                    }
                });
    }


    private void callwebservice__save_order(String buyer_id)
    {

        progressBarHandler.show();
        String user_id = app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");
        String login_url = context.getResources().getString(R.string.webservice_base_url) + "/save_order";
        Ion.with(context)
                .load(login_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("buyer_id", buyer_id)
                .setBodyParameter("user_id",user_id)
                .setBodyParameter("device_id", AppConfig.getCurrentDeviceId(context))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        //  AndroidUtils.showErrorLog(context,result,"dghdfghsaf dawbnedvhaewnbedvsab dsadduyf");
                       // System.out.println("result-----------" + result.toString());

                        String message = result.get("message").getAsString();
                        if (message.equals("Order Saved Successfully!"))
                        {

                            JsonObject jsonObject = result.getAsJsonObject("result");
                            order_number = jsonObject.get("tracking_no").getAsString();
                            progressBarHandler.hide();
                            //makePayment();
                            open_payumoney_webview();
                        }
                        else
                        {
                            progressBarHandler.hide();
                            AndroidUtils.showToast(context, message );
                        }

                    }
                });
    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private double getAmount()
    {
        Double amount = 10.0;
        return amount;
    }


    public void open_payumoney_webview()

    {

        String getFname,getPhone,getEmail,getAmt;

        if(Validation.isNonEmptyStr(app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(),"Aapka Trade")))
        {
           getFname = app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), "Aapka Trade");
        }
        else {
        getFname = "Aapka Trade";
        }

        if(Validation.isNonEmptyStr(app_sharedpreference.getSharedPref(SharedPreferenceConstants.MOBILE.toString())))
        {
            getPhone = app_sharedpreference.getSharedPref(SharedPreferenceConstants.MOBILE.toString());
        }
        else {
            getPhone = getApplicationContext().getResources().getText(R.string.customer_care_no).toString();
        }

        if(Validation.isNonEmptyStr(app_sharedpreference.getSharedPref(SharedPreferenceConstants.EMAIL_ID.toString())))
        {
            getEmail = app_sharedpreference.getSharedPref(SharedPreferenceConstants.EMAIL_ID.toString());
        }
        else {
            getEmail = "info@aapkatrade.com";
        }

         getAmt   = tvAmountPayable.getText().toString().replace(getApplicationContext().getResources().getText(R.string.rupay_text),"");//rechargeAmt.getText().toString().trim();

        AndroidUtils.showErrorLog(context,"Fname--"+getFname +"Phone"+getPhone +"Email--"+getEmail+"Amit--"+getAmt);


        Intent intent = new Intent(CartCheckoutActivity.this, PayMentGateWay.class);
        intent.putExtra("FIRST_NAME",getFname);
        intent.putExtra("PHONE_NUMBER",getPhone);
        intent.putExtra("EMAIL_ADDRESS",getEmail);
        intent.putExtra("RECHARGE_AMT",getAmt);
        intent.putExtra("ORDER_NUMBER",order_number);
        startActivity(intent);

    }

    public void makePayment()
    {

        String phone = "8882434664";
        String productName = "product_name";
        String firstName = "piyush";
        String txnId = "0nf7" + System.currentTimeMillis();
        String email="piyush.jain@payu.in";
        String sUrl = "https://www.payumoney.com/mobileapp/payumoney/success.php";
        String fUrl = "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        boolean isDebug = false;
        String key = "rrPYm3Tz";
        String merchantId = "5704896" ;

        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();

        builder.setAmount(Double.valueOf("1.00"))
                .setTnxId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(sUrl)
                .setfUrl(fUrl)
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setIsDebug(isDebug)
                .setKey(key)
                .setMerchantId(merchantId);

        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();

          // server side call required to calculate hash with the help of <salt>
         //  <salt> is already shared along with merchant <key>
         /*        serverCalculatedHash =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|<salt>)
             (e.g.)
             sha512(FCstqb|0nf7|10.0|product_name|piyush|piyush.jain@payu.in||||||MBgjYaFG)
             9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc
        */
        // Recommended


      // calculateServerSideHashAndInitiatePayment(paymentParam);

       // testing purpose

        String salt = "QjMbJt8I7V";
        String serverCalculatedHash=hashCal(key+"|"+txnId+"|"+getAmount()+"|"+productName+"|"
                +firstName+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"|"+salt);

        paymentParam.setMerchantHash(serverCalculatedHash);

        PayUmoneySdkInitilizer.startPaymentActivityForResult(CartCheckoutActivity.this, paymentParam);



    }

    private void calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitilizer.PaymentParam paymentParam)
    {
        // Replace your server side hash generator API URL
        String url = "https://test.payumoney.com/payment/op/calculateHashForTest";

        Toast.makeText(this, "Please wait... Generating hash from server ... ", Toast.LENGTH_LONG).show();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has(SdkConstants.STATUS)) {
                        String status = jsonObject.optString(SdkConstants.STATUS);
                        if (status != null || status.equals("1")) {

                            String hash = jsonObject.getString(SdkConstants.RESULT);
                            Log.i("app_activity", "Server calculated Hash :  " + hash);

                            paymentParam.setMerchantHash(hash);

                            PayUmoneySdkInitilizer.startPaymentActivityForResult(CartCheckoutActivity.this, paymentParam);
                        } else {
                            Toast.makeText(CartCheckoutActivity.this,
                                    jsonObject.getString(SdkConstants.RESULT),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(CartCheckoutActivity.this,
                            CartCheckoutActivity.this.getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CartCheckoutActivity.this,
                            error.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paymentParam.getParams();
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);




    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE)
        {
            /*if(data != null && data.hasExtra("result")){
              String responsePayUmoney = data.getStringExtra("result");
                if(SdkHelper.checkForValidString(responsePayUmoney))
                    showDialogMessage(responsePayUmoney);
            } else {
                showDialogMessage("Unable to get Status of Payment");
            }*/
            if (resultCode == RESULT_OK)
            {
                Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
               // showDialogMessage("Payment Success Id : " + paymentId);
                 callWebServiceMakePayment(paymentId,"true");

            } else if (resultCode == RESULT_CANCELED)
            {
                Log.i(TAG, "failure");
                //String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
               // callWebServiceMakePayment(paymentId,"false");
                showDialogMessage("Payment Cancelled");

            }
            else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED)
            {
                Log.i("app_activity", "failure");

                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {
                        showDialogMessage("Payment failure");
                    }
                }
                //Write your code if there's no result
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
                Log.i(TAG, "User returned without login");
                showDialogMessage("User returned without login");
            }
        }
    }

    private void showDialogMessage(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aapka Trade");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();

    }


    private void callWebServiceMakePayment(String transactionId, String status)
    {

        progressBarHandler.show();

        String login_url = context.getResources().getString(R.string.webservice_base_url) + "/make_payment";

        System.out.println("order_number--------------" + order_number+status+transactionId+"fgdfgb----");

        Ion.with(context)
                .load(login_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                //.setBodyParameter("data_n", params.toString())
                .setBodyParameter("order_id", order_number)
                .setBodyParameter("transactionid", transactionId)
                .setBodyParameter("status", status)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        //  AndroidUtils.showErrorLog(context,result,"dghdfghsaf dawbnedvhaewnbedvsab dsadduyf");
                        progressBarHandler.hide();

                        System.out.println("result--------------" + result);
                        if (result.get("error").getAsString().contains("false"))
                        {
                            String payment_status;
                            JsonObject jsonObject = result.getAsJsonObject("result");

                            if(result.get("payment_status").getAsString().contains("false"))
                            {
                                payment_status = "false";
                                app_sharedpreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0);
                            }
                            else
                            {
                                payment_status = "true";
                                String cart_count = jsonObject.get("cart_item").getAsString();
                                app_sharedpreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), Integer.valueOf(cart_count));
                            }

                            AndroidUtils.showErrorLog(context, result.toString());

                            Intent intent = new Intent(context, PaymentCompletionActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("isSuccess", payment_status);
                            intent.putExtra("vpc_Amount", jsonObject.get("amount").getAsString());
                            intent.putExtra("vpc_TransactionNo", jsonObject.get("transactionID").getAsString());
                            intent.putExtra("vpc_ReceiptNo", jsonObject.get("order_id").getAsString());
                            startActivity(intent);


                        } else {
                            Intent intent = new Intent(context, PaymentCompletionActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("isSuccess", "false");
                            startActivity(intent);
                        }
                        //Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
