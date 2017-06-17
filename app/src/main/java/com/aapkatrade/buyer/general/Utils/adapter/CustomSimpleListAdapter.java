package com.aapkatrade.buyer.general.Utils.adapter;

/**
 * Created by PPC16 on 10-Jan-17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.home.navigation.entity.Category;
import com.aapkatrade.buyer.home.navigation.entity.SubCategory;


import java.util.ArrayList;

public class CustomSimpleListAdapter extends BaseAdapter {
    Context context;
    ArrayList categoriesNames;
    LayoutInflater inflter;


    public CustomSimpleListAdapter(Context applicationContext, ArrayList categoriesNames) {
        this.context = applicationContext;
        this.categoriesNames = categoriesNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return categoriesNames.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override


    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.row_spinner, null);
        TextView names = (TextView) view.findViewById(R.id.tvSpCategory);

        if (categoriesNames.get(i) instanceof String) {
            names.setText((CharSequence) categoriesNames.get(i));
        } else if (categoriesNames.get(i) instanceof Category) {
            names.setText(((Category) categoriesNames.get(i)).getCategoryName());
        } else if (categoriesNames.get(i) instanceof SubCategory) {
            names.setText(((SubCategory) categoriesNames.get(i)).subCategoryName);
        } else {
            names.setText(categoriesNames.get(i).toString());
        }
        return view;
    }
}