package com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aapkatrade.buyer.general.AppConfig;
import com.aapkatrade.buyer.general.Utils.ParseUtils;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.payment.PaymentCompletionActivity;
import com.aapkatrade.buyer.user_dashboard.address.viewpager.CartCheckoutActivity;
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
import java.util.Map;

public class BillPaymentActivity extends AppCompatActivity
{
    private Context context;
    private AppSharedPreference appSharedPreference;
    private TextView tvStatusTitle, tvStatusMsg, tvHeaderTransaction, tvSubHeaderTransaction,
            tvHeaderAmountPaid, tvSubHeaderAmountPaid, tvReceiptNo, tvDone;
    private ImageView tickImageView, circleImageView1, circleImageView2;
    private LinearLayout circleTile2Layout, circleTile1Layout, paymentCompletionRootLayout;
    private TextView rlSaveLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView tvAmount;
    private BillPaymentAdapter billPaymentAdapter;
    private ArrayList<BillPaymentListData> billPaymentListDatas = new ArrayList<>();

    private ArrayList<String> SelectedMachineNos=new ArrayList<>();
    private ProgressBarHandler progressBarHandler;
    public static CommonInterface commonInterface;
    public static final String TAG = "PayUMoneySDK Sample";
    String order_number;
    ArrayList<MachineTotalBillDatas> machineTotalBillDatases=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment);
        context = BillPaymentActivity.this;
        setUpToolBar();
        initView();

        callBillPayment();
    }

    private void callBillPayment()
    {

        recyclerView.setLayoutManager(linearLayoutManager);

        String webserviceUrlBillPayment = context.getString(R.string.webservice_base_url) + "/get_bill_payment";

        Ion.with(context)
                .load(webserviceUrlBillPayment)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("seller_id", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString()))
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3").asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {


                if (result != null) {
                    progressBarHandler.show();
                    if (result.get("error").getAsString().contains("false")) {


                        JsonArray jsonArrayResult = result.getAsJsonArray("result");

                        for (int k = 0; k < jsonArrayResult.size(); k++) {
                            JsonObject jsonObjectResult = jsonArrayResult.get(k).getAsJsonObject();


                            String machine_number = jsonObjectResult.get("machine_number").getAsString();
                            String machine_type = jsonObjectResult.get("machine_type").getAsString();
                            String machine_fee = jsonObjectResult.get("machine_fee").getAsString();

                            billPaymentListDatas.add(new BillPaymentListData(machine_type, machine_number, machine_fee, false, R.drawable.circle_sienna));


                        }
                        recyclerView.setLayoutManager(linearLayoutManager);

                        if (billPaymentAdapter == null) {


                            billPaymentAdapter = new BillPaymentAdapter(BillPaymentActivity.this, billPaymentListDatas);
                            recyclerView.setAdapter(billPaymentAdapter);
                        } else {
                            billPaymentAdapter.notifyDataSetChanged();
//
                        }
                        progressBarHandler.hide();

                    } else {
                        AndroidUtils.showErrorLog(context, "json_return_error", result.get("error").getAsString());
                        progressBarHandler.hide();
                    }


                } else {


                    progressBarHandler.hide();

                }


            }
        });


     /*   if (billPaymentAdapter == null) {

            billPaymentListDatas.add(new BillPaymentListData("POS", "5375475734", "250", false, R.drawable.circle_sienna));
            billPaymentListDatas.add(new BillPaymentListData("MPOS", "537547656", "1250", false, R.drawable.circle_purple));
            billPaymentListDatas.add(new BillPaymentListData("MPOS", "537547656", "1250", false, R.drawable.circle_purple));
            billPaymentListDatas.add(new BillPaymentListData("POS", "5375475734", "250", false, R.drawable.circle_sienna));
            billPaymentAdapter = new BillPaymentAdapter(BillPaymentActivity.this, billPaymentListDatas);
            recyclerView.setAdapter(billPaymentAdapter);
        } else {
            billPaymentAdapter.notifyDataSetChanged();
//
        }*/


    }


    private void initView()
    {
        appSharedPreference = new AppSharedPreference(context);
        progressBarHandler = new ProgressBarHandler(context);
        tvStatusTitle = (TextView) findViewById(R.id.tvStatusTitle);
        tvStatusMsg = (TextView) findViewById(R.id.tvStatusMsg);
        tickImageView = (ImageView) findViewById(R.id.tickImageView);
        tvAmount = (TextView) findViewById(R.id.tv_billing_amount2);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView_bill_payment);
        rlSaveLayout = (TextView) findViewById(R.id.tvDone);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);


        rlSaveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment();

            }
        });

        commonInterface = new CommonInterface() {
            @Override
            public Object getData(Object object) {



     machineTotalBillDatases= (ArrayList<MachineTotalBillDatas>) object;
                for(int i=0;i<machineTotalBillDatases.size();i++)
                {

                    SelectedMachineNos.add(machineTotalBillDatases.get(i).toString());

                }




                tvAmount.setText(new StringBuilder(getString(R.string.rupay_text)).append("  ").append(machineTotalBillDatases.get(machineTotalBillDatases.size()-1).TotalAmount));

                return null;
            }
        };


        tvAmount.setText(new StringBuilder(getString(R.string.rupay_text)).append("  0"));
    }

    private void setUpToolBar() {
        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        ImageView back_imagview = (ImageView) findViewById(R.id.back_imagview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AndroidUtils.setImageColor(homeIcon, context, R.color.white);
        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callHomeActivity();
            }
        });
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callHomeActivity();
            }
        });
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setElevation(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void callHomeActivity() {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


   /* private void callwebservice__save_order(String buyer_id)
    {

        progressBarHandler.show();
        String user_id = appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "notlogin");
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
                            makePayment();
                        }
                        else
                        {
                            progressBarHandler.hide();
                            AndroidUtils.showToast(context, message );
                        }

                    }
                });
    }*/

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


    public void makePayment()
    {

        String phone = "8882434664";
        String productName = "product_name";
        String firstName = "piyush";
        String txnId = "0nf7" + System.currentTimeMillis();
        String email="piyush.jain@payu.in";
        String sUrl = "https://test.payumoney.com/mobileapp/payumoney/success.php";
        String fUrl = "https://test.payumoney.com/mobileapp/payumoney/failure.php";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        boolean isDebug = true;
        String key = "dRQuiA";
        String merchantId = "4928174" ;

        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();

        builder.setAmount(Double.valueOf(tvAmount.getText().toString().replace(getApplicationContext().getResources().getText(R.string.rupay_text),"")))
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


        calculateServerSideHashAndInitiatePayment(paymentParam);

        // testing purpose

       /* String salt = "";
        String serverCalculatedHash=hashCal(key+"|"+txnId+"|"+getAmount()+"|"+productName+"|"
                +firstName+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"|"+salt);

        paymentParam.setMerchantHash(serverCalculatedHash);

        PayUmoneySdkInitilizer.startPaymentActivityForResult(MyActivity.this, paymentParam);*/



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

                            PayUmoneySdkInitilizer.startPaymentActivityForResult(BillPaymentActivity.this, paymentParam);
                        } else {
                            Toast.makeText(BillPaymentActivity.this,
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
                    Toast.makeText(BillPaymentActivity.this,
                            BillPaymentActivity.this.getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BillPaymentActivity.this,
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


    private void callWebServiceMakePayment(String transactionId, String status) {

        progressBarHandler.show();

        String login_url = context.getResources().getString(R.string.webservice_base_url) + "/payment_bill";

        System.out.println("order_number--------------" + order_number + status + transactionId + "fgdfgb----");

        if (SelectedMachineNos.size() != 0) {
         String  jsonArrayMachineNOs= String.valueOf(ParseUtils.ArrayListToJsonObject( SelectedMachineNos));
            AndroidUtils.showErrorLog(context,"machine_numers",jsonArrayMachineNOs);

            Ion.with(context)
                    .load(login_url)
                    .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    //.setBodyParameter("data_n", params.toString())
                    .setBodyParameter("order_id", order_number)
                    .setBodyParameter("transactionid", transactionId)
                    .setBodyParameter("status", status)
                    .setBodyParameter("machine_num", jsonArrayMachineNOs)
                    .setBodyParameter("payment", tvAmount.getText().toString())
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            //  AndroidUtils.showErrorLog(context,result,"dghdfghsaf dawbnedvhaewnbedvsab dsadduyf");
                            progressBarHandler.hide();

                            System.out.println("result--------------" + result);
                            if (result.get("error").getAsString().contains("false")) {
                                String payment_status;
                                JsonObject jsonObject = result.getAsJsonObject("result");

                                if (result.get("payment_status").getAsString().contains("false")) {
                                    payment_status = "false";
                                    appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0);
                                } else {
                                    payment_status = "true";
                                    String cart_count = jsonObject.get("cart_item").getAsString();
                                    appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), Integer.valueOf(cart_count));
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
        else{


            AndroidUtils.showErrorLog(context,"SelectedMachineNos","SelectedMachineNos size 0");
        }

    }
}
