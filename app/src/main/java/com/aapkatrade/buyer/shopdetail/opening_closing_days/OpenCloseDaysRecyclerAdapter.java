package com.aapkatrade.buyer.shopdetail.opening_closing_days;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;

import java.util.ArrayList;

/**
 * Created by PPC16 on 4/10/2017.
 */

public class OpenCloseDaysRecyclerAdapter extends RecyclerView.Adapter<OpenCloseDaysViewHolder> {


    private Context context;
    private ArrayList<OpenCloseShopData> openCloseShopDataArrayList = new ArrayList<>();
    private LayoutInflater inflater;
    private int colorArray[] = {R.color.open_shop_day_color1, R.color.dark_purple, R.color.open_shop_day_color3, R.color.open_shop_day_color4, R.color.open_shop_day_color5, R.color.green_light, R.color.open_shop_day_color7};

    public OpenCloseDaysRecyclerAdapter(Context context, ArrayList<OpenCloseShopData> openCloseShopDataArrayList) {
        this.openCloseShopDataArrayList = openCloseShopDataArrayList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public OpenCloseDaysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OpenCloseDaysViewHolder(inflater.inflate(R.layout.row_open_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(OpenCloseDaysViewHolder holder, final int position) {
        AndroidUtils.setBackgroundSolid(holder.relativeOpenShop, context, colorArray[position], 0, GradientDrawable.RECTANGLE);

        holder.tvDayName.setText(openCloseShopDataArrayList.get(position).dayName);
        holder.tvOpeningTime.setText(new StringBuilder("Opening Time : ").append(openCloseShopDataArrayList.get(position).openingTime));
        holder.tvClosingTime.setText(new StringBuilder("Closing Time : ").append(openCloseShopDataArrayList.get(position).closingTime));
    }

    @Override
    public int getItemCount() {
        return openCloseShopDataArrayList.size();
    }
}
