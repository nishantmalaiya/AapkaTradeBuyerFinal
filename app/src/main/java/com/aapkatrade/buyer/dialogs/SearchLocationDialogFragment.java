package com.aapkatrade.buyer.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.chat.ChatActivity;
import com.aapkatrade.buyer.chat.ChatValidationDatas;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.general.progressbar.ProgressDialogHandler;
import com.aapkatrade.buyer.search.Search;
import com.aapkatrade.buyer.uicomponent.ChatUserTypeSelection;
import com.aapkatrade.buyer.uicomponent.customspinner.CountryStateSelectSpinner;
import com.aapkatrade.buyer.uicomponent.customspinner.SearchableListDialog;
import com.aapkatrade.buyer.uicomponent.customspinner.SpinnerDatas;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Arrays;

import static com.aapkatrade.buyer.R.id.etyourquestion;

@SuppressLint("ValidFragment")
public class SearchLocationDialogFragment extends DialogFragment {


    private Context context;


    ProgressDialogHandler progressDialogHandler;
    View v;
    ViewGroup viewgrp;


    LinearLayout layoutcontainer;
    ImageView img_close_dialog;


    ProgressBarHandler progressBarHandler;

    ImageButton btn_continue, btn_back_To_Home;
    AppSharedPreference appSharedPreference;
    Spinner state_list_spinner;
    boolean firsttime;
    private ArrayList<String> countryNameArrayList = new ArrayList<>();

    private ArrayList<String> countryIds = new ArrayList<>();

    public SearchLocationDialogFragment(Context context, boolean firsttime) {
        super();

        this.context = context;
        this.firsttime = firsttime;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        viewgrp = container;
        v = inflater.inflate(R.layout.fragment_location_dialog, container, false);
        //noinspection ConstantConditions
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        progressDialogHandler = new ProgressDialogHandler(getActivity());
        initView(v);
        AndroidUtils.setGradientColor(layoutcontainer, android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.datanotfound_gradient_bottom), ContextCompat.getColor(context, R.color.datanotfound_gradient_top), android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, 0);


        setup_state_spinner(v);
        return v;
    }

    private void initView(View v) {
        btn_continue = v.findViewById(R.id.btn_continue);
        btn_back_To_Home = v.findViewById(R.id.btn_back_To_Home);

        appSharedPreference = new AppSharedPreference(getActivity());
        img_close_dialog = (ImageView) v.findViewById(R.id.img_close_dialog);

        layoutcontainer = (LinearLayout) v.findViewById(R.id.state_location_container);

        progressBarHandler = new ProgressBarHandler(getActivity());
        callEvents();


    }


    private void callEvents() {


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dismiss();
                if (state_list_spinner != null)
                    Search.commonInterface.getData(state_list_spinner.getSelectedItem().toString());


            }
        });

        img_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!firsttime) {
                    getDialog().dismiss();
                }

            }
        });
        btn_back_To_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

                ((Activity) context).finish();

            }
        });

    }


    private void setup_state_spinner(View v) {
        state_list_spinner = (Spinner) v.findViewById(R.id.spCountryList);


        call_Country_webservice();

        state_list_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // stateList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.state_list)));

/*        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), R.layout.white_textcolor_spinner, countryNameArrayList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.white_textcolor_spinner);

        state_list_spinner.setAdapter(spinnerArrayAdapter);*/


    }

    private void call_Country_webservice() {
        progressDialogHandler.show();


        Ion.with(context)
                .load(getResources().getString(R.string.webservice_base_url).concat("/dropdown"))
                .setHeader("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "country")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressDialogHandler.hide();

                        AndroidUtils.showErrorLog(context, "Country Data loading : ", result);

                        if (result != null) {
                            if (result.get("error").getAsString().equals("false")) {
                                JsonArray jsonArray = result.getAsJsonArray("result");
                                for (int i = 0; i < jsonArray.size(); i++) {

                                    countryNameArrayList.add(jsonArray.get(i).getAsJsonObject().get("name").getAsString());
                                    countryIds.add(jsonArray.get(i).getAsJsonObject().get("id").getAsString());
                                }

                                //CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter()
                                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), R.layout.white_textcolor_spinner, countryNameArrayList);
                                spinnerArrayAdapter.setDropDownViewResource(R.layout.white_textcolor_spinner);

                                state_list_spinner.setAdapter(spinnerArrayAdapter);


                            }
                        }
                    }
                });


    }


}
