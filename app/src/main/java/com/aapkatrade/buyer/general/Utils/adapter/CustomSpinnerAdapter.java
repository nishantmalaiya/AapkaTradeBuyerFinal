package com.aapkatrade.buyer.general.Utils.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.entity.KeyValue;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.CompanyShopData;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.AddProductActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.CompanyDropdownDatas;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.entity.FormValue;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by PPC21 on 24-Jan-17.
 */

public class CustomSpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList arrayList, id;
    LayoutInflater inflter;

    public CustomSpinnerAdapter(Context context, ArrayList arrayList, ArrayList id) {
        this.context = context;
        this.arrayList = arrayList;
        this.id = id;
        inflter = (LayoutInflater.from(context));
    }

    public CustomSpinnerAdapter(Context context, ArrayList arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.row_spinner, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.container_simple_spinner);
        RelativeLayout containershoplist = (RelativeLayout) view.findViewById(R.id.containershoplist);
        TextView spinnerItemName = (TextView) view.findViewById(R.id.tvSpCategory);
        if (arrayList.get(position) instanceof CompanyDropdownDatas) {
           if(((CompanyDropdownDatas) arrayList.get(position)).comapanyCategory!="") {
               linearLayout.setVisibility(View.GONE);
               containershoplist.setVisibility(View.VISIBLE);


               CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.shopimage);
               TextView tvshopdropdownShopname = (TextView) view.findViewById(R.id.tvshopdropdownshopname);
               TextView tvshopdropdownCategoryname = (TextView) view.findViewById(R.id.tvshopdropdownshopcategory);


               if (Validation.isNonEmptyStr(((CompanyDropdownDatas) arrayList.get(position)).companyImageUrl)) {
                   Picasso.with(context)
                           .load(((CompanyDropdownDatas) arrayList.get(position)).companyImageUrl)
                           .error(R.drawable.banner)
                           .placeholder(R.drawable.default_noimage)
                           .error(R.drawable.default_noimage)
                           .into(circleImageView);
               }


               StringBuilder stringBuilder_txnamount = new StringBuilder("<font size=\"20\" color=" + "#ffffff>" + ((CompanyDropdownDatas) arrayList.get(position)).companyName +
                       ((CompanyDropdownDatas) arrayList.get(position)).comapanyCategory + "</font>");
               String tvData = stringBuilder_txnamount.toString();
               AndroidUtils.showErrorLog(context, position+"position"+arrayList.size()+"arrayList.size()   "+"work1***" + Html.fromHtml(tvData));
               tvshopdropdownShopname.setText(((CompanyDropdownDatas) arrayList.get(position)).companyName);
               tvshopdropdownCategoryname.setText("Category : " + ((CompanyDropdownDatas) arrayList.get(position)).comapanyCategory);
               if(position == arrayList.size()-1) {
                   AddProductActivity.commonInterface.getData(true);
               }
               return view;
           }
           else{

               linearLayout.setVisibility(View.VISIBLE);
               containershoplist.setVisibility(View.GONE);
               spinnerItemName.setText("Please Select Shop/Company");
               return view;
           }

        } else {


            if (arrayList.get(position) instanceof KeyValue) {
                spinnerItemName.setText(((KeyValue) arrayList.get(position)).value.toString());
                spinnerItemName.setTextColor(ContextCompat.getColor(context, R.color.black));
            } else if (arrayList.get(position) instanceof FormValue) {
                view.findViewById(R.id.view1).setVisibility(View.VISIBLE);
                view.findViewById(R.id.view2).setVisibility(View.VISIBLE);
                spinnerItemName.setText(((FormValue) arrayList.get(position)).getValue());
            } else {
                spinnerItemName.setText(arrayList.get(position).toString());
            }

            return view;
        }

    }
}