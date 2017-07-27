package com.aapkatrade.buyer.uicomponent.pagingspinner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.CompanyDropdownDatas;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.ProductMediaData;
import com.aapkatrade.buyer.uicomponent.pagingspinner.dialog.PagingSpinnerDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

/**
 * Created by PPC15 on 7/19/2017.
 */

public class PagingSpinner extends RelativeLayout {
    private Context context;
    private AppSharedPreference appSharedpreference;
    private View view;
    private ProgressBarHandler progressBarHandler;
    private RelativeLayout relativeLayoutRoot;
    private LinearLayout linearLayoutMain;
    private String shopType = "0", sellerId = "0", shopId = "0";
    private PagingSpinnerDialog pagingSpinnerDialog;
    public static CommonInterface commonInterface;
    public CommonInterface commonInterfaceOuter;

    public PagingSpinner(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PagingSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public PagingSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @SuppressLint("NewApi")
    public PagingSpinner(final Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    protected void init() {
        appSharedpreference = new AppSharedPreference(context);
        if (this.isInEditMode()) {
            return;
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layoutId(), this, true);
        progressBarHandler = new ProgressBarHandler(context);
        relativeLayoutRoot = (RelativeLayout) view.findViewById(R.id.linearLayoutRoot);
        linearLayoutMain = (LinearLayout) view.findViewById(R.id.linearLayoutMain);
        linearLayoutMain.findViewById(R.id.rootContainer).setVisibility(VISIBLE);
        linearLayoutMain.findViewById(R.id.container_simple_spinner).setVisibility(View.VISIBLE);
        ((TextView) linearLayoutMain.findViewById(R.id.tvSpCategory)).setText("Please Select Company/Shop");
        linearLayoutMain.findViewById(R.id.containershoplist).setVisibility(View.GONE);
//        AndroidUtils.setBackgroundStroke(relativeLayoutRoot, context, R.color.green, 10, 3);
        relativeLayoutRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.showErrorLog(context, "relativeLayoutRoot clicked.");
                pagingSpinnerDialog = new PagingSpinnerDialog(context, shopType, sellerId);
                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                pagingSpinnerDialog.show(fm, "PagingSpinnerDialog");
            }
        });
        commonInterface = new CommonInterface() {
            @Override
            public Object getData(Object object) {
                if (object != null) {
                    CompanyDropdownDatas companyDropdownDatas = (CompanyDropdownDatas) object;
                    shopId = companyDropdownDatas.comapanyId;
                    if(commonInterfaceOuter!=null){
                        commonInterfaceOuter.getData(shopId);
                    }
                    linearLayoutMain.findViewById(R.id.rootContainer).setVisibility(VISIBLE);
                    linearLayoutMain.findViewById(R.id.container_simple_spinner).setVisibility(View.GONE);
                    linearLayoutMain.findViewById(R.id.containershoplist).setVisibility(View.VISIBLE);

                    AndroidUtils.showErrorLog(context, "Object received with value : " + companyDropdownDatas.toString());


                    if (Validation.isNonEmptyStr(companyDropdownDatas.companyImageUrl)) {
                        Picasso.with(context)
                                .load(companyDropdownDatas.companyImageUrl)
                                .error(R.drawable.banner)
                                .placeholder(R.drawable.default_noimage)
                                .error(R.drawable.default_noimage)
                                .into((de.hdodenhof.circleimageview.CircleImageView) linearLayoutMain.findViewById(R.id.shopimage));
                    }


                    StringBuilder stringBuilder_txnamount = new StringBuilder("<font size=\"20\" color=" + "#ffffff>" + companyDropdownDatas.companyName +
                            companyDropdownDatas.comapanyCategory + "</font>");
                    String tvData = stringBuilder_txnamount.toString();
//                AndroidUtils.showErrorLog(context, position+"position"+arrayList.size()+"arrayList.size()   "+"work1***" + Html.fromHtml(tvData));
                    ((TextView) linearLayoutMain.findViewById(R.id.tvshopdropdownshopname)).setText(companyDropdownDatas.companyName);
                    ((TextView) linearLayoutMain.findViewById(R.id.tvshopdropdownshopcategory)).setText("Category : " + companyDropdownDatas.comapanyCategory);
                } else {
                    linearLayoutMain.findViewById(R.id.rootContainer).setVisibility(VISIBLE);
                    linearLayoutMain.findViewById(R.id.container_simple_spinner).setVisibility(View.VISIBLE);
                    ((TextView) linearLayoutMain.findViewById(R.id.tvSpCategory)).setText("Please Select Company/Shop");
                    linearLayoutMain.findViewById(R.id.containershoplist).setVisibility(View.GONE);
                }
                return null;
            }
        };
    }


    private int layoutId() {
        return R.layout.paging_spinner_container;
    }

    public void setShopType(int shopType) {
        this.shopType = String.valueOf(shopType);
    }

    public String getShopType() {
        return Validation.isEmptyStr(this.shopType) ? "0" : this.shopType;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerId() {
        return Validation.isEmptyStr(this.sellerId) ? "0" : this.sellerId;
    }

    public String getShopId() {
        return Validation.isEmptyStr(this.shopId) ? "0" : this.shopId;
    }

    public void setShopData(String productId) {
        AndroidUtils.showErrorLog(context, "     productId     ", productId);
        progressBarHandler.show();
        Ion.with(context)
                .load(context.getString(R.string.webservice_base_url).concat("/shop_short_details"))
                .setHeader("Authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("authorization", "xvfdbgfdhbfdhtrh54654h54ygdgerwer3")
                .setBodyParameter("id", productId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        AndroidUtils.showErrorLog(context, "result shop_short_details -----> ", result);
                        progressBarHandler.hide();
                        if (result != null) {
                            if (result.get("error").getAsString().contains("true")) {
                                JsonObject jsonObject = result.get("result").getAsJsonObject();

                                CompanyDropdownDatas companyDropdownDatas = new CompanyDropdownDatas(jsonObject.get("id").getAsString(), jsonObject.get("image_url").getAsString(), jsonObject.get("name").getAsString(), jsonObject.get("category_name").getAsString());
                                shopId = companyDropdownDatas.comapanyId;
                                linearLayoutMain.findViewById(R.id.rootContainer).setVisibility(VISIBLE);
                                linearLayoutMain.findViewById(R.id.container_simple_spinner).setVisibility(View.GONE);
                                linearLayoutMain.findViewById(R.id.containershoplist).setVisibility(View.VISIBLE);

                                AndroidUtils.showErrorLog(context, "2222 Object received with value : " + companyDropdownDatas.toString());


                                if (Validation.isNonEmptyStr(companyDropdownDatas.companyImageUrl)) {
                                    Picasso.with(context)
                                            .load(companyDropdownDatas.companyImageUrl)
                                            .error(R.drawable.banner)
                                            .placeholder(R.drawable.default_noimage)
                                            .error(R.drawable.default_noimage)
                                            .into((de.hdodenhof.circleimageview.CircleImageView) linearLayoutMain.findViewById(R.id.shopimage));
                                }


                                StringBuilder stringBuilder_txnamount = new StringBuilder("<font size=\"20\" color=" + "#ffffff>" + companyDropdownDatas.companyName +
                                        companyDropdownDatas.comapanyCategory + "</font>");
//                AndroidUtils.showErrorLog(context, position+"position"+arrayList.size()+"arrayList.size()   "+"work1***" + Html.fromHtml(tvData));
                                ((TextView) linearLayoutMain.findViewById(R.id.tvshopdropdownshopname)).setText(companyDropdownDatas.companyName);
                                ((TextView) linearLayoutMain.findViewById(R.id.tvshopdropdownshopcategory)).setText("Category : " + companyDropdownDatas.comapanyCategory);
                            }
                        }
                    }
                });
    }

}
