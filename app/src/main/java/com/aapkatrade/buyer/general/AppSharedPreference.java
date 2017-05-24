package com.aapkatrade.buyer.general;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PPC21 on 27-Jan-17.
 */

public class AppSharedPreference extends Application {

    public static String app_pref = "aapkatrade";
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;


    public AppSharedPreference(Context c) {
        this.sharedPreferences = c.getSharedPreferences(app_pref, Activity.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }


    public String getSharedPref(String pref_key, String default_value) {
        return sharedPreferences.getString(pref_key, default_value);
    }

    public int getSharedPrefInt(String pref_ky, int value) {
        return sharedPreferences.getInt(pref_ky, value);
    }


    public String getSharedPref(String pref_key) {
        return sharedPreferences.getString(pref_key, "");
    }

    public int getSharedPrefInt(String pref_key) {
        return sharedPreferences.getInt(pref_key, 0);
    }

    public void setSharedPref(String pref_key, String text) {
        editor.putString(pref_key, text);
        editor.commit();
    }

    public void setSharedPrefInt(String pref_key, int n) {
        editor.putInt(pref_key, n);
        editor.commit();
    }



    public void setSharedPrefBoolean(String pref_key, boolean n) {
        editor.putBoolean(pref_key, n);
        editor.commit();
    }

    public boolean getSharedPrefBoolean(String pref_key) {
        return sharedPreferences.getBoolean(pref_key,false);
    }


}
