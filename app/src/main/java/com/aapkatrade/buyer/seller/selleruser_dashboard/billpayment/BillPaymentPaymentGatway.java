package com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.ParseUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.payment.PaymentCompletionActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.aapkatrade.buyer.home.cart.CartAdapter.appSharedPreference;


/**
 * Created by varun Kumar on 23/1/16.
 */
public class BillPaymentPaymentGatway extends Activity {

    private ArrayList<String> post_val = new ArrayList<String>();
    private String post_Data="";
    WebView webView ;
    final Activity activity = this;
    private String tag = "BillPaymentPaymentGatway";
    private String hash,hashSequence;
    ProgressDialog progressDialog ;

//    String merchant_key="zBxSQi"; // live
//    String salt="ZhraT96O"; // live

  /*  String merchant_key="Y9TTGAD1"; // comapny
   String salt="9Q1J4MqWWd"; // comapny
*/


    String merchant_key="kYz2vV"; // test
    String salt="zhoXe53j"; // test
    String action1 ="";
    String base_url="https://test.payu.in";
    //https://secure.payu.in
    // String base_url="https://secure.payu.in";//
    int error=0;
    String hashString="";
    Map<String,String> params;
    String txnid ="";

    String SUCCESS_URL = "https://www.payumoney.com/mobileapp/payumoney/success.php" ; // failed
    String FAILED_URL = "https://www.payumoney.com/mobileapp/payumoney/failure.php" ;
    AppSharedPreference app_sharedpreference;
    Handler mHandler = new Handler();
    String order_number;
    String jsonArrayMachineNOs;
    static String getFirstName, getNumber, getEmailAddress, getRechargeAmt;
    ProgressDialog pDialog ;



    @SuppressLint("JavascriptInterface") @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(activity);


        app_sharedpreference = new AppSharedPreference(getApplicationContext());

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        webView = new WebView(this);
        setContentView(webView);

        Intent oIntent  = getIntent();

        getFirstName    = oIntent.getExtras().getString("FIRST_NAME");
        getNumber       = oIntent.getExtras().getString("PHONE_NUMBER");
        getEmailAddress = oIntent.getExtras().getString("EMAIL_ADDRESS");
        getRechargeAmt  = oIntent.getExtras().getString("RECHARGE_AMT");
        jsonArrayMachineNOs = oIntent.getExtras().getString("MACHINE_ARRAY");

        Log.e("jsonArrayMachineNOs",jsonArrayMachineNOs);

        //post_val = getIntent().getStringArrayListExtra("post_val");
        //Log.d(tag, "post_val: "+post_val);
        params= new HashMap<String,String>();
        params.put("key", merchant_key);

        params.put("amount", getRechargeAmt);
        params.put("firstname", getFirstName);
        params.put("email", getEmailAddress);
        params.put("phone", getNumber);
        params.put("productinfo", "Recharge Wallet");
        params.put("surl", SUCCESS_URL);
        params.put("furl", FAILED_URL);
        params.put("service_provider", "payu_paisa");
        params.put("lastname", "");
        params.put("address1", "");
        params.put("address2", "");
        params.put("city", "");
        params.put("state", "");
        params.put("country", "");
        params.put("zipcode", "");
        params.put("udf1", "");
        params.put("udf2", "");
        params.put("udf3", "");
        params.put("udf4", "");
        params.put("udf5", "");
        params.put("pg", "");

		/*for(int i = 0;i<post_val.size();){
			params.put(post_val.get(i), post_val.get(i+1));

			i+=2;
		}*/


        if(empty(params.get("txnid"))){
            Random rand = new Random();
            String rndm = Integer.toString(rand.nextInt())+(System.currentTimeMillis() / 1000L);
            txnid=hashCal("SHA-256",rndm).substring(0,20);
            params.put("txnid", txnid);
        }
        else
            txnid=params.get("txnid");
        //String udf2 = txnid;
        //String txn="abcd";
        hash="";
        String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
        if(empty(params.get("hash")) && params.size()>0)
        {
            if( empty(params.get("key"))
                    || empty(params.get("txnid"))
                    || empty(params.get("amount"))
                    || empty(params.get("firstname"))
                    || empty(params.get("email"))
                    || empty(params.get("phone"))
                    || empty(params.get("productinfo"))
                    || empty(params.get("surl"))
                    || empty(params.get("furl"))
                    || empty(params.get("service_provider"))

                    ){
                error=1;
            }
            else{
                String[] hashVarSeq=hashSequence.split("\\|");

                for(String part : hashVarSeq)
                {
                    hashString= (empty(params.get(part)))?hashString.concat(""):hashString.concat(params.get(part));
                    hashString=hashString.concat("|");
                }
                hashString=hashString.concat(salt);


                hash=hashCal("SHA-512",hashString);
                action1=base_url.concat("/_payment");
            }
        }
        else if(!empty(params.get("hash")))
        {
            hash=params.get("hash");
            action1=base_url.concat("/_payment");
        }

        webView.setWebViewClient(new BillPaymentPaymentGatway.MyWebViewClient(){

            public void onPageFinished(WebView view, final String url) {
                progressDialog.dismiss();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //make sure dialog is showing
                if(! progressDialog.isShowing()){
                    progressDialog.show();
                }
            }


			/*@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "SslError! " +  error, Toast.LENGTH_SHORT).show();
				 handler.proceed();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "Page Started! " + url, Toast.LENGTH_SHORT).show();
				if(url.startsWith(SUCCESS_URL)){
					Toast

					.makeText(activity, "Payment Successful! " + url, Toast.LENGTH_SHORT).show();
					 Intent intent = new Intent(BillPaymentPaymentGatway.this, MainActivity.class);
					    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
					    startActivity(intent);
					    finish();
					    return false;
				}else if(url.startsWith(FAILED_URL)){
					Toast.makeText(activity, "Payment Failed! " + url, Toast.LENGTH_SHORT).show();
				    return false;
				}else if(url.startsWith("http")){
					return true;
				}
				//return super.shouldOverrideUrlLoading(view, url);
				return false;
			}*/


        });


        webView.setVisibility(0);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(2);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);

        //webView.addJavascriptInterface(new PayUJavaScriptInterface(getApplicationContext()), "PayUMoney");
        webView.addJavascriptInterface(new BillPaymentPaymentGatway.PayUJavaScriptInterface(), "PayUMoney");
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("key",merchant_key);
        mapParams.put("hash", BillPaymentPaymentGatway.this.hash);
        mapParams.put("txnid",(empty(BillPaymentPaymentGatway.this.params.get("txnid"))) ? "" : BillPaymentPaymentGatway.this.params.get("txnid"));
        Log.d(tag, "txnid: "+ BillPaymentPaymentGatway.this.params.get("txnid"));
        mapParams.put("service_provider","payu_paisa");

        mapParams.put("amount",(empty(BillPaymentPaymentGatway.this.params.get("amount"))) ? "" : BillPaymentPaymentGatway.this.params.get("amount"));
        mapParams.put("firstname",(empty(BillPaymentPaymentGatway.this.params.get("firstname"))) ? "" : BillPaymentPaymentGatway.this.params.get("firstname"));
        mapParams.put("email",(empty(BillPaymentPaymentGatway.this.params.get("email"))) ? "" : BillPaymentPaymentGatway.this.params.get("email"));
        mapParams.put("phone",(empty(BillPaymentPaymentGatway.this.params.get("phone"))) ? "" : BillPaymentPaymentGatway.this.params.get("phone"));

        mapParams.put("productinfo",(empty(BillPaymentPaymentGatway.this.params.get("productinfo"))) ? "" : BillPaymentPaymentGatway.this.params.get("productinfo"));
        mapParams.put("surl",(empty(BillPaymentPaymentGatway.this.params.get("surl"))) ? "" : BillPaymentPaymentGatway.this.params.get("surl"));
        mapParams.put("furl",(empty(BillPaymentPaymentGatway.this.params.get("furl"))) ? "" : BillPaymentPaymentGatway.this.params.get("furl"));
        mapParams.put("lastname",(empty(BillPaymentPaymentGatway.this.params.get("lastname"))) ? "" : BillPaymentPaymentGatway.this.params.get("lastname"));

        mapParams.put("address1",(empty(BillPaymentPaymentGatway.this.params.get("address1"))) ? "" : BillPaymentPaymentGatway.this.params.get("address1"));
        mapParams.put("address2",(empty(BillPaymentPaymentGatway.this.params.get("address2"))) ? "" : BillPaymentPaymentGatway.this.params.get("address2"));
        mapParams.put("city",(empty(BillPaymentPaymentGatway.this.params.get("city"))) ? "" : BillPaymentPaymentGatway.this.params.get("city"));
        mapParams.put("state",(empty(BillPaymentPaymentGatway.this.params.get("state"))) ? "" : BillPaymentPaymentGatway.this.params.get("state"));

        mapParams.put("country",(empty(BillPaymentPaymentGatway.this.params.get("country"))) ? "" : BillPaymentPaymentGatway.this.params.get("country"));
        mapParams.put("zipcode",(empty(BillPaymentPaymentGatway.this.params.get("zipcode"))) ? "" : BillPaymentPaymentGatway.this.params.get("zipcode"));
        mapParams.put("udf1",(empty(BillPaymentPaymentGatway.this.params.get("udf1"))) ? "" : BillPaymentPaymentGatway.this.params.get("udf1"));
        mapParams.put("udf2",(empty(BillPaymentPaymentGatway.this.params.get("udf2"))) ? "" : BillPaymentPaymentGatway.this.params.get("udf2"));

        mapParams.put("udf3",(empty(BillPaymentPaymentGatway.this.params.get("udf3"))) ? "" : BillPaymentPaymentGatway.this.params.get("udf3"));
        mapParams.put("udf4",(empty(BillPaymentPaymentGatway.this.params.get("udf4"))) ? "" : BillPaymentPaymentGatway.this.params.get("udf4"));
        mapParams.put("udf5",(empty(BillPaymentPaymentGatway.this.params.get("udf5"))) ? "" : BillPaymentPaymentGatway.this.params.get("udf5"));
        mapParams.put("pg",(empty(BillPaymentPaymentGatway.this.params.get("pg"))) ? "" : BillPaymentPaymentGatway.this.params.get("pg"));
        webview_ClientPost(webView, action1, mapParams.entrySet());

    }

	/*public class PayUJavaScriptInterface {

		@JavascriptInterface
        public void success(long id, final String paymentId) {
            mHandler.post(new Runnable() {
                public void run() {
                    mHandler = null;

                    new PostRechargeData().execute();

            		Toast.makeText(getApplicationContext(),"Successfully payment\n redirect from PayUJavaScriptInterface" ,Toast.LENGTH_LONG).show();

                }
            });
        }
	}*/


    private final class PayUJavaScriptInterface {

        PayUJavaScriptInterface() {
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void success(long id, final String paymentId) {
            mHandler.post(new Runnable() {
                public void run() {
                    mHandler = null;

	                  /*  Intent intent = new Intent();
	                    intent.putExtra(Constants.RESULT, "success");
	                    intent.putExtra(Constants.PAYMENT_ID, paymentId);
	                    setResult(RESULT_OK, intent);
	                    finish();*/
                    // new PostRechargeData().execute();

                    Log.e("bill_payment_id",paymentId);
                   callWebServiceMakePayment(paymentId,"success");

                    /*Intent intent=new Intent(BillPaymentPaymentGatway.this,CartCheckoutActivity.class);
                    intent.putExtra("test",getFirstName);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), "Successfully payment", Toast.LENGTH_LONG).show();*/

                }
            });
        }

        @JavascriptInterface
        public void failure(final String id, String error) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //cancelPayment();
                    Toast.makeText(getApplicationContext(),"Cancel payment" , Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public void failure() {
            failure("");
        }

        @JavascriptInterface
        public void failure(final String params) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

	                    /*Intent intent = new Intent();
	                    intent.putExtra(Constants.RESULT, params);
	                    setResult(RESULT_CANCELED, intent);
	                    finish();*/
                    Toast.makeText(getApplicationContext(),"Failed payment" , Toast.LENGTH_LONG).show();
                }
            });
        }

    }


    public void webview_ClientPost(WebView webView, String url, Collection< Map.Entry<String, String>> postData){
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>", url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");
        Log.d(tag, "webview_ClientPost called");

        //setup and load the progress bar
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading. Please wait...");
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }


    public void success(long id, final String paymentId) {

        mHandler.post(new Runnable() {
            public void run() {
                mHandler = null;

                //  new PostRechargeData().execute();

                Toast.makeText(getApplicationContext(),"Successfully payment\n redirect from Success Function" , Toast.LENGTH_LONG).show();

            }
        });
    }


    public boolean empty(String s)
    {
        if(s== null || s.trim().equals(""))
            return true;
        else
            return false;
    }

    public String hashCal(String type, String str){
        byte[] hashseq=str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try{
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();



            for (int i=0;i<messageDigest.length;i++) {
                String hex= Integer.toHexString(0xFF & messageDigest[i]);
                if(hex.length()==1) hexString.append("0");
                hexString.append(hex);
            }

        }catch(NoSuchAlgorithmException nsae){ }

        return hexString.toString();


    }

    //String SUCCESS_URL = "https://pay.in/sccussful" ; // failed
    //String FAILED_URL = "https://pay.in/failed" ;
    //override the override loading method for the webview client
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

        	/*if(url.contains("response.php") || url.equalsIgnoreCase(SUCCESS_URL)){

        		new PostRechargeData().execute();

        		Toast.makeText(getApplicationContext(),"Successfully payment\n redirect from webview" ,Toast.LENGTH_LONG).show();

                return false;
        	}else  */if(url.startsWith("http")){
                //Toast.makeText(getApplicationContext(),url ,Toast.LENGTH_LONG).show();
                progressDialog.show();
                view.loadUrl(url);
                System.out.println("myresult "+url);
                //return true;
            } else {
                return false;
            }

            return true;
        }
    }

    /******************************************* send record to back end ******************************************/
    /*class PostRechargeData extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(BillPaymentPaymentGatway.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }
        protected String doInBackground(String... args)
        {
            String strStatus = null;
            ProfileSessionManager ProSessionManager = new ProfileSessionManager(BillPaymentPaymentGatway.this);

            String getUserid   = ProSessionManager.getSpeculatorId();
            String getSpeculationId  = "0";
            String rechargeAmt = getRechargeAmt;
            String postAction = "1";
            //http://speculometer.com/webService/stockApp/speculationMoneyreports.php?
            //access_token=ISOFTINCstockAppCheckDevelop&speculator=1&speculation=&amount=1000&action=1
            ServiceHandler sh = new ServiceHandler();
            String upLoadServerUri = ServiceList.payment_money_url+"speculator="+getUserid+"&speculation="+getSpeculationId+"&amount="+rechargeAmt+"&action="+postAction;

            try{
                String jsonStr = sh.makeServiceCall(upLoadServerUri, ServiceHandler.POST);
                JSONObject jsonObj  = new JSONObject(jsonStr);

                JSONObject jobjDoc = jsonObj.optJSONObject("document");
                JSONObject jobjRes = jobjDoc.optJSONObject("response");

                strStatus   = jobjRes.getString("status");
                //strMessage  = jobjRes.getString("message");
                String strUserId = jobjRes.getString("user_id");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return strStatus;
        }

        protected void onPostExecute(final String strStatus)
        {

            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    pDialog.dismiss();
                    if(strStatus != null && strStatus.equalsIgnoreCase("0")){
                        Toast.makeText(getApplicationContext(),"Your recharge amount not added in wallet." ,Toast.LENGTH_LONG).show();
                    }else if(strStatus != null && strStatus.equalsIgnoreCase("1")){

                        Toast.makeText(getApplicationContext(),"Your recharge amount added in wallet." ,Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                }
            });

        }
    }*/


    /******************************************* closed send record to back end ************************************/


    private void callWebServiceMakePayment(String transactionId, String status)
    {

        String login_url = getApplicationContext().getResources().getString(R.string.webservice_base_url) + "/payment_bill";

        System.out.println("order_number--------------" +  status + transactionId + "fgdfgb----");

        String userid = app_sharedpreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "");

            Ion.with(BillPaymentPaymentGatway.this)
                    .load(login_url)
                    .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("seller_id",userid)
                    //.setBodyParameter("data_n", params.toString())
                    .setBodyParameter("transactionID", transactionId)
                    .setBodyParameter("status", status)
                    .setBodyParameter("machine_num", jsonArrayMachineNOs)
                    .setBodyParameter("payment", getRechargeAmt)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result)
                        {
                             //  AndroidUtils.showErrorLog(context,result,"dghdfghsaf dawbnedvhaewnbedvsab dsadduyf");
                             // progressBarHandler.hide();
                             System.out.println("result--------------" + result);

                            if (result.get("error").getAsString().contains("false"))
                            {
                                String payment_status;
                                JsonObject jsonObject = result.getAsJsonObject("result");

                                if (result.get("payment_status").getAsString().contains("false"))
                                {
                                    payment_status = "false";
                                    appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), 0);
                                }
                                else
                                {
                                    payment_status = "true";
                                    String cart_count = jsonObject.get("cart_item").getAsString();
                                    appSharedPreference.setSharedPrefInt(SharedPreferenceConstants.CART_COUNT.toString(), Integer.valueOf(cart_count));
                                }

                                AndroidUtils.showErrorLog(BillPaymentPaymentGatway.this, result.toString());

                                Intent intent = new Intent(BillPaymentPaymentGatway.this, PaymentCompletionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("isSuccess", payment_status);
                                intent.putExtra("vpc_Amount", jsonObject.get("amount").getAsString());
                                intent.putExtra("vpc_TransactionNo", jsonObject.get("transactionID").getAsString());
                                intent.putExtra("vpc_ReceiptNo", jsonObject.get("order_id").getAsString());
                                startActivity(intent);


                            }
                            else
                            {

                                Intent intent = new Intent(BillPaymentPaymentGatway.this, PaymentCompletionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("isSuccess", "false");
                                startActivity(intent);

                            }
                            //Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }

}