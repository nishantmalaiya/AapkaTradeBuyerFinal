package com.aapkatrade.buyer.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.home.cart.MyCartActivity;
import com.aapkatrade.buyer.shopdetail.ShopDetailActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
   public static ChatAdapter chatAdapter;
    Context context;
   public static ArrayList<ChatDatas> chatDatas = new ArrayList<>();
    ArrayList<ChatDatas> chatDatas2 = new ArrayList<>();
    ArrayList<ChatDatas> chatDatasN = new ArrayList<>();
    EditText etchatmessage;
    Button btnSend;
    String chatid, chat_ids, name, message, jsonarray_string, className;

    public static CommonInterface commonInterface;

    AppSharedPreference appSharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context = this;
        chatid = getIntent().getStringExtra("chatid");
        className = getIntent().getStringExtra("className");
        AndroidUtils.showErrorLog(context, "className", className);


        if (getIntent().getStringExtra("jsonarray_string") != null) {
            jsonarray_string = getIntent().getStringExtra("jsonarray_string");
            AndroidUtils.showErrorLog(context, "jsonarray_string", jsonarray_string);
            try {
                JSONArray list = new JSONArray(jsonarray_string.toString());





                for (int k = 0; k < list.length(); k++) {


                    JSONObject jsonObject = list.getJSONObject(k);

                    String message = jsonObject.get("msg").toString();
                    name = jsonObject.get("name_support").toString();
                    String user_id = jsonObject.get("user_id").toString();
                    chat_ids = jsonObject.get("chat_id").toString();

                    boolean you;

                    if (user_id.contains("1")) {

                        you = true;


                    } else {

                        you = false;

                    }
                    long time = Long.parseLong(jsonObject.get("time").toString());

                    chatDatas.add(new ChatDatas(message, name, time, you));
                    chatAdapter = new ChatAdapter(context, chatDatas);
                    chatAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

if(getIntent().getStringExtra("name")!=null ||getIntent().getStringExtra("message")!=null)
{
    name = getIntent().getStringExtra("name");
    message = getIntent().getStringExtra("message");
}



        setupToolBar();

        initView();

        setupRecyclerView();
        if (className.contains("HomeActivity")) {

            initChat();

        }

        clickEvents();


        commonInterface = new CommonInterface() {
            @Override
            public Object getData(Object object) {


                chatDatas2 = (ArrayList<ChatDatas>) object;

                chatDatas.add(new ChatDatas(chatDatas2.get(0).message, chatDatas2.get(0).name, chatDatas2.get(0).timestamp, false));
                chatAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(chatDatas.size());
                AndroidUtils.showErrorLog(context, "***************", chatDatas.size() + chatDatas.toString());


                return null;
            }
        };

    }


    private void initChat() {

        ChatDatas myData = new ChatDatas(message, name, Long.parseLong("" + System.currentTimeMillis()), false);
        chatDatas.add(myData);
        add_message(chatDatas);
    }

    private void clickEvents() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation.isNonEmptyStr(etchatmessage.getText().toString())) {

                    String fcm_token = appSharedPreference.getSharedPref(SharedPreferenceConstants.FIREBASE_REG_ID.toString());


                    Callwebservicechat(etchatmessage.getText().toString());
                    etchatmessage.setText("");


                }
            }
        });
    }

    private void Callwebservicechat(final String s) {

        String callwebservicechat = context.getResources().getString(R.string.webservice_base_url) + "/chating";


        Ion.with(context)
                .load(callwebservicechat)
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")

                .setBodyParameter("message", s)

                .setBodyParameter("chat_id", chatid)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        if (result != null) {

                            AndroidUtils.showErrorLog(ChatActivity.this, "Response Chat Activity", result.toString());

                            chatDatas.add(new ChatDatas(s, name, Long.parseLong("" + System.currentTimeMillis()), false));
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

        AppCompatImageView homeIcon = (AppCompatImageView) findViewById(R.id.logoWord);
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


    public void add_message(ArrayList<ChatDatas> chatDatas2)


    {
        if (chatAdapter != null) {

            chatAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(chatDatas2.size());
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu, menu);

       




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }



}
