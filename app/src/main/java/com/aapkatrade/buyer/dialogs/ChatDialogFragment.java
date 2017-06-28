package com.aapkatrade.buyer.dialogs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressDialogHandler;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

@SuppressLint("ValidFragment")
public class ChatDialogFragment extends DialogFragment {


    private Context context;


    ProgressDialogHandler progressDialogHandler;
    View v;
    ViewGroup viewgrp;

    RelativeLayout layoutcontainer;

    public ChatDialogFragment(Context context) {
        super();

        this.context = context;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        viewgrp = container;
        v = inflater.inflate(R.layout.fragment_chat_dialog, container, false);
        //noinspection ConstantConditions
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        initView(v);
        AndroidUtils.setGradientColor(layoutcontainer, android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.datanotfound_gradient_bottom), ContextCompat.getColor(context, R.color.datanotfound_gradient_top), android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, 0);
        progressDialogHandler = new ProgressDialogHandler(getActivity());

        return v;
    }

    private void initView(View v) {


        layoutcontainer = (RelativeLayout) v.findViewById(R.id.chat_container);

    }


}
