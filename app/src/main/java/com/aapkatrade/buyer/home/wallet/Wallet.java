package com.aapkatrade.buyer.home.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Tabletsize;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.wallet.walletadapter.WalletAdapter;
import com.aapkatrade.buyer.home.wallet.walletadapter.WalletTransactionDatas;
import com.aapkatrade.buyer.payment.PaymentCompletionActivity;
import com.aapkatrade.buyer.uicomponent.calenderview.CalendarDay;
import com.aapkatrade.buyer.uicomponent.calenderview.CalendarMode;
import com.aapkatrade.buyer.uicomponent.calenderview.MaterialCalendarView;
import com.aapkatrade.buyer.uicomponent.calenderview.OnDateSelectedListener;
import com.aapkatrade.buyer.uicomponent.calenderview.OnRangeSelectedListener;
import com.aapkatrade.buyer.uicomponent.calenderview.decorator.HighlightWeekendsDecorator;
import com.aapkatrade.buyer.uicomponent.calenderview.decorator.MySelectorDecorator;
import com.aapkatrade.buyer.uicomponent.calenderview.decorator.OneDayDecorator;
import com.aapkatrade.buyer.wallet.AddWalletMoneyActivity;
import com.aapkatrade.buyer.wallet.WalletPaymentActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Wallet extends Fragment
{

    LinearLayout linearLayoutUser,linearLayourWallet;
    RecyclerView recyclerView;
    Button buttonAddCredit;
    ImageView imgUser;
    TextView tv_currentbal_value,tvUserName;
    GridLayoutManager wallet_history_layout_manager;
    public MaterialCalendarView materialCalendarView;
    WalletAdapter  walletAdapter;
    ArrayList<WalletTransactionDatas> walletTransactionDataslist;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    ProgressBarHandler progressBarHandler;
    AppSharedPreference appSharedPreference;
    CardView cardViewBalance;



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


       /* materialCalendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                AndroidUtils.showToast(getActivity(),"  hi  "+ dates.toString());
            }
        });

        materialCalendarView.commonInterface = new CommonInterface() {
            @Override
            public Object getData(Object object) {
                if((Boolean) object){
                  // AndroidUtils.showToast(getActivity(),"  hi  "+ String.valueOf(materialCalendarView.getLastDayOfWeek()));
                }
                return null;
            }
        };*/
        // Inflate the layout for this fragment



        return v;

    }

    private void initView(View v)
    {
        linearLayourWallet = (LinearLayout) v.findViewById(R.id.linearLayourWallet);

        linearLayoutUser = (LinearLayout) v.findViewById(R.id.linearLayoutUser);

        cardViewBalance = (CardView) v.findViewById(R.id.cardViewBalance);

        imgUser = (ImageView) v.findViewById(R.id.imageviewpp);

        if (Validation.isNonEmptyStr(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), "")))
        {
            Picasso.with(getActivity()).load(appSharedPreference.getSharedPref(SharedPreferenceConstants.PROFILE_PIC.toString(), ""))
                    .error(R.drawable.ic_profile_user)
                    .into(imgUser);
        }

        tv_currentbal_value = (TextView) v.findViewById(R.id.tv_currentbal_value);

        tvUserName = (TextView) v.findViewById(R.id.tvUserName);

        tvUserName.setText(appSharedPreference.getSharedPref(SharedPreferenceConstants.USER_NAME.toString(), ""));

        walletTransactionDataslist=new ArrayList<>();

        setupcalender(v);

        setupRecycleview(v);


    }

    private void setupcalender(View v)
    {

        buttonAddCredit = (Button) v.findViewById(R.id.buttonAddCredit);

        buttonAddCredit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), AddWalletMoneyActivity.class);
                i.putExtra("Amount",tv_currentbal_value.getText().toString());
                startActivity(i);

            }
        });

      /*  materialCalendarView=(MaterialCalendarView)v.findViewById(R.id.calendarView);
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





        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                AndroidUtils.showToast(getActivity(),"Hi Dear"+date.toString());

            }
        });
*/

    }

    private void setupRecycleview(View v)
    {

        recyclerView=(RecyclerView) v.findViewById(R.id.recycleview_wallet_history);

        if (Tabletsize.isTablet(getActivity())) {
            wallet_history_layout_manager = new GridLayoutManager(getActivity(), 3);
        } else {

            wallet_history_layout_manager = new GridLayoutManager(getActivity(), 2);
        }
        //wallet_history_layout_manager = new GridLayoutManager(getActivity(), 2);

      /*  walletTransactionDataslist.add(new WalletTransactionDatas("123","12345","Complete",getString(R.string.rupay_text)+"200","Cash",""));
        walletTransactionDataslist.add(new WalletTransactionDatas("123","12345","Complete",getString(R.string.rupay_text)+"200","Cash",""));
        walletTransactionDataslist.add(new WalletTransactionDatas("123","12345","Complete",getString(R.string.rupay_text)+"200","Cash",""));
        walletTransactionDataslist.add(new WalletTransactionDatas("123","12345","Complete",getString(R.string.rupay_text)+"200","Cash",""));*/
        walletAdapter=new WalletAdapter(getActivity(),walletTransactionDataslist);
        recyclerView.setLayoutManager(wallet_history_layout_manager);
        recyclerView.setAdapter(walletAdapter);


        callWebServicewallet_history();

    }


  /*  @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected)
    {
        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();
    }*/


    private void callWebServicewallet_history()
    {
        linearLayourWallet.setVisibility(View.INVISIBLE);
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
                         AndroidUtils.showErrorLog(getActivity(),"wallet history",result);
                        // progressBarHandler.hide();
                        progressBarHandler.hide();

                        if (result!=null)
                        {
                            if (result.get("error").getAsString().contains("false"))
                            {
                                linearLayourWallet.setVisibility(View.VISIBLE);
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

                                    String transaction_message = jsonObject1.get("message").getAsString();

                                    walletTransactionDataslist.add(new WalletTransactionDatas(transactionid,transaction_date,transaction_status,trans_amt,txn_type,transaction_message));


                                }

                                walletAdapter.notifyDataSetChanged();
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
                                linearLayourWallet.setVisibility(View.INVISIBLE);
                                progressBarHandler.hide();
                                Intent intent = new Intent(getActivity(), PaymentCompletionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("isSuccess", "false");
                                startActivity(intent);
                            }

                        }
                        else {

                            progressBarHandler.hide();
                            linearLayourWallet.setVisibility(View.INVISIBLE);
                            AndroidUtils.showErrorLog(getActivity(),"Server Error Please Try Again");

                        }

                        //Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_SHORT).show();


                    }
                });
    }


}
