package com.aapkatrade.buyer.general.Utils.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.entity.KeyValue;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.entity.FormValue;

import java.util.ArrayList;

/**
 * Created by PPC21 on 24-Jan-17.
 */

public class CustomSpinnerAdapter extends BaseAdapter
{
    Context context;
    ArrayList arrayList,id;
    LayoutInflater inflter;

    public CustomSpinnerAdapter(Context applicationContext, ArrayList arrayList, ArrayList id)
    {
        this.context = applicationContext;
        this.arrayList = arrayList;
        this.id=id;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public CustomSpinnerAdapter(Context applicationContext, ArrayList arrayList)
    {
        this.context = applicationContext;
        this.arrayList = arrayList;
        inflter = (LayoutInflater.from(applicationContext));
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
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        view = inflter.inflate(R.layout.row_spinner, null);

        TextView spinnerItemName = (TextView) view.findViewById(R.id.tvSpCategory);
        if(arrayList.get(i) instanceof KeyValue){
            spinnerItemName.setText(((KeyValue) arrayList.get(i)).value.toString());
            spinnerItemName.setTextColor(ContextCompat.getColor(context,R.color.black));
        } else if(arrayList.get(i) instanceof FormValue) {
            view.findViewById(R.id.view1).setVisibility(View.VISIBLE);
            view.findViewById(R.id.view2).setVisibility(View.VISIBLE);
            spinnerItemName.setText(((FormValue) arrayList.get(i)).getValue());
        }else {
            spinnerItemName.setText(arrayList.get(i).toString());
        }
        return view;
    }
}