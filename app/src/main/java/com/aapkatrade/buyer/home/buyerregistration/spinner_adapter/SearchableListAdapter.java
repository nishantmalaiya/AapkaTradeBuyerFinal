package com.aapkatrade.buyer.home.buyerregistration.spinner_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.home.buyerregistration.BuyerRegistrationActivity;
import com.aapkatrade.buyer.home.buyerregistration.entity.Country;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.uicomponent.customspinner.SearchableListDialog;
import com.aapkatrade.buyer.uicomponent.customspinner.SpinnerDatas;

import java.util.ArrayList;

/**
 * Created by PPC16 on 16-Jan-17.
 */

public class SearchableListAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<SpinnerDatas> countryArrayList, countryArrayListOrginal;
    private SearchableListDialog searchableListDialog;
    private CommonInterface commonInterface;

    public SearchableListAdapter(Context context, ArrayList<SpinnerDatas> countryArrayList, SearchableListDialog searchableListDialog, CommonInterface commonInterface) {
        this.context = context;
        this.countryArrayList = countryArrayList;
        this.searchableListDialog = searchableListDialog;
        this.commonInterface = commonInterface;
    }

    @Override
    public int getCount() {
        return countryArrayList.size();
    }


    @Override
    public Object getItem(int i) {
        return countryArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        AndroidUtils.showErrorLog(context, "List Size&&&&&&&&&& ", countryArrayList.size());
        view = LayoutInflater.from(context).inflate(R.layout.row_spinner, null);
        TextView names = view.findViewById(R.id.tvSpCategory);
        names.setText(countryArrayList.get(i).Name);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonInterface.getData(countryArrayList.get(i).Id);
                searchableListDialog.dismiss();
            }
        });
        return view;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<SpinnerDatas> results = new ArrayList<SpinnerDatas>();
                if (countryArrayListOrginal == null)
                    countryArrayListOrginal = countryArrayList;
                if (constraint != null) {
                    if (countryArrayListOrginal != null && countryArrayListOrginal.size() > 0) {
                        for (final SpinnerDatas g : countryArrayListOrginal) {
                            if (g.Name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                                results.add(g);
                            }

                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                countryArrayList = (ArrayList<SpinnerDatas>) results.values;
                notifyDataSetChanged();
            }
        };
    }


}
