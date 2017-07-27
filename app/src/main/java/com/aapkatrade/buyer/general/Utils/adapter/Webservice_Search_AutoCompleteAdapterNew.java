package com.aapkatrade.buyer.general.Utils.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.shopdetail.ShopDetailActivity;
import java.util.ArrayList;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

/**
 * Created by akshay on 1/2/15.
 */
public class Webservice_Search_AutoCompleteAdapterNew extends ArrayAdapter<SearchAutoCompleteData> {

    Context context;
    int resource, textViewResourceId;
    ArrayList<SearchAutoCompleteData> items, tempItems, suggestions;

    public Webservice_Search_AutoCompleteAdapterNew(Context context, int resource, ArrayList<SearchAutoCompleteData> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<SearchAutoCompleteData>(items); // this makes the difference.
        suggestions = new ArrayList<SearchAutoCompleteData>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.search_suggestion_row, parent, false);
        }
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutSearch);
        TextView names = (TextView) view.findViewById(R.id.tv_shop_name);
        TextView distance = (TextView) view.findViewById(R.id.tvDistance);
        TextView category_name = (TextView) view.findViewById(R.id.tvCategoryName);

        final SearchAutoCompleteData people = items.get(position);
        if (people != null)
        {
            names.setText(people.getShop_name());
            distance.setText(people.getShop_distance());
            category_name.setText(people.getCategory_name());

        }

        relativeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(context, ShopDetailActivity.class);
                intent.putExtra("product_id",people.getProduct_id());
                intent.putExtra("product_location", people.getShop_location());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });

        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((SearchAutoCompleteData) resultValue).getShop_name();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (SearchAutoCompleteData people : tempItems) {
                    if (people.getShop_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<SearchAutoCompleteData> filterList = (ArrayList<SearchAutoCompleteData>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (SearchAutoCompleteData people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}