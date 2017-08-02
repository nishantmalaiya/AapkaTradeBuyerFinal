package com.aapkatrade.buyer.shopdetail.reviewlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by PPC16 on 4/8/2017.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater inflater;
    private List<ReviewListData> itemList;
    private Context context;
    private ReviewListHolder viewHolder;
    private AppSharedPreference appSharedPreference;
    private ProgressBarHandler progress_handler;


    public ReviewListAdapter(Context context, List<ReviewListData> itemList) {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);

        appSharedPreference = new AppSharedPreference(context);
        progress_handler = new ProgressBarHandler(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_review_list, parent, false);

        viewHolder = new ReviewListHolder(view);

        AndroidUtils.setImageColor(viewHolder.imgStar, context, R.color.white);

        System.out.println("data-----------" + itemList);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ReviewListHolder homeHolder = (ReviewListHolder) holder;

        Log.e("size----------", String.valueOf(itemList.size()));

        homeHolder.title.setText(itemList.get(position).title);

        homeHolder.message_description.setText(itemList.get(position).message_description);

        homeHolder.username.setText(itemList.get(position).username);

        homeHolder.tvRating.setText(itemList.get(position).rating);

        if(Integer.valueOf(itemList.get(position).rating)== 5){
            AndroidUtils.setBackgroundSolid(homeHolder.relativeRating, context, R.color.color_progress1, 10, GradientDrawable.RECTANGLE);
        } else if(Integer.valueOf(itemList.get(position).rating)==4){
            AndroidUtils.setBackgroundSolid(homeHolder.relativeRating, context, R.color.color_progress2, 10, GradientDrawable.RECTANGLE);
        } else if(Integer.valueOf(itemList.get(position).rating)==3){
            AndroidUtils.setBackgroundSolid(homeHolder.relativeRating, context, R.color.color_progress3, 10, GradientDrawable.RECTANGLE);
        } else if(Integer.valueOf(itemList.get(position).rating)==2){
            AndroidUtils.setBackgroundSolid(homeHolder.relativeRating, context, R.color.color_progress4, 10, GradientDrawable.RECTANGLE);
        } else if(Integer.valueOf(itemList.get(position).rating)==1){
            AndroidUtils.setBackgroundSolid(homeHolder.relativeRating, context, R.color.color_progress5, 10, GradientDrawable.RECTANGLE);
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = null;

        try {
            date = form.parse(itemList.get(position).created_date);

        } catch (ParseException e) {

            e.printStackTrace();
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy");
        String newDateStr = postFormater.format(date);

        homeHolder.deliver_date.setText(newDateStr);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}