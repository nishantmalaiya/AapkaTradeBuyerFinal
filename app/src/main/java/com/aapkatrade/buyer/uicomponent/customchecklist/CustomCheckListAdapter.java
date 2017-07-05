package com.aapkatrade.buyer.uicomponent.customchecklist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.filter.viewholder.FilterColumn2ViewHolder;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.entity.KeyValue;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.entity.FormValue;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomCheckListAdapter extends RecyclerView.Adapter<FilterColumn2ViewHolder> {
    private ArrayList<FormValue> formValueArrayList = new ArrayList<>();
    private ArrayList<FormValue> selectedItemsArrayList = new ArrayList<>();
    private HashMap<String, ArrayList<FormValue>>stringArrayListHashMap = new HashMap<>();
    private Context context;
    private RadioButton radioButton;
    private String title;

    public static CommonInterface commonInterface;
    private boolean isRadio;


    public CustomCheckListAdapter(Context context, String title, ArrayList<FormValue> formValueArrayList, boolean isRadio) {
        this.context = context;
        this.formValueArrayList = formValueArrayList;
        this.isRadio = isRadio;
        this.title = title;
    }

    @Override
    public FilterColumn2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterColumn2ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_filter_column2, parent, false), isRadio);
    }

    @Override
    public void onBindViewHolder(final FilterColumn2ViewHolder holder, final int position) {
        if (isRadio) {
            if (holder != null) {
                holder.radioButton.setChecked(false);
                holder.filterValue.setText(formValueArrayList.get(position).getValue());
                holder.filterValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!holder.radioButton.isChecked()) {
                            holder.radioButton.setChecked(true);
                        } else {
                            holder.radioButton.setChecked(false);
                        }
                    }
                });
                holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        holder.radioButton.setChecked(true);
                        if (radioButton != null) {
                            radioButton.setChecked(false);
                        }
                        radioButton = holder.radioButton;
                        if(isChecked){
                            selectedItemsArrayList.add(formValueArrayList.get(position));
                        } else {
                            selectedItemsArrayList.remove(0);
                        }
                        commonInterface.getData(new KeyValue(title, selectedItemsArrayList));
                    }
                });

            }
        } else {
            if (holder != null) {
                holder.checkFilterValue.setChecked(false);
                holder.filterValue.setText(formValueArrayList.get(position).getValue());
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
                        if (isChecked) {
                            addItemsToList(position);
                        } else {
                            removeItemsFromList(position);
                        }
                    }
                });

            }
        }
    }


    private void removeItemsFromList(int position) {
        if (selectedItemsArrayList.contains(formValueArrayList.get(position))) {
            for (int i = 0; i < selectedItemsArrayList.size(); i++) {
                if (selectedItemsArrayList.get(i).equals(formValueArrayList.get(position))) {
                    selectedItemsArrayList.remove(i);
                }
            }
            AndroidUtils.showErrorLog(context, "Item No removed ;  " + position + "  new size " + selectedItemsArrayList.size());
        }
        commonInterface.getData(new KeyValue(title, selectedItemsArrayList));
    }


    @Override
    public int getItemCount() {
        return formValueArrayList.size();
    }

    private void addItemsToList(int position) {
        AndroidUtils.showErrorLog(context, "Item No Selected ;  " + position);
        if (!selectedItemsArrayList.contains(formValueArrayList.get(position))) {
            selectedItemsArrayList.add(formValueArrayList.get(position));
            AndroidUtils.showErrorLog(context, "Item No Added ;  " + position + "  new size " + selectedItemsArrayList.size());
        }
        commonInterface.getData(new KeyValue(title, selectedItemsArrayList));
    }

}

