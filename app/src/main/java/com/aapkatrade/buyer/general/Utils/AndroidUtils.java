package com.aapkatrade.buyer.general.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.home.navigation.entity.Category;
import com.aapkatrade.buyer.uicomponent.CustomSnackBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Created by PPC09 on 03-Feb-17.
 */

public class AndroidUtils {

    public static String BaseUrl = "https://aapkatrade.com";

    public static void showSnackBar(ViewGroup layout, String message) {
        CustomSnackBar snackbar = CustomSnackBar.Builder(layout.getContext())
                .layout(R.layout.my_toast)
                .background(R.color.green)
                .duration(CustomSnackBar.LENGTH.SHORT)
                .swipe(true)
                .build(layout);
        snackbar.setText(message);
        snackbar.show();
    }


    public static String getEditTextData(EditText et) {
        if (Validation.validateEdittext(et)) {
            return et.getText().toString();
        }
        return "";
    }

    public static String getUserType(String user_type) {
        if (user_type.equals("1")) {
            return "2";
        } else if (user_type.equals("2")) {
            return "1";
        }
        return user_type;
    }

    public static Calendar stringToCalender(String date_yyyy_mm_dd) {
        int day = Integer.parseInt(date_yyyy_mm_dd.split("-")[2]);
        int month = Integer.parseInt(date_yyyy_mm_dd.split("-")[1]);
        int year = Integer.parseInt(date_yyyy_mm_dd.split("-")[0]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar;
    }

    public static void setBackgroundSolid(View layout, Context context, int bgColor, int cornerRadius, int oval) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(cornerRadius);
        shape.setColor(ContextCompat.getColor(context, bgColor));
        shape.setShape(oval);
        layout.setBackground(shape);
    }

    public static void setBackgroundSolidEachRadius(View layout, Context context, int bgColor, int leftTopRadius, int rightTopRadius, int rightBottomRadius, int leftBottomRadius, int oval) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadii(new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius, rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius});
        shape.setColor(ContextCompat.getColor(context, bgColor));
        shape.setShape(oval);
        layout.setBackground(shape);
    }

    public static void setBackgroundSolid(View layout, Context context, int bgColor, int cornerRadius, int viewShape, int border_width, int border_color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(cornerRadius);
        shape.setStroke(border_width, ContextCompat.getColor(context, border_color));
        shape.setColor(ContextCompat.getColor(context, bgColor));
        shape.setShape(viewShape);
        layout.setBackground(shape);
    }


    public static void setBackgroundStroke(View layout, Context context, int bgColor, int cornerRadius, int strokeWidth) {
        GradientDrawable shape = new GradientDrawable();
        shape.setStroke(strokeWidth, ContextCompat.getColor(context, bgColor));
        shape.setCornerRadius(cornerRadius);
        layout.setBackground(shape);
    }


    public static void setBackgroundStrokeEachRadius(View layout, Context context, int bgColor, int leftTopRadius, int rightTopRadius, int rightBottomRadius, int leftBottomRadius, int strokeWidth) {
        GradientDrawable shape = new GradientDrawable();
        shape.setStroke(strokeWidth, ContextCompat.getColor(context, bgColor));
        shape.setCornerRadii(new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius, rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius});
        layout.setBackground(shape);
    }


    public static void setBackgroundStroke(View layout, Context context, int bgColor, int cornerRadius, int strokeWidth, int backgroundcolor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setStroke(strokeWidth, ContextCompat.getColor(context, bgColor));
        shape.setCornerRadius(cornerRadius);
        layout.setBackground(shape);
    }


    public static void setImageColor(ImageView imageView, Context context, int color) {
        if (imageView != null)
            imageView.setColorFilter(ContextCompat.getColor(context, color));
    }

    public static int screenHeight(Context ctx) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static int screenWidth(Context ctx) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static String getTag(Context context) {
        return context == null ? "" : context.getClass().getSimpleName();
    }

    public static Drawable setImageColor(Context context, int imageDrawable, int color) {
        Drawable mDrawable = ContextCompat.getDrawable(context, imageDrawable);
        mDrawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        return mDrawable;

    }

    public static void showErrorLog(Context context, Object message) {
        Log.e(getTag(context), message.toString());
    }


    public static void showErrorLog(Context context, Object addIdentifierText, Object message) {
        Log.e(getTag(context) + addIdentifierText.toString(), message.toString());
    }


    public static void sortArrayList(ArrayList<Category> arrayList) {

    }

    public static int convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / (metrics.densityDpi / 160f));
        return dp;
    }

    public static void setGradientColor(View layout, int viewShape, int startColor, int endColor, GradientDrawable.Orientation orientation, int cornerRadius) {
        GradientDrawable gradient = new GradientDrawable(orientation, new int[]{startColor,endColor});
        gradient.setShape(viewShape);
        gradient.setCornerRadius(cornerRadius);
        layout.setBackground(gradient);
    }

    public static void showToast(Context context, String message){
        View v = ((Activity)context).getLayoutInflater().inflate(R.layout.my_toast, (ViewGroup) (((Activity) context).findViewById(R.id.myToast)), false);
        LinearLayout myToastLinearLayout = (LinearLayout) v.findViewById(R.id.myToast);
        setGradientColor(myToastLinearLayout, GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.toast_end_color), ContextCompat.getColor(context, R.color.toast_start_color), GradientDrawable.Orientation.TOP_BOTTOM, 50);
        TextView textView = (TextView) v.findViewById(R.id.msg);
        textView.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
        toast.setView(v);
        toast.show();
    }




}
