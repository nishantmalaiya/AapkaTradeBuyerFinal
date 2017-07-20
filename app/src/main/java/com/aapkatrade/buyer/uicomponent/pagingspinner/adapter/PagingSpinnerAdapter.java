package com.aapkatrade.buyer.uicomponent.pagingspinner.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.AddProductActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.CompanyDropdownDatas;
import com.aapkatrade.buyer.uicomponent.pagingspinner.dialog.PagingSpinnerDialog;
import com.aapkatrade.buyer.uicomponent.pagingspinner.viewholder.PagingSpinnnerViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by PPC15 on 7/19/2017.
 */

public class PagingSpinnerAdapter extends RecyclerView.Adapter<PagingSpinnnerViewHolder> {
    private Context context;
    private ArrayList arrayList;

    public PagingSpinnerAdapter(Context context, ArrayList arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public PagingSpinnnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PagingSpinnnerViewHolder((LayoutInflater.from(context)).inflate(R.layout.row_spinner, null));
    }

    @Override
    public void onBindViewHolder(PagingSpinnnerViewHolder holder, final int position) {

//        AndroidUtils.setGradientColor(holder.rootContainer, android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.green), ContextCompat.getColor(context, R.color.green_gradient3), android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, 0);


        if (arrayList.get(position) instanceof CompanyDropdownDatas) {
            if(((CompanyDropdownDatas) arrayList.get(position)).comapanyCategory!="") {
                holder.linearLayout.setVisibility(View.GONE);
                holder.containershoplist.setVisibility(View.VISIBLE);




                if (Validation.isNonEmptyStr(((CompanyDropdownDatas) arrayList.get(position)).companyImageUrl)) {
                    Picasso.with(context)
                            .load(((CompanyDropdownDatas) arrayList.get(position)).companyImageUrl)
                            .error(R.drawable.banner)
                            .placeholder(R.drawable.default_noimage)
                            .error(R.drawable.default_noimage)
                            .into(holder.circleImageView);
                }


                StringBuilder stringBuilder_txnamount = new StringBuilder("<font size=\"20\" color=" + "#ffffff>" + ((CompanyDropdownDatas) arrayList.get(position)).companyName +
                        ((CompanyDropdownDatas) arrayList.get(position)).comapanyCategory + "</font>");
                String tvData = stringBuilder_txnamount.toString();
                AndroidUtils.showErrorLog(context, position+"position"+arrayList.size()+"arrayList.size()   "+"work1***" + Html.fromHtml(tvData));
                holder.tvshopdropdownShopname.setText(((CompanyDropdownDatas) arrayList.get(position)).companyName);
                holder.tvshopdropdownCategoryname.setText("Category : " + ((CompanyDropdownDatas) arrayList.get(position)).comapanyCategory);
                holder.rootContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if(position == arrayList.size()-1) {
                            PagingSpinnerDialog.commonInterface.getData(position);
//                        }
                    }
                });

            }
            else{
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.containershoplist.setVisibility(View.GONE);
                holder.spinnerItemName.setText("Please Select Shop/Company");
            }

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}