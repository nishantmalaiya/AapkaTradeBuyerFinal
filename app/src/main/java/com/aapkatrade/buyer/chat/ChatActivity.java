package com.aapkatrade.buyer.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.home.HomeActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    Context context;
    ArrayList<ChatDatas> chatDatas = new ArrayList<>();
    EditText etchatmessage;
    Button btnSend;
    String chatid, emailphone, token, name, message;

    AppSharedPreference appSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatid = getIntent().getStringExtra("chat_id");


        context = this;
        setupToolBar();
        initView();

        setupRecyclerView();


        clickEvents();
    }

    private void clickEvents() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation.isNonEmptyStr(etchatmessage.getText().toString())) {

                    String fcm_token = appSharedPreference.getSharedPref(SharedPreferenceConstants.FIREBASE_REG_ID.toString());


                    Callwebservicechat();


                }
            }
        });
    }

    private void Callwebservicechat() {

        String callwebservicechat = context.getResources().getString(R.string.webservice_base_url) + "/chating";


        Ion.with(context)
                .load(callwebservicechat)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("emailphone", emailphone)
                .setBodyParameter("name", name)
                .setBodyParameter("message", message)
                .setBodyParameter("token", token)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        if (result != null) {
                            ChatDatas myData = new ChatDatas("demo", "demo", Long.parseLong("" + System.currentTimeMillis()), true, token, "demo@gmail.com");
                            chatDatas.add(myData);
                            add_message(chatDatas);


                        } else {

                        }

                    }
                });


    }

    private void initView() {
        appSharedPreference = new AppSharedPreference(context);
        etchatmessage = (EditText) findViewById(R.id.et_message);
        btnSend = (Button) findViewById(R.id.buttonSend);

    }


    private void setupToolBar() {

        ImageView homeIcon = (ImageView) findViewById(R.id.iconHome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AndroidUtils.setImageColor(homeIcon, context, R.color.white);
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setElevation(0);
        }

    }


    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        chatAdapter = new ChatAdapter(context, chatDatas);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);
    }


    public void add_message(ArrayList<ChatDatas> chatDatas)


    {


        chatAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(this.chatDatas.size());


    }


}
