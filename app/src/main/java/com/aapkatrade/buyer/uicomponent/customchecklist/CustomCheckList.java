package com.aapkatrade.buyer.uicomponent.customchecklist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.entity.KeyValue;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.entity.FormValue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by PPC17 on 01-Apr-17.
 */

public class CustomCheckList extends LinearLayout {
    private Context context;
    private View view;
    private ProgressBarHandler progressBarHandler;
    private RecyclerView recyclerView;
    private TextInputLayout inputLayout;
    private ArrayList<FormValue> formValueArrayList = new ArrayList<>();
    private ArrayList<Integer> selectedItemsArrayList = new ArrayList<>();
    private TextView tvTitle;
    private boolean isRadio = false;
    public CommonInterface commonInterface;

    public CustomCheckList(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomCheckList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CustomCheckList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @SuppressLint("NewApi")
    public CustomCheckList(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    }

    private void initView() {
        inputLayout = (TextInputLayout) findViewById(R.id.input_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvTitle = (TextView) findViewById(R.id.tv_title);
    }

    protected int layoutId() {
        return R.layout.layout_dynamic_check_list;
    }

    public void setBasicRequiredValues(String title, ArrayList<FormValue> formValueArrayList, boolean isRadio){
        this.formValueArrayList = formValueArrayList;
        this.isRadio = isRadio;
        if(formValueArrayList != null && formValueArrayList.size() > 0){
            commonInterface = new CommonInterface() {
                @Override
                public Object getData(Object object) {
                    if(object != null){
                        selectedItemsArrayList = (ArrayList<Integer>) object;
                        AndroidUtils.showErrorLog(context, "****************", selectedItemsArrayList);
                    }
                    return null;
                }
            };
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            CustomCheckListAdapter customCheckListAdapter = new CustomCheckListAdapter(context, title, commonInterface, formValueArrayList, isRadio);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(customCheckListAdapter);
            recyclerView.smoothScrollToPosition(formValueArrayList.size());
            customCheckListAdapter.notifyDataSetChanged();
        }

        if(Validation.isNonEmptyStr(title)){
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(title);
        }
    }

    public ArrayList<FormValue> getSelectedCheckList(){
        ArrayList<FormValue> finalSelectedList = new ArrayList<>();
        if(formValueArrayList!=null && formValueArrayList.size() >0){
            for (int i : selectedItemsArrayList){
                finalSelectedList.add(formValueArrayList.get(i));
            }
        }
        return finalSelectedList;
    }

}