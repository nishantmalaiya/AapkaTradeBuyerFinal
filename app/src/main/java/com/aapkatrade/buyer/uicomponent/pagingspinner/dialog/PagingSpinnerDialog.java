package com.aapkatrade.buyer.uicomponent.pagingspinner.dialog;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressDialogHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.CompanyShopListAdapter;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.AddProductActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.CompanyDropdownDatas;
import com.aapkatrade.buyer.uicomponent.pagingspinner.PagingSpinner;
import com.aapkatrade.buyer.uicomponent.pagingspinner.adapter.PagingSpinnerAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PagingSpinnerDialog extends DialogFragment {
    private Context context;
    private ImageView dialogClose;
    private ProgressDialogHandler progressDialogHandler;
    private ArrayList arrayList = new ArrayList();
    private String shopType = "0", sellerId = "0";
    private int page = 0, totalPage = 0;
    private RecyclerView recyclerView;
    private PagingSpinnerAdapter companyShopListAdapter;
    private LinearLayoutManager linearLayoutManager;
    public static CommonInterface commonInterface;

    public PagingSpinnerDialog(Context context, String shopType, String sellerId) {
        this.context = context;
        this.shopType = shopType;
        this.sellerId = sellerId;
        progressDialogHandler = new ProgressDialogHandler(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dialog_paging_spinner, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        initView(v);
        callCompanyListWebservice(++page);
        v.findViewById(R.id.closeDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }

    private void initView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AndroidUtils.showErrorLog(getActivity(),"Paging Spinner Dialog",v.getVerticalScrollbarPosition());
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();
                AndroidUtils.showErrorLog(context, "=====================_", "totalItemCount"+totalItemCount
                        +"lastVisibleItemCount"+lastVisibleItemCount
                +"totalPage"+totalPage
                +"page"+page);
                if (totalItemCount > 0 && totalPage > page) {
                    if ((totalItemCount - 1) == lastVisibleItemCount) {
                        callCompanyListWebservice(++page);
                    }
                }
            }

        });

        commonInterface = new CommonInterface() {
            @Override
            public Object getData(Object object) {
                int position = (int) object;
                if(position >= 0){
                    CompanyDropdownDatas companyDropdownDatas = (CompanyDropdownDatas) arrayList.get(position);
                    PagingSpinner.commonInterface.getData(companyDropdownDatas);
                }
                return null;
            }
        };

    }


    private void callCompanyListWebservice(final int page) {
        progressDialogHandler.show();
        Ion.with(context)
                .load(context.getString(R.string.webservice_base_url).concat("/shoplist"))
                .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("seller_id", sellerId)
                .setBodyParameter("lat", "0")
                .setBodyParameter("long", "0")
                .setBodyParameter("apply", "1")
                .setBodyParameter("shoptype", this.shopType)
                .setBodyParameter("page", String.valueOf(page))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                progressDialogHandler.hide();
                if (result != null) {
                    if (result.get("error").getAsString().contains("false")) {
                        JsonArray jsonArray_response = result.getAsJsonArray("result");
                        totalPage = result.get("total_page").getAsInt();

                        for (int i = 0; i < jsonArray_response.size(); i++) {


                            JsonObject jsonObject = jsonArray_response.get(i).getAsJsonObject();
                            String companyId = jsonObject.get("id").getAsString();
                            String companyImageUrl = jsonObject.get("image_url").getAsString();
                            String companyName = jsonObject.get("name").getAsString();
                            String comapanyCategory = jsonObject.get("category_name").getAsString();

                            arrayList.add(new CompanyDropdownDatas(companyId, companyImageUrl, companyName, comapanyCategory));

                        }
                        AndroidUtils.showErrorLog(context, "arrayList.size---------->", arrayList.size());

                        if (companyShopListAdapter == null) {
                            AndroidUtils.showErrorLog(context, "companyShopListAdapter---------null->");

                            companyShopListAdapter = new PagingSpinnerAdapter(context, arrayList, PagingSpinnerDialog.this);
                            linearLayoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(companyShopListAdapter);
//                            recyclerView.smoothScrollToPosition(arrayList.size());
                        } else {
                            AndroidUtils.showErrorLog(context, "companyShopListAdapter----------else>");

                            companyShopListAdapter.notifyDataSetChanged();
                        }
                    }
                }


                AndroidUtils.showErrorLog(context, "ShopListResponse", result);


            }
        });
    }




}
