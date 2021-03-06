package com.aapkatrade.buyer.filter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.filter.entity.FilterObject;
import com.aapkatrade.buyer.filter.viewholder.FilterColumn2ViewHolder;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.entity.KeyValue;

import java.util.ArrayList;

public class FilterColumn2RecyclerAdapter extends RecyclerView.Adapter<FilterColumn2ViewHolder> {
    private final LayoutInflater inflater;
    private ArrayList<FilterObject> filterValueList = new ArrayList<>();
    private ArrayList<FilterObject> selectedValueList = new ArrayList<>();
    private Context context;
    public static CommonInterface commonInterface;
    private String key = "";

    public FilterColumn2RecyclerAdapter(Context context,String key, ArrayList<FilterObject> filterValueList) {
        this.context = context;
        this.key = key;
        this.filterValueList = filterValueList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public FilterColumn2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterColumn2ViewHolder(inflater.inflate(R.layout.row_filter_column2, parent, false));
    }

    @Override
    public void onBindViewHolder(final FilterColumn2ViewHolder holder, final int position) {
        if(holder!=null) {
            if (Validation.isNonEmptyStr(filterValueList.get(position).name.value.toString())) {
                holder.checkFilterValue.setChecked(filterValueList.get(position).isChecked);
                holder.filterValue.setText(filterValueList.get(position).name.value.toString());
                holder.filterValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!holder.checkFilterValue.isChecked()) {
                            holder.checkFilterValue.setChecked(true);
                        } else {
                            holder.checkFilterValue.setChecked(false);
                        }
                    }
                });
                holder.checkFilterValue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        filterValueList.get(position).isChecked = isChecked;
                        if (isChecked) {
                            addItemsToList(position);
                        } else {
                            removeItemsFromList(position);
                        }
                        commonInterface.getData(selectedValueList.size() > 0 ? new KeyValue(key, selectedValueList) : new KeyValue(key, filterValueList));
                    }
                });

            }
        }
    }

    private void removeItemsFromList(int position) {
        if(selectedValueList.contains(filterValueList.get(position))){
            for(int i = 0; i < selectedValueList.size(); i++){
                if(selectedValueList.get(i).equals(filterValueList.get(position))){
                    selectedValueList.remove(i);
                }
            }
            AndroidUtils.showErrorLog(context, "Item No removed ;  " + position+"  new size "+selectedValueList.size());
        }
    }


    @Override
    public int getItemCount() {
        return filterValueList.size();
    }

    private void addItemsToList(int position){
        AndroidUtils.showErrorLog(context, "Item No Selected ;  " + position);
        if (!selectedValueList.contains(filterValueList.get(position))){
            selectedValueList.add(filterValueList.get(position));
            AndroidUtils.showErrorLog(context, "Item No Added ;  " + position+"  new size "+selectedValueList.size());
        }
    }

}

