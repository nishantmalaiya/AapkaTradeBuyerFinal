package com.aapkatrade.buyer.uicomponent.customspinner;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.ConnetivityCheck;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.buyerregistration.BuyerRegistrationActivity;
import com.aapkatrade.buyer.home.buyerregistration.entity.BuyerRegistration;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by PPC17 on 23-Aug-17.
 */

public class SearchableSpinner extends RelativeLayout {
    private Context context;
    private AppSharedPreference appSharedpreference;
    private View view;
    private ProgressBarHandler progressBarHandler;
    private TextView tvData;
    private RelativeLayout relativeLayoutRoot;
    private SearchableListDialog searchableListDialog;
    private CommonInterface commonInterface;
    LayoutInflater inflater;
    ArrayList<SpinnerDatas> countryArrayList = new ArrayList<>();
    ArrayList<SpinnerDatas> stateArrayList = new ArrayList<>();
    ArrayList<SpinnerDatas> cityArrayList = new ArrayList<>();
    private String selectedCountryId, selectedStateId, SelectedCityId;
    String type;
    AttributeSet attr;

    public static enum Constants {
        COUNTRY("country"),
        STATE("state"),
        CITY("city");

        private final String text;

        Constants(final String text) {
            this.text = text;
        }

        public String toString() {
            return this.text;
        }
    }


    public SearchableSpinner(Context context) {
        super(context);
        this.context = context;
        init(context, null, 0);

    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context);
        this.context = context;


        init(context, attrs, 0);
    }
    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs, 0);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        this.attr = attrs;
        init(context, attrs, defStyleAttr);

    }





    public void init(final Context context, AttributeSet attrs, int defStyle) {


        appSharedpreference = new AppSharedPreference(context);

        if (this.isInEditMode()) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);

        type = a.getString(R.styleable.SearchableSpinner_type);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layoutId(), this, true);
        progressBarHandler = new ProgressBarHandler(context);
        tvData = view.findViewById(R.id.tvData);
        relativeLayoutRoot = view.findViewById(R.id.relativeLayoutRoot);
        tvData.setText("Select " + type);

        commonInterface = new CommonInterface() {
            @Override
            public Object getData(Object object) {


                if (object != null) {

                    if (type.equals(Constants.COUNTRY.toString())) {
                        setText(countryArrayList.get(getIndex_Country(((String) object))).Name);
                        stateArrayList.clear();
                        selectedCountryId = countryArrayList.get(getIndex_Country(((String) object))).Id;
                        //  BuyerRegistrationActivity.formBuyerData.setCountryId(selectedCountryId);
                        BuyerRegistrationActivity.commonInterface.getData(new Idtype(selectedCountryId, "country"));

                        //hitStateWebService(true);
                    } else if (type.equals(Constants.STATE.toString())) {
                        setText(stateArrayList.get(getIndex_State(((String) object))).Name);

                        selectedStateId = stateArrayList.get(getIndex_State(((String) object))).Id;
                        // gd.getid(Integer.parseInt(selectedStateId), Constants.STATE.toString());
                    } else if (type.equals(Constants.CITY.toString())) {

                        setText(countryArrayList.get(getIndex_Country(((String) object))).Name);

                        SelectedCityId = countryArrayList.get(getIndex_Country(((String) object))).Id;
                        //gd.getid(Integer.parseInt(SelectedCityId), Constants.CITY.toString());


                    }


                }


                return null;
            }
        };

        hitCountryWebService(false);

        relativeLayoutRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.showErrorLog(context, "selected type" + type);
                if (ConnetivityCheck.isNetworkAvailable(context)) {

                    if (type.equals(Constants.COUNTRY.toString())) {

                        if (countryArrayList.size() == 0) {
                            hitCountryWebService(true);
                        } else {
                            searchableListDialog = new SearchableListDialog(context, countryArrayList, commonInterface);
                            FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                            searchableListDialog.show(fm, "SearchableListDialog");
                        }
                    } else if (type.equals(Constants.STATE.toString())) {
                        stateArrayList.clear();

                        if (stateArrayList.size() == 0) {
                            hitStateWebService(true);
                        } else {
                            searchableListDialog = new SearchableListDialog(context, stateArrayList, commonInterface);
                            FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                            searchableListDialog.show(fm, "SearchableListDialog");
                        }

                    } else if (type.equals(Constants.CITY.toString())) {
                        cityArrayList.clear();

                        if (cityArrayList.size() == 0) {
                            hitCityWebService(true);
                        } else {
                            searchableListDialog = new SearchableListDialog(context, cityArrayList, commonInterface);
                            FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                            searchableListDialog.show(fm, "SearchableListDialog");
                        }

                    }


                } else {
                    AndroidUtils.showToast(context, "!Internet not available. Check your internet connection.");
                }

            }
        });


    }

    public void hitCityWebService(final boolean val) {
        String stateid = BuyerRegistrationActivity.formBuyerData.getStateId();
        if (stateid != null) {
            progressBarHandler.show();
            Ion.with(context)
                    .load(getResources().getString(R.string.webservice_base_url).concat("/dropdown"))
                    .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                    .setBodyParameter("type", "city")
                    .setBodyParameter("id", stateid)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressBarHandler.hide();
                            AndroidUtils.showErrorLog(context, "Country Data loading : ", result);

                            if (result != null) {
                                if (result.get("error").getAsString().equals("false")) {
                                    JsonArray jsonArray = result.getAsJsonArray("result");
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        cityArrayList.add(new SpinnerDatas(jsonArray.get(i).getAsJsonObject().get("id").getAsString(), jsonArray.get(i).getAsJsonObject().get("name").getAsString()));
                                    }

                                    if (val) {
                                        searchableListDialog = new SearchableListDialog(context, cityArrayList, commonInterface);
                                        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                                        searchableListDialog.show(fm, "SearchableListDialog");
                                    }

                                }
                            }
                        }
                    });
        } else {


        }

    }

    public void hitStateWebService(final boolean val) {


        try {
            String countryid = BuyerRegistrationActivity.formBuyerData.getCountryId();
            AndroidUtils.showErrorLog(context, "countryid" + countryid);


            if (countryid != null)

            {

                progressBarHandler.show();
                Ion.with(context)
                        .load(getResources().getString(R.string.webservice_base_url).concat("/dropdown"))
                        .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                        .setBodyParameter("type", "state")
                        .setBodyParameter("id", countryid)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                progressBarHandler.hide();
                                AndroidUtils.showErrorLog(context, "Country Data loading : ", result);

                                if (result != null) {
                                    if (result.get("error").getAsString().equals("false")) {
                                        JsonArray jsonArray = result.getAsJsonArray("result");
                                        for (int i = 0; i < jsonArray.size(); i++) {
                                            stateArrayList.add(new SpinnerDatas(jsonArray.get(i).getAsJsonObject().get("id").getAsString(), jsonArray.get(i).getAsJsonObject().get("name").getAsString()));
                                        }

                                        if (val) {
                                            searchableListDialog = new SearchableListDialog(context, stateArrayList, commonInterface);
                                            FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                                            searchableListDialog.show(fm, "SearchableListDialog");
                                        }

                                    } else {
                                        AndroidUtils.showErrorLog(context, "not null", result.toString());
                                    }
                                }
                            }
                        });
            } else {
                AndroidUtils.showErrorLog(context, "country_id null");
            }
        } catch (Exception e) {
            AndroidUtils.showErrorLog(context, "errror", e.toString());
        }
    }

    private int getIndex_Country(String id) {

        for (int i = 0; i < countryArrayList.size(); i++) {
            if (countryArrayList.get(i).Id.equals(id)) {
                return i;
            }
        }
        return 0;
    }

    private int getIndex_State(String id) {

        for (int i = 0; i < stateArrayList.size(); i++) {
            if (stateArrayList.get(i).Id.equals(id)) {
                return i;
            }
        }
        return 0;
    }


    public String getCountryId() {

        if (selectedCountryId != null) {
            return selectedCountryId;
        } else {
            return "-1";
        }

    }

    public void setText(String data) {
        if (tvData != null) {
            tvData.setText(data);
        }
    }

    public void hitCountryWebService(final boolean val) {
        progressBarHandler.show();
        Ion.with(context)
                .load(getResources().getString(R.string.webservice_base_url).concat("/dropdown"))
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "country")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        AndroidUtils.showErrorLog(context, "Country Data loading : ", result);

                        if (result != null) {
                            if (result.get("error").getAsString().equals("false")) {
                                JsonArray jsonArray = result.getAsJsonArray("result");
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    countryArrayList.add(new SpinnerDatas(jsonArray.get(i).getAsJsonObject().get("id").getAsString(), jsonArray.get(i).getAsJsonObject().get("name").getAsString()));
                                }

                                if (val) {
                                    searchableListDialog = new SearchableListDialog(context, countryArrayList, commonInterface);
                                    FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                                    searchableListDialog.show(fm, "SearchableListDialog");
                                }

                            }
                        }
                    }
                });


    }


    private int layoutId() {
        return R.layout.searcheable_spinner_container;
    }


}
