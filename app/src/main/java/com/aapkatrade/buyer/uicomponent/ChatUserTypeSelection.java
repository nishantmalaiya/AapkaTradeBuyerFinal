package com.aapkatrade.buyer.uicomponent;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
 * Created by PPC17 on 03-Jul-17.
 */

public class ChatUserTypeSelection extends RelativeLayout {

    private View view;
    private ProgressBarHandler progressBarHandler;
    TextView tv_usertype;
    private Context context;
    LinearLayout circleImageLinearLayout;
    ImageView usertypeimage;
    RadioButton radioButton;


    public ChatUserTypeSelection(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ChatUserTypeSelection(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ChatUserTypeSelection(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @SuppressLint("NewApi")
    public ChatUserTypeSelection(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    protected void init() {
        if (this.isInEditMode()) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_chat_tile, this, true);
        progressBarHandler = new ProgressBarHandler(context);
        initView();




    }



    private void initView() {

        tv_usertype = (TextView) findViewById(R.id.tv_usertype);
        usertypeimage = (ImageView) findViewById(R.id.circleImageView);
        radioButton = (RadioButton) findViewById(R.id.radio_usertypeselection);
        circleImageLinearLayout=(LinearLayout)findViewById(R.id.circleImageLinearLayout);

    }


}
