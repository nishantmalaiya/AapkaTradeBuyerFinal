package com.aapkatrade.buyer.payment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PaymentActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context context;
    String SECURE_SECRET = "F025C47B53D2FBD4D6B02ED5C0F8A696";
    String accessCode = "28DCA441";
    String merchantId = "TESTCFZATRDPRLTD";
    String amount = "10";
    String orderid, order_number;
    String Merchant_transaction_reference = "756756876";
    String HashData_prepare;
    String success_url = "http://staging.aapkatrade.com/payment/PHP_VPC_3Party_Order_DR.php";
    String hash_pack;
    String url;
    Handler mHandler = new Handler();
    JSONObject json;
    ArrayList<String> paymeny_data = new ArrayList<>();

    List<String> vpc_AuthorizeId = new ArrayList<>();
    private int[] tabIcons = {

            R.drawable.new_order_wht,

            R.drawable.cancel_order_wht,

            R.drawable.shipped_order_wht,

            R.drawable.complete_order_wht,
    };
    private WebView webview;
    private ProgressBarHandler progressBarHandler;

    String buyer_id;
    List<String> values;
    Map<String, List<String>> params;
    AppSharedPreference appSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);
        context = PaymentActivity.this;

        Bundle bundle = getIntent().getExtras();

        appSharedPreference = new AppSharedPreference(context);

        String price = bundle.getString("price");
        buyer_id = bundle.getString("userid");
        order_number = bundle.getString("order_number");
        double damount = Double.valueOf(price);
        int amount = (int) damount;
        setUpToolBar();
        webview = (WebView) findViewById(R.id.webview);
        progressBarHandler = new ProgressBarHandler(this);
        progressBarHandler.show();

        Ion.with(PaymentActivity.this)
                .load("http://staging.aapkatrade.com/payment/PHP_VPC_3Party_Order_DO.php")
                .setBodyParameter("vpc_Version", "1")
                .setBodyParameter("vpc_Command", "pay")
                .setBodyParameter("vpc_AccessCode", accessCode)
                .setBodyParameter("vpc_Merchant", merchantId)
                .setBodyParameter("vpc_OrderInfo", "Test Product")
                .setBodyParameter("vpc_Amount", String.valueOf(amount * 100))
                .setBodyParameter("vpc_Currency", "INR")
                .setBodyParameter("vpc_Locale", "en_INR")
                .setBodyParameter("virtualPaymentClientURL", "https://migs.mastercard.com.au/vpcpay")
                .setBodyParameter("vpc_MerchTxnRef", order_number)
                .setBodyParameter("vpc_ReturnURL", "http://staging.aapkatrade.com/payment/PHP_VPC_3Party_Order_DR.php")

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        AndroidUtils.showErrorLog(context, "**********", result.toString());
                        url = result.get("url").getAsString();
                        webview.getSettings().setBuiltInZoomControls(true);
                        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                        webview.getSettings().setDomStorageEnabled(true);
                        webview.clearHistory();
                        webview.clearCache(true);
                        webview.getSettings().setJavaScriptEnabled(true);
                        webview.getSettings().setSupportZoom(true);
                        webview.getSettings().setUseWideViewPort(false);
                        webview.getSettings().setLoadWithOverviewMode(false);
                        CookieManager.getInstance().setAcceptCookie(true);
                        webview.loadUrl(url);

                        webview.setWebChromeClient(new WebChromeClient() {
                            @Override
                            public void onReceivedTitle(WebView view, String title) {
                                getWindow().setTitle(title);
                                view.isPrivateBrowsingEnabled();//Set Activity tile to page title.
                            }
                        });

                        webview.setWebViewClient(new WebViewClient() {
                            @SuppressLint("SetJavaScriptEnabled")
                            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                                if (urlx.contains(success_url)) {
                                    Log.e("html2", urlx);

                                    params = new HashMap<>();
                                    String[] urlParts = urlx.split("\\?");
                                    if (urlParts.length > 1) {
                                        String query = urlParts[1];
                                        System.out.println("query---------" + query);

                                        for (String param : query.split("&")) {
                                            String pair[] = param.split("=");

                                            String key = null;
                                            try {
                                                key = URLDecoder.decode(pair[0], "UTF-8");

                                                String value = "";
                                                if (pair.length > 1) {
                                                    value = URLDecoder.decode(pair[1], "UTF-8");

                                                    paymeny_data.add(value);

                                                    System.out.println("value_data---------" + value);


                                                }
                                                values = params.get(key);
                                                if (values == null) {
                                                    values = new ArrayList<>();
                                                    params.put(key, values);
                                                }
                                                AndroidUtils.showErrorLog(context, params, "_____*****________");

                                                values.add(value);
                                                System.out.println("values---------" + values);
                                            } catch (UnsupportedEncodingException e1) {
                                                e1.printStackTrace();
                                            }
                                        }

                                        System.out.println("values2---------" + values);
                                    }
                                    Log.e("queryParameter", params.toString());
                                    //viewx.loadUrl(urlx);

                                    callWebServiceMakePayment(values);
                                }
                                viewx.isPrivateBrowsingEnabled();
                                viewx.getSettings().setJavaScriptEnabled(true);
                                progressBarHandler.hide();
                                return false;
                            }

                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                super.onPageStarted(view, url, favicon);
                                progressBarHandler.show();
                            }

                            @Override
                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                // TODO Auto-generated method stub
                                System.out.println(">>>>>>>>>>>>>>onReceivedError>>>>>>>>>>>>>>>>>>");
                                AndroidUtils.showToast(context, "Oh no!");
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                progressBarHandler.hide();
                                Log.e("pagefinished", "pagefinished");

                                //super.onPageFinished(view, url);
//                                webview.loadUrl("javascript:window.HtmlViewer.showHTML" +
//                                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                            }
                        });
                    }


                });
    }

    private void setUpToolBar() {
        AppCompatImageView homeIcon = (AppCompatImageView) findViewById(R.id.logoWord);
        AppCompatImageView back_imagview = (AppCompatImageView) findViewById(R.id.back_imagview);

        back_imagview.setVisibility(View.VISIBLE);

        back_imagview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Transaction Cancel?")
                        .setMessage("Are you sure you want to cancel this transaction?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent intent = new Intent(context, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        TextView header_name = (TextView) findViewById(R.id.header_name);
       // header_name.setVisibility(View.VISIBLE);
        header_name.setText(getResources().getString(R.string.select_payment_mode_title));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.e("hi---", "IIIIIIIII" + tab.getPosition());
        if (tab.getPosition() == 0) {
            // tab.setIcon(AndroidUtils.setImageColor(context,tabIcons[0],R.color.orange));
            tabLayout.setTabTextColors(Color.parseColor("#066C57"), Color.parseColor("#FF6600"));
        } else if (tab.getPosition() == 1) {
            //tab.setIcon(AndroidUtils.setImageColor(context,tabIcons[1],R.color.orange));
            tabLayout.setTabTextColors(Color.parseColor("#066C57"), Color.parseColor("#FF6600"));
        } else if (tab.getPosition() == 2) {
            // tab.setIcon(AndroidUtils.setImageColor(context,tabIcons[2],R.color.orange));
            tabLayout.setTabTextColors(Color.parseColor("#066C57"), Color.parseColor("#FF6600"));
        } else if (tab.getPosition() == 3) {
            //tab.setIcon(AndroidUtils.setImageColor(context,tabIcons[3],R.color.orange));
            tabLayout.setTabTextColors(Color.parseColor("#066C57"), Color.parseColor("#FF6600"));
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            // tab.setIcon(AndroidUtils.setImageColor(context,tabIcons[0],R.color.text_order_tab));
            tabLayout.setTabTextColors(Color.parseColor("#066C57"), Color.parseColor("#FF6600"));
        } else if (tab.getPosition() == 1) {
            //tab.setIcon(AndroidUtils.setImageColor(context,tabIcons[1],R.color.text_order_tab));
            tabLayout.setTabTextColors(Color.parseColor("#066C57"), Color.parseColor("#FF6600"));
        } else if (tab.getPosition() == 2) {
            // tab.setIcon(AndroidUtils.setImageColor(context,tabIcons[2],R.color.text_order_tab));
            tabLayout.setTabTextColors(Color.parseColor("#066C57"), Color.parseColor("#FF6600"));
        } else if (tab.getPosition() == 3) {
            //tab.setIcon(AndroidUtils.setImageColor(context,tabIcons[3],R.color.text_order_tab));
            tabLayout.setTabTextColors(Color.parseColor("#066C57"), Color.parseColor("#FF6600"));
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            //tab.setIcon(AndroidUtils.setImageColor(context,tabIcons[0],R.color.orange));
            tabLayout.setTabTextColors(Color.parseColor("#066C57"), Color.parseColor("#FF6600"));
        } else if (tab.getPosition() == 1) {
            // tab.setIcon(AndroidUtils.setImageColor(context,tabIcons[1],R.color.orange));
            tabLayout.setTabTextColors(Color.parseColor("#066C57"), Color.parseColor("#FF6600"));
        } else if (tab.getPosition() == 2) {
            // tab.setIcon(AndroidUtils.setImageColor(context,tabIcons[2],R.color.orange));
            tabLayout.setTabTextColors(Color.parseColor("#066C57"), Color.parseColor("#FF6600"));
        } else if (tab.getPosition() == 3) {
            //tab.setIcon(AndroidUtils.setImageColor(context,tabIcons[3],R.color.orange));
            tabLayout.setTabTextColors(Color.parseColor("#066C57"), Color.parseColor("#FF6600"));
        }
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(AndroidUtils.setImageColor(context, tabIcons[0], R.color.orange));
        tabLayout.getTabAt(1).setIcon(AndroidUtils.setImageColor(context, tabIcons[1], R.color.text_order_tab));
        tabLayout.getTabAt(2).setIcon(AndroidUtils.setImageColor(context, tabIcons[2], R.color.text_order_tab));
        tabLayout.getTabAt(3).setIcon(AndroidUtils.setImageColor(context, tabIcons[3], R.color.text_order_tab));
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private void callWebServiceMakePayment(List<String> data)
    {

        progressBarHandler.show();

        List<String> order_id = params.get("vpc_MerchTxnRef");
        List<String> transaction_no = params.get("vpc_TransactionNo");
        List<String> batch_no = params.get("vpc_BatchNo");

        if (params.get("vpc_AuthorizeId") == null) {


            vpc_AuthorizeId.add("");
        } else {
            vpc_AuthorizeId = params.get("vpc_AuthorizeId");
        }

        List<String> vpc_Card = params.get("vpc_Card");
        List<String> vpc_Message = params.get("vpc_Message");
        final List<String> vpc_ReceiptNo = params.get("vpc_ReceiptNo");
        final List<String> vpc_TransactionNo = params.get("vpc_TransactionNo");
        final List<String> vpc_Amount = params.get("vpc_Amount");


        //  System.out.println("data-----------data" + params.toString());

//        System.out.println("order_id--"+order_id.toString()+"transaction_no--"+transaction_no.toString()+"batch_no--"+batch_no.toString()+"vpc_AuthorizeId--"+vpc_AuthorizeId.toString()+"vpc_Card--"+vpc_Card.toString()+"vpc_Message--"+vpc_Message.toString()+"vpc_ReceiptNo--"+vpc_ReceiptNo.toString()+"vpc_TransactionNo--"+vpc_TransactionNo.toString());

        String login_url = context.getResources().getString(R.string.webservice_base_url) + "/make_payment";


        System.out.println("login_url--------------" + login_url);
        Ion.with(context)
                .load(login_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                //.setBodyParameter("data_n", params.toString())
                .setBodyParameter("vpc_BatchNo", batch_no.get(0))
                .setBodyParameter("order_id", order_id.get(0))
                .setBodyParameter("vpc_AuthorizeId", vpc_AuthorizeId.get(0))
                .setBodyParameter("vpc_Card", vpc_Card.get(0))
                .setBodyParameter("vpc_Message", vpc_Message.get(0))
                .setBodyParameter("vpc_ReceiptNo", vpc_ReceiptNo.get(0))
                .setBodyParameter("vpc_TransactionNo", vpc_TransactionNo.get(0))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        //  AndroidUtils.showErrorLog(context,result,"dghdfghsaf dawbnedvhaewnbedvsab dsadduyf");
                        progressBarHandler.hide();

                        if (result.get("error").getAsString().contains("false"))
                        {

                            String payment_status;
                            JsonObject jsonObject = result.getAsJsonObject("result");

                            


                           if(result.get("payment_status").getAsString().contains("false"))
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

                            AndroidUtils.showErrorLog(context, result.toString());

                            String amount = vpc_Amount.toString().replace("[", "");
                            amount = amount.replace("]", "");

                            String transaction_no = vpc_TransactionNo.toString().replace("[", "");
                            transaction_no = transaction_no.replace("]", "");

                            String receipt_no = vpc_ReceiptNo.toString().replace("[", "");
                            receipt_no = receipt_no.replace("]", "");


                            System.out.println("amount----------" + amount);

                            Intent intent = new Intent(context, PaymentCompletionActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            intent.putExtra("isSuccess", payment_status);
                            intent.putExtra("vpc_Amount", amount);
                            intent.putExtra("vpc_TransactionNo", transaction_no);
                            intent.putExtra("vpc_ReceiptNo", receipt_no);
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

    private boolean isExistInMap(String key, HashMap<String, String> stringStringHashMap) {
        for (Map.Entry m : stringStringHashMap.entrySet()) {
            if (m.getKey().toString().trim().toLowerCase().equals(key.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private String getValueFromMapByKey(String key, HashMap<String, String> stringStringHashMap) {
        if (isExistInMap(key, stringStringHashMap)) {
            for (Map.Entry m : stringStringHashMap.entrySet()) {
                if (m.getKey().toString().trim().toLowerCase().equals(key.toLowerCase())) {
                    AndroidUtils.showErrorLog(context, m.getKey().toString().trim() + " (if) " + m.getValue());
                    return m.getValue().toString();
                }
            }
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Transaction Cancel?")
                .setMessage("Are you sure you want to cancel this transaction?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }
}
