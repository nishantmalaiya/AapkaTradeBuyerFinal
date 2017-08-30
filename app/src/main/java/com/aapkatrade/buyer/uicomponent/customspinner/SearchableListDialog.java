package com.aapkatrade.buyer.uicomponent.customspinner;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.home.buyerregistration.spinner_adapter.SearchableListAdapter;

import java.util.ArrayList;

/**
 * Created by PPC17 on 23-Aug-17.
 */

public class SearchableListDialog extends DialogFragment {

    private View view;
    private Context context;
    private ArrayList<SpinnerDatas> countryArrayList;
    private ListView listView;
    private SearchView searchView;
    private SearchableListAdapter searchableListAdapter;
    private CommonInterface commonInterface;

    public SearchableListDialog(Context context, ArrayList<SpinnerDatas> countryArrayList, CommonInterface commonInterface) {
        this.context = context;
        this.countryArrayList = countryArrayList;
        this.commonInterface = commonInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.searchable_list_dialog, container, false);
  getDialog().setTitle("Select Country");
        initView(v);
        return v;
    }

    private void initView(View v) {
        searchView = v.findViewById(R.id.searchView);
        listView = v.findViewById(R.id.listView);
        searchableListAdapter = new SearchableListAdapter(context, countryArrayList, this, commonInterface);


        listView.setAdapter(searchableListAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(Validation.isNonEmptyStr(s))
                searchableListAdapter.getFilter().filter(s);
                return true;
            }
        });
    }

}