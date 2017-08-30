package com.aapkatrade.buyer.uicomponent.customspinner;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.os.Build;
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
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.home.buyerregistration.BuyerRegistrationActivity;
import com.aapkatrade.buyer.home.buyerregistration.entity.BuyerRegistration;
import com.aapkatrade.buyer.seller.registration.SellerRegistrationActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addcompanyshop.AddCompanyShopActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.editcompanyshop.EditCompanyShopActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by PPC17 on 29-Aug-17.
 */

public class CountryStateSelectSpinner extends RelativeLayout {
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

    public CountryStateSelectSpinner(Context context) {
        super(context);
        this.context = context;
        init(context, null, 0);
    }

    public CountryStateSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs, 0);
    }

    public CountryStateSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CountryStateSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
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
        tvData.setText(new StringBuilder("Select ").append(String.valueOf(type.charAt(0)).toUpperCase()).append(type.substring(1)));

        commonInterface = new CommonInterface() {
            @Override
            public Object getData(Object object) {


                if (object != null) {

                    if (type.equals(SearchableSpinner.Constants.COUNTRY.toString())) {
                        setText(countryArrayList.get(getIndex_Country(((String) object))).Name);
                        stateArrayList.clear();
                        selectedCountryId = countryArrayList.get(getIndex_Country(((String) object))).Id;
                        //  BuyerRegistrationActivity.formBuyerData.setCountryId(selectedCountryId);
                        if (context instanceof BuyerRegistrationActivity) {
                            BuyerRegistrationActivity.commonInterface.getData(new Idtype(selectedCountryId, "country"));
                        } else if (context instanceof SellerRegistrationActivity) {


                            SellerRegistrationActivity.commonInterface.getData(new Idtype(selectedCountryId, "country"));

                        } else if (context instanceof AddCompanyShopActivity) {


                            AddCompanyShopActivity.commonInterface.getData(new Idtype(selectedCountryId, "country"));

                        }

                        else if (context instanceof EditCompanyShopActivity) {


                            EditCompanyShopActivity.commonInterface.getData(new Idtype(selectedCountryId, "country"));

                        }
                        //hitStateWebService(true);
                    } else if (type.equals(SearchableSpinner.Constants.STATE.toString())) {
                        cityArrayList.clear();
                        setText(stateArrayList.get(getIndex_State(((String) object))).Name);

                        selectedStateId = stateArrayList.get(getIndex_State(((String) object))).Id;
                        if (context instanceof BuyerRegistrationActivity) {

                            BuyerRegistrationActivity.commonInterface.getData(new Idtype(selectedStateId, "state"));
                        } else if (context instanceof SellerRegistrationActivity) {


                            SellerRegistrationActivity.commonInterface.getData(new Idtype(selectedStateId, "state"));

                        } else if (context instanceof AddCompanyShopActivity) {


                            AddCompanyShopActivity.commonInterface.getData(new Idtype(selectedStateId, "state"));

                        }
                        else if (context instanceof EditCompanyShopActivity) {


                            EditCompanyShopActivity.commonInterface.getData(new Idtype(selectedStateId, "state"));

                        }


                        // gd.getid(Integer.parseInt(selectedStateId), Constants.STATE.toString());
                    } else if (type.equals(SearchableSpinner.Constants.CITY.toString())) {

                        setText(cityArrayList.get(getIndex_City(((String) object))).Name);

                        SelectedCityId = cityArrayList.get(getIndex_City(((String) object))).Id;
                        if (context instanceof BuyerRegistrationActivity) {
                            BuyerRegistrationActivity.commonInterface.getData(new Idtype(SelectedCityId, "city"));
                        } else if (context instanceof SellerRegistrationActivity) {

                            SellerRegistrationActivity.commonInterface.getData(new Idtype(SelectedCityId, "city"));


                        } else if (context instanceof AddCompanyShopActivity) {


                            AddCompanyShopActivity.commonInterface.getData(new Idtype(SelectedCityId, "city"));

                        }
                        else if (context instanceof EditCompanyShopActivity) {


                            EditCompanyShopActivity.commonInterface.getData(new Idtype(SelectedCityId, "city"));

                        }


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

                    if (type.equals(SearchableSpinner.Constants.COUNTRY.toString())) {

                        if (countryArrayList.size() == 0) {
                            hitCountryWebService(true);
                        } else {
                            searchableListDialog = new SearchableListDialog(context, countryArrayList, commonInterface);


                            FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();

                            searchableListDialog.show(fm, "SearchableListDialog");
                        }
                    } else if (type.equals(SearchableSpinner.Constants.STATE.toString())) {
                        stateArrayList.clear();

                        if (stateArrayList.size() == 0) {
                            hitStateWebService(true);
                        } else {
                            searchableListDialog = new SearchableListDialog(context, stateArrayList, commonInterface);
                            FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                            searchableListDialog.show(fm, "SearchableListDialog");
                        }

                    } else if (type.equals(SearchableSpinner.Constants.CITY.toString())) {
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
        String stateid;

        if (context instanceof BuyerRegistrationActivity) {
            stateid = BuyerRegistrationActivity.formBuyerData.getStateId();
            AndroidUtils.showErrorLog(context, "state id", stateid);
        }

        else if(context instanceof AddCompanyShopActivity)
        {

            stateid=AddCompanyShopActivity.stateID;

        }

        else if(context instanceof EditCompanyShopActivity)
        {

            stateid=EditCompanyShopActivity.stateID;

        }
        else {
            stateid = SellerRegistrationActivity.formSellerData.getStateId();
        }


        if (Validation.isNonEmptyStr(stateid)) {
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


                                    if (!result.get("message").getAsString().equals("No record found")) {
                                        JsonArray jsonArray = result.getAsJsonArray("result");
                                        for (int i = 0; i < jsonArray.size(); i++) {
                                            cityArrayList.add(new SpinnerDatas(jsonArray.get(i).getAsJsonObject().get("id").getAsString(), jsonArray.get(i).getAsJsonObject().get("name").getAsString()));
                                        }

                                        if (val) {
                                            searchableListDialog = new SearchableListDialog(context, cityArrayList, commonInterface);
                                            FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                                            searchableListDialog.show(fm, "SearchableListDialog");
                                        }
                                    } else {

                                        cityArrayList.clear();

                                        AndroidUtils.showToast(context, "No City found for this state");
                                    }

                                }
                            }
                        }
                    });
        } else {

            AndroidUtils.showErrorLog(context, "state id null");

            AndroidUtils.showToast(context, "Please Select Country/State First");
        }

    }

    public void hitStateWebService(final boolean val) {


        try {
            String countryid;
            if (context instanceof BuyerRegistrationActivity) {
                countryid = BuyerRegistrationActivity.formBuyerData.getCountryId();
            }

            else if(context instanceof AddCompanyShopActivity)
            {

                countryid=AddCompanyShopActivity.countryID;

            }
            else if(context instanceof EditCompanyShopActivity)
            {

                countryid=EditCompanyShopActivity.countryID;

            }

            else {
                countryid = SellerRegistrationActivity.formSellerData.getCountryId();
            }
            AndroidUtils.showErrorLog(context, "countryid" + countryid);


            if (Validation.isNonEmptyStr(countryid))

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


                AndroidUtils.showToast(context, "please Select Country First");
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


    private int getIndex_City(String id) {

        for (int i = 0; i < cityArrayList.size(); i++) {
            if (cityArrayList.get(i).Id.equals(id)) {
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
