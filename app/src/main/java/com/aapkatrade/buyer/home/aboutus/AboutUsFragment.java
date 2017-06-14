package com.aapkatrade.buyer.home.aboutus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.uicomponent.ExpandableTextView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class AboutUsFragment extends Fragment {
    private Context context;

    private ExpandableTextView tvTermsAndConditions;

    private TextView tvReadMore;
    private RelativeLayout expandableRelativeLayout;
    private LinearLayout policyContentMainLayout;
    private LinearLayout policyHeaderLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_terms_and_condition, container, false);
        view.findViewById(R.id.toolbar).setVisibility(View.GONE);
        context = getContext();
        initView(view);
        getAboutUsData();


        return view;
    }


    private void getAboutUsData() {
        final ProgressBarHandler progressBarHandler = new ProgressBarHandler(context);
        progressBarHandler.show();
        Ion.with(context)
                .load(getString(R.string.webservice_base_url) + "/about_us")
                .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressBarHandler.hide();
                        if (result != null) {
                            AndroidUtils.showErrorLog(context, "if**********" + result.toString()/*, Html.fromHtml(result.get("result").getAsString())*/);
                            tvTermsAndConditions.setText(Html.fromHtml(result.get("result").getAsString()));
                        } else {
                            AndroidUtils.showErrorLog(context, "Else**********");
                        }
                    }
                });
    }

    private void initView(View view) {
        policyContentMainLayout = (LinearLayout) view.findViewById(R.id.policyContentMainLayout);
        AndroidUtils.setBackgroundSolid(policyContentMainLayout, context, android.R.color.transparent, 10, GradientDrawable.RECTANGLE);
        tvTermsAndConditions = (ExpandableTextView) view.findViewById(R.id.tvTermsAndConditions);
        tvTermsAndConditions.setText("");
        policyHeaderLayout = (LinearLayout) view.findViewById(R.id.policyHeaderLayout);
        tvReadMore = (TextView) view.findViewById(R.id.tvReadMore);
        tvReadMore.setBackground(AndroidUtils.setImageColor(context, R.drawable.ic_arrow, R.color.green));
        tvReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                policyHeaderLayout.animate().alpha(0.0f).setDuration(2000).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        policyHeaderLayout.setVisibility(View.GONE);
                        policyContentMainLayout.removeView(tvReadMore);
                    }
                });
                tvReadMore.setVisibility(View.GONE);
            }
        });
    }

}
