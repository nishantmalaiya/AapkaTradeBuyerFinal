package com.aapkatrade.buyer.uicomponent.customchecklist;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.entity.FormValue;
import com.aapkatrade.buyer.user_dashboard.order_list.OrderListAdapter;

import java.util.ArrayList;

/**
 * Created by PPC17 on 01-Apr-17.
 */

public class CustomCheckList extends LinearLayout {
    private Context context;
    private View view;
    private ProgressBarHandler progressBarHandler;
    private RecyclerView recyclerView;
    private TextInputLayout inputLayout;
    private FormValue title, value;
    private ArrayList<FormValue> formValueArrayList = new ArrayList<>(), selectedFormValueArrayList = new ArrayList<>();
    private TextView tvTitle;

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
        CustomCheckListAdapter.commonInterface = new CommonInterface() {
            @Override
            public Object getData(Object object) {
                if(object != null){
                    selectedFormValueArrayList = (ArrayList<FormValue>) object;
                    AndroidUtils.showErrorLog(context, "****************", selectedFormValueArrayList);
                }
                return null;
            }
        };
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

        if(formValueArrayList != null && formValueArrayList.size() > 0){
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            CustomCheckListAdapter customCheckListAdapter = new CustomCheckListAdapter(context, formValueArrayList, isRadio);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(customCheckListAdapter);
            customCheckListAdapter.notifyDataSetChanged();
        }

        if(Validation.isNonEmptyStr(title)){
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(title);
        }
    }

    public ArrayList<FormValue> getSelectedCheckList(){
        return selectedFormValueArrayList;
    }

}