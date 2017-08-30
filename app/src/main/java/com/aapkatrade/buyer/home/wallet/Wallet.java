package com.aapkatrade.buyer.home.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.wallet.walletadapter.WalletAdapter;
import com.aapkatrade.buyer.home.wallet.walletadapter.WalletTransactionDatas;
import com.aapkatrade.buyer.payment.PaymentCompletionActivity;
import com.aapkatrade.buyer.uicomponent.calenderview.CalendarDay;
import com.aapkatrade.buyer.uicomponent.calenderview.CalendarMode;
import com.aapkatrade.buyer.uicomponent.calenderview.MaterialCalendarView;
import com.aapkatrade.buyer.uicomponent.calenderview.OnDateSelectedListener;
import com.aapkatrade.buyer.uicomponent.calenderview.decorator.HighlightWeekendsDecorator;
import com.aapkatrade.buyer.uicomponent.calenderview.decorator.MySelectorDecorator;
import com.aapkatrade.buyer.uicomponent.calenderview.decorator.OneDayDecorator;
import com.aapkatrade.buyer.wallet.AddWalletMoneyActivity;
import com.aapkatrade.buyer.wallet.WalletPaymentActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Calendar;


public class Wallet extends Fragment  implements OnDateSelectedListener
{

    RecyclerView recyclerView;
    Button buttonAddCredit;
    ImageView imgUser;
    TextView tv_currentbal_value,tvUserName;
    LinearLayoutManager wallet_history_layout_manager;
    MaterialCalendarView materialCalendarView;
    WalletAdapter  walletAdapter;
    ArrayList<WalletTransactionDatas> walletTransactionDataslist;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    ProgressBarHandler progressBarHandler;
    AppSharedPreference appSharedPreference;


    public Wallet()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v=inflater.inflate(R.layout.fragment_wallet, container, false);

        appSharedPreference = new AppSharedPreference(getActivity());

        progressBarHandler = new ProgressBarHandler(getActivity());



        initView(v);
        // Inflate the layout for this fragment
        return v;

    }

    private void initView(View v)
    {

        imgUser = (ImageView) v.findViewById(R.id.imgUser);

        tv_currentbal_value = (TextView) v.findViewById(R.id.tv_currentbal_value);

        tvUserName = (TextView) v.findViewById(R.id.tvUserName);

        walletTransactionDataslist=new ArrayList<>();

        setupcalender(v);

        setupRecycleview(v);



    }

    private void setupcalender(View v) {

        buttonAddCredit = (Button) v.findViewById(R.id.buttonAddCredit);

        buttonAddCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), AddWalletMoneyActivity.class);
                startActivity(i);

            }
        });

        materialCalendarView=(MaterialCalendarView)v.findViewById(R.id.calendarView);
        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);


        Calendar instance = Calendar.getInstance();
        materialCalendarView.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        materialCalendarView.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();

        materialCalendarView.addDecorators(
                new MySelectorDecorator(getActivity()),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

        materialCalendarView.state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();
    }

    private void setupRecycleview(View v)
    {

        recyclerView=(RecyclerView) v.findViewById(R.id.recycleview_wallet_history);
        wallet_history_layout_manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

      /*  walletTransactionDataslist.add(new WalletTransactionDatas("123","12345","Complete",getString(R.string.rupay_text)+"200","Cash",""));
        walletTransactionDataslist.add(new WalletTransactionDatas("123","12345","Complete",getString(R.string.rupay_text)+"200","Cash",""));
        walletTransactionDataslist.add(new WalletTransactionDatas("123","12345","Complete",getString(R.string.rupay_text)+"200","Cash",""));
        walletTransactionDataslist.add(new WalletTransactionDatas("123","12345","Complete",getString(R.string.rupay_text)+"200","Cash",""));*/
        walletAdapter=new WalletAdapter(getActivity(),walletTransactionDataslist);
        recyclerView.setLayoutManager(wallet_history_layout_manager);
        recyclerView.setAdapter(walletAdapter);


        callWebServiceMakePayment();

    }
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected)
    {
        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();
    }


    private void callWebServiceMakePayment()
    {
        progressBarHandler.show();

        String login_url = getActivity().getResources().getString(R.string.webservice_base_url) + "/wallet_history";

        AndroidUtils.showErrorLog(getActivity(),"Data nbn",appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), "")+appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), ""));

        Ion.with(getActivity())
                .load(login_url)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("user_id",appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_ID.toString(), ""))
                .setBodyParameter("user_type", appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_TYPE.toString(), ""))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        //  AndroidUtils.showErrorLog(context,result,"dghdfghsaf dawbnedvhaewnbedvsab dsadduyf");
                        // progressBarHandler.hide();
                        progressBarHandler.hide();

                         if (result.get("error").getAsString().contains("false"))
                        {
                            progressBarHandler.hide();
                            String payment_status;

                            JsonObject jsonObject = result.getAsJsonObject("result");

                            String total_balance = jsonObject.get("total_balance").getAsString();

                            tv_currentbal_value.setText(getString(R.string.rupay_text)+total_balance);

                            JsonArray jsonArray = jsonObject.getAsJsonArray("history");

                            for (int i= 0; i<jsonArray.size(); i++)
                            {
                                JsonObject jsonObject1 = (JsonObject) jsonArray.get(i);

                                String transactionid =jsonObject1.get("transactionId").getAsString();

                                String trans_amt = jsonObject1.get("trans_amt").getAsString();

                                String txn_type = jsonObject1.get("txn_type").getAsString();

                                String transaction_status = jsonObject1.get("transaction_status").getAsString();

                                String transaction_date = jsonObject1.get("created_at").getAsString();

                                walletTransactionDataslist.add(new WalletTransactionDatas(transactionid,transaction_date,transaction_status,getString(R.string.rupay_text)+trans_amt,txn_type,""));


                            }


                            AndroidUtils.showErrorLog(getActivity(), jsonObject.toString());

                                /* Intent intent = new Intent(getActivity(), PaymentCompletionActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("isSuccess", "true");
                            intent.putExtra("vpc_Amount", jsonObject.get("amount").getAsString());
                            intent.putExtra("vpc_TransactionNo", jsonObject.get("transactionID").getAsString());
                            intent.putExtra("vpc_ReceiptNo", "");
                            startActivity(intent);*/

                        }
                        else {
                            progressBarHandler.hide();
                            Intent intent = new Intent(getActivity(), PaymentCompletionActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("isSuccess", "false");
                            startActivity(intent);
                        }
                        //Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_SHORT).show();


                    }
                });
    }


}
