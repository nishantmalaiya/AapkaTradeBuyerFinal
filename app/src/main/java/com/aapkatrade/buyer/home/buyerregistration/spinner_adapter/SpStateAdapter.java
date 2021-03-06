package com.aapkatrade.buyer.home.buyerregistration.spinner_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aapkatrade.buyer.home.buyerregistration.entity.State;
import com.aapkatrade.buyer.R;

import java.util.ArrayList;

/**
 * Created by PPC16 on 16-Jan-17.
 */

public class SpStateAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<State> stateList = new ArrayList<>();
    private LayoutInflater inflter;
    private boolean isProductCount = false;

    public SpStateAdapter(Context applicationContext, ArrayList<State> stateList) {
        this.context = applicationContext;
        this.stateList = stateList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return stateList.size();
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
        names.setText(stateList.get(i).stateName);
        return view;
    }


}
