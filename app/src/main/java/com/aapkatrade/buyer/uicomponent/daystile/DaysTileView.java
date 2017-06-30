package com.aapkatrade.buyer.uicomponent.daystile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.adapter.CustomSpinnerAdapter;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.entity.KeyValue;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by PPC17 on 01-Apr-17.
 */

public class DaysTileView extends RelativeLayout {
    private View view;
    private ProgressBarHandler progressBarHandler;
    private RelativeLayout mainLayout, rl_layout1, rl_layout2, rl_layout3;
    private TextView tv_day_name;
    private Context context;
    private TextInputLayout input_layout_open_time, input_layout_close_time;
    private Spinner spOpenTime, spCloseTime;
    private KeyValue openTime, closeTime;
    private ArrayList<KeyValue> openTimingList = new ArrayList<>(), closeTimingList = new ArrayList<>();
    private ImageView imgOpenTimeSpinner, imgCloseTimeSpinner;

    public DaysTileView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DaysTileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public DaysTileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @SuppressLint("NewApi")
    public DaysTileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    protected void init() {
        if (this.isInEditMode()) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layoutId(), this, true);
        progressBarHandler = new ProgressBarHandler(context);
        initView();
        setOpenTimeSpinner();
        setCloseTimeSpinner();
    }

    private void initView() {
        mainLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);
        rl_layout1 = (RelativeLayout) view.findViewById(R.id.rl_layout1);
        rl_layout2 = (RelativeLayout) view.findViewById(R.id.rl_layout2);
        rl_layout3 = (RelativeLayout) view.findViewById(R.id.rl_layout3);
        tv_day_name = (TextView) view.findViewById(R.id.tv_day_name);
        input_layout_open_time = (TextInputLayout) view.findViewById(R.id.input_layout_open_time);
        input_layout_close_time = (TextInputLayout) view.findViewById(R.id.input_layout_close_time);
        spOpenTime = (Spinner) view.findViewById(R.id.spOpenTime);
        spCloseTime = (Spinner) view.findViewById(R.id.spCloseTime);
        imgOpenTimeSpinner = (ImageView) findViewById(R.id.imgOpenTimeSpinner);
        imgCloseTimeSpinner = (ImageView) findViewById(R.id.imgCloseTimeSpinner);
    }

    protected int layoutId() {
        return R.layout.layout_days_tile_view;
    }


    public void setBackgroundColor(@ColorRes int color) {
        if (this.rl_layout1 == null) {
            return;
        }
        AndroidUtils.setBackgroundSolid(rl_layout1, context, color, 0, GradientDrawable.OVAL);

        if (this.input_layout_open_time == null) {
            return;
        }
        AndroidUtils.setBackgroundSolid(input_layout_open_time, context, color, 0, GradientDrawable.RECTANGLE);

        if (this.input_layout_close_time == null) {
            return;
        }
        AndroidUtils.setBackgroundSolid(input_layout_close_time, context, color, 0, GradientDrawable.RECTANGLE);
    }


    public void setDayName(String title) {
        if (this.tv_day_name == null) {
            return;
        }
        this.tv_day_name.setText(title);
    }

    public void setDayName(@StringRes int title) {
        if (this.tv_day_name == null) {
            return;
        }
        this.tv_day_name.setText(title);
    }


    public void setOpenTimeSpinner() {
        if (this.spOpenTime == null) {
            return;
        }
        progressBarHandler.show();
        Ion.with(context)
                .load(context.getString(R.string.webservice_base_url) + "/dropdown")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "open_close")
                .setBodyParameter("timing", "0")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        if (result != null) {
                            JsonArray jsonResultArray = result.getAsJsonArray("result");
                            if (Validation.containsIgnoreCase(result.get("message").getAsString(), "Listed") && jsonResultArray != null && jsonResultArray.size() > 0) {
                                for (int i = 0; i < jsonResultArray.size(); i++) {
                                    JsonObject jsonObject = (JsonObject) jsonResultArray.get(i);
                                    openTimingList.add(new KeyValue(jsonObject.get("id").getAsString(), jsonObject.get("timing").getAsString()));
                                }
                                spOpenTime.setAdapter(new CustomSpinnerAdapter(context, openTimingList));
                                spOpenTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (position > 0) {
                                            openTime = openTimingList.get(position);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }
                    }

                });
    }

    public String getOpeningTime() {
        return openTime == null ? "" : openTime.value.toString();
    }


    public String getOpeningTimeID() {
        return openTime == null ? "" : openTime.key.toString();
    }

    public void setCloseTimeSpinner() {
        if (this.spCloseTime == null) {
            return;
        }
        progressBarHandler.show();
        Ion.with(context)
                .load(context.getString(R.string.webservice_base_url) + "/dropdown")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("type", "open_close")
                .setBodyParameter("timing", "1")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        if (result != null) {
                            JsonArray jsonResultArray = result.getAsJsonArray("result");
                            if (Validation.containsIgnoreCase(result.get("message").getAsString(), "Listed") && jsonResultArray != null && jsonResultArray.size() > 0) {
                                for (int i = 0; i < jsonResultArray.size(); i++) {
                                    JsonObject jsonObject = (JsonObject) jsonResultArray.get(i);
                                    closeTimingList.add(new KeyValue(jsonObject.get("id").getAsString(), jsonObject.get("timing").getAsString()));
                                }
                                spCloseTime.setAdapter(new CustomSpinnerAdapter(context, closeTimingList));
                                spCloseTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (position > 0) {
                                            closeTime = closeTimingList.get(position);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }
                    }

                });
    }

    public String getClosingTime() {
        return closeTime == null ? "" : closeTime.value.toString();
    }

    public String getClosingTimeID() {
        return closeTime == null ? "" : closeTime.key.toString();
    }

}