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
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.entity.KeyValue;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.entity.FormValue;

import java.util.ArrayList;

public class CustomCheckListAdapter extends RecyclerView.Adapter<FilterColumn2ViewHolder> {
    private ArrayList<FormValue> formValueArrayList = new ArrayList<>();
    private ArrayList<FormValue> selectedValueList = new ArrayList<>();
    private Context context;
    private RadioButton radioButton;

    public static CommonInterface commonInterface;
    private boolean isRadio;

    public CustomCheckListAdapter(Context context, ArrayList<FormValue> formValueArrayList) {
        this.context = context;
        this.formValueArrayList = formValueArrayList;
    }

    public CustomCheckListAdapter(Context context, ArrayList<FormValue> formValueArrayList, boolean isRadio) {
        this.context = context;
        this.formValueArrayList = formValueArrayList;
        this.isRadio = isRadio;
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
                            commonInterface.getData(radioButton);
                        }
                        radioButton = holder.radioButton;
                    }
                });

            }
        } else {
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
                        if (isChecked) {
                            addItemsToList(position);
                        } else {
                            removeItemsFromList(position);
                        }
                        if (selectedValueList != null)
                            commonInterface.getData(selectedValueList.size() > 0 ? selectedValueList : formValueArrayList);
                    }
                });

            }
        }
    }


    private void removeItemsFromList(int position) {
        if (selectedValueList.contains(formValueArrayList.get(position))) {
            for (int i = 0; i < selectedValueList.size(); i++) {
                if (selectedValueList.get(i).equals(formValueArrayList.get(position))) {
                    selectedValueList.remove(i);
                }
            }
            AndroidUtils.showErrorLog(context, "Item No removed ;  " + position + "  new size " + selectedValueList.size());
        }
    }


    @Override
    public int getItemCount() {
        return formValueArrayList.size();
    }

    private void addItemsToList(int position) {
        AndroidUtils.showErrorLog(context, "Item No Selected ;  " + position);
        if (!selectedValueList.contains(formValueArrayList.get(position))) {
            selectedValueList.add(formValueArrayList.get(position));
            AndroidUtils.showErrorLog(context, "Item No Added ;  " + position + "  new size " + selectedValueList.size());
        }
    }

}

