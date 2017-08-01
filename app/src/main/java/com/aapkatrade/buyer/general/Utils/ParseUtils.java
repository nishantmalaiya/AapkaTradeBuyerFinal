package com.aapkatrade.buyer.general.Utils;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.aapkatrade.buyer.filter.entity.FilterObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by PPC15 on 10-04-2017.
 */

public class ParseUtils {
    public static HashMap<String, String> mapToJsonString(ArrayMap arrayMap) {
        HashMap<String, String> map = new HashMap<>();
        for (Object obj : arrayMap.keySet()) {
            String key = (String) obj;
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<FilterObject> tempArrayList = (ArrayList<FilterObject>) arrayMap.get(key);
            for (int i = 0; i < tempArrayList.size(); i++) {
                FilterObject filterObject = tempArrayList.get(i);
                arrayList.add(filterObject.name.value.toString().replaceAll("\\\"", "\""));
//                arrayList.add(filterObject.name.key.toString()+" : "+filterObject.name.value.toString());
            }
            map.put(key, new Gson().toJson(arrayList));

            Log.e("parsing", "OOOOOOOOOOOO@@@!!!        " + map.get(key));
        }
        return map;
    }

    public static String mapToJson(HashMap<String, String> hashMap) {
        return new Gson().toJson(hashMap)/*.replaceAll("\\\\\"", "\"")*/;
    }

    public static JSONObject stringArrayToJsonObject(String[] strings) {
        int index = 0;
        JSONObject jsonObject = new JSONObject();
        try {
            for (String s : strings) {
                jsonObject.put(String.valueOf(index), s);
                index++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String ArrayListToJsonObject(ArrayList arrayList) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < arrayList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("machine", arrayList.get(i));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }
    public static String ShippingChargeArrayListToJsonObject(ArrayList arrayList) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < arrayList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("shipping_charge", arrayList.get(i));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }



}
