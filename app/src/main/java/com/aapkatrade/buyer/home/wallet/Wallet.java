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
import com.aapkatrade.buyer.home.wallet.walletadapter.WalletAdapter;
import com.aapkatrade.buyer.home.wallet.walletadapter.WalletTransactionDatas;
import com.aapkatrade.buyer.uicomponent.calenderview.CalendarDay;
import com.aapkatrade.buyer.uicomponent.calenderview.CalendarMode;
import com.aapkatrade.buyer.uicomponent.calenderview.MaterialCalendarView;
import com.aapkatrade.buyer.uicomponent.calenderview.OnDateSelectedListener;
import com.aapkatrade.buyer.uicomponent.calenderview.decorator.HighlightWeekendsDecorator;
import com.aapkatrade.buyer.uicomponent.calenderview.decorator.MySelectorDecorator;
import com.aapkatrade.buyer.uicomponent.calenderview.decorator.OneDayDecorator;
import com.aapkatrade.buyer.wallet.AddWalletMoneyActivity;

import java.util.ArrayList;
import java.util.Calendar;


public class Wallet extends Fragment  implements OnDateSelectedListener {

    RecyclerView recyclerView;
    Button buttonAddCredit;
    ImageView imgUser;
    TextView tv_currentbal_value;
    LinearLayoutManager wallet_history_layout_manager;
    MaterialCalendarView materialCalendarView;
    WalletAdapter  walletAdapter;
    ArrayList<WalletTransactionDatas> walletTransactionDataslist;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    public Wallet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v=inflater.inflate(R.layout.fragment_wallet, container, false);

        initView(v);
        // Inflate the layout for this fragment
        return v;


    }

    private void initView(View v)
    {

        imgUser = (ImageView) v.findViewById(R.id.imgUser);

        tv_currentbal_value = (TextView) v.findViewById(R.id.tv_currentbal_value);

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

    private void setupRecycleview(View v) {

        recyclerView=(RecyclerView) v.findViewById(R.id.recycleview_wallet_history);
        wallet_history_layout_manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        walletTransactionDataslist.add(new WalletTransactionDatas("123","12345","Complete",getString(R.string.rupay_text)+"200","Cash",""));
        walletTransactionDataslist.add(new WalletTransactionDatas("123","12345","Complete",getString(R.string.rupay_text)+"200","Cash",""));
        walletTransactionDataslist.add(new WalletTransactionDatas("123","12345","Complete",getString(R.string.rupay_text)+"200","Cash",""));
        walletTransactionDataslist.add(new WalletTransactionDatas("123","12345","Complete",getString(R.string.rupay_text)+"200","Cash",""));
        walletAdapter=new WalletAdapter(getActivity(),walletTransactionDataslist);
        recyclerView.setLayoutManager(wallet_history_layout_manager);
        recyclerView.setAdapter(walletAdapter);



    }
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();
    }


}
