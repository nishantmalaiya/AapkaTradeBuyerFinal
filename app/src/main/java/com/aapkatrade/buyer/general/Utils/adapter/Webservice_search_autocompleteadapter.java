package com.aapkatrade.buyer.general.Utils.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.aapkatrade.buyer.R;

import java.util.ArrayList;

/**
 * Created by PPC21 on 06-Feb-17.
 */

public class Webservice_search_autocompleteadapter extends BaseAdapter implements Filterable
{


    Context context;
    ArrayList<String> names_data;
    ArrayList<String> distance_data;
    LayoutInflater inflter;
    private ArrayList<String> categoriesList = new ArrayList<>();
    private ArrayList<String> filteredData;
    private ArrayList<String> originalData;
    private ValueFilter valueFilter;


    public Webservice_search_autocompleteadapter(Context applicationContext, ArrayList<String> names_data,ArrayList<String> distance_data,ArrayList<String> categoriesList)
    {
        this.context = applicationContext;

        this.names_data = names_data;
        this.distance_data=distance_data;
        this.categoriesList = categoriesList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return names_data.size();
    }

    @Override
    public Object getItem(int i) {
        return names_data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.search_suggestion_row, null);
        TextView names = (TextView) view.findViewById(R.id.tv_shop_name);
        TextView distance = (TextView) view.findViewById(R.id.tvDistance);
        TextView category_name = (TextView) view.findViewById(R.id.tvCategoryName);
        Log.e("names", names_data.get(i));
        names.setText(names_data.get(i));
        distance.setText(distance_data.get(i));
        category_name.setText(categoriesList.get(i));
        return view;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();

        }
        return valueFilter;
    }


    public class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> filterList = new ArrayList<>();
                for (int i = 0; i < names_data.size(); i++) {
                    String contact = names_data.get(i);
                    if ((contact.toUpperCase()).contains(constraint.toString().toUpperCase())
                            ) {
                        filterList.add(names_data.get(i));
                    }

                }
                results.count = filterList.size();
                results.values = filterList;
                Log.e("results>0",results.values.toString());
            } else {
                results.count = names_data.size();
                results.values = names_data;
                Log.e("results<0",results.values.toString());
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            originalData = (ArrayList<String>) results.values;
            notifyDataSetInvalidated();
           //notifyDataSetChanged();
        }




    }







}