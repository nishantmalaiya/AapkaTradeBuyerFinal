package com.aapkatrade.buyer.general.Utils.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.shopdetail.ShopDetailActivity;

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
    ArrayList<String> LocationList = new ArrayList<>();
    private ArrayList<String> filteredData;
    private ArrayList<String> originalData;
    private ValueFilter valueFilter;
    public ArrayList<String> productIdList = new ArrayList<>();
    TextView names;



    public Webservice_search_autocompleteadapter(Context applicationContext, ArrayList<String> names_data,ArrayList<String> distance_data,ArrayList<String> categoriesList,ArrayList<String> productIdList,ArrayList<String> LocationList)
    {
        this.context = applicationContext;
        this.names_data = names_data;
        this.distance_data=distance_data;
        this.categoriesList = categoriesList;
        this.productIdList = productIdList;
        this.LocationList = LocationList;
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
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        view = inflter.inflate(R.layout.search_suggestion_row, null);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutSearch);
         names = (TextView) view.findViewById(R.id.tv_shop_name);
        TextView distance = (TextView) view.findViewById(R.id.tvDistance);
        TextView category_name = (TextView) view.findViewById(R.id.tvCategoryName);
        Log.e("names", names_data.get(i));
        names.setText(names_data.get(i));
        distance.setText(distance_data.get(i));
        category_name.setText(categoriesList.get(i));

        //AndroidUtils.showToast(context,String.valueOf(i));

        relativeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AndroidUtils.showToast(context, LocationList.get(i));

                Intent intent = new Intent(context, ShopDetailActivity.class);
                intent.putExtra("product_id",productIdList.get(i));
                intent.putExtra("product_location", LocationList.get(i));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        return view;

    }

    @Override
    public Filter getFilter() {
            valueFilter = new ValueFilter();
        return valueFilter;
    }


    public class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();

            AndroidUtils.showErrorLog(context,constraint.length());

            if (constraint != null) {
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
                Log.e("results<2",results.values.toString());
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            originalData = (ArrayList<String>) results.values;
           // notifyDataSetInvalidated();
           notifyDataSetChanged();
        }


    }

}