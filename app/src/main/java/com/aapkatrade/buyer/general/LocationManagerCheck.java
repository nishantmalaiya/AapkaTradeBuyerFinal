package com.aapkatrade.buyer.general;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by PPC17 on 15-Feb-17.
 */

public class LocationManagerCheck {

    LocationManager locationManager;
    Boolean locationServiceBoolean = false;
    int providerType = 0;
    static AlertDialog alert;

    public LocationManagerCheck(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (networkIsEnabled && gpsIsEnabled) {
            locationServiceBoolean = true;
            providerType = 1;

        } else if (!networkIsEnabled && gpsIsEnabled) {
            locationServiceBoolean = true;
            providerType = 2;

        } else if (networkIsEnabled && gpsIsEnabled != true) {
            locationServiceBoolean = true;
            providerType = 1;
        }

    }

    public Boolean isLocationServiceAvailable() {
        return locationServiceBoolean;
    }

    public int getProviderType() {
        return providerType;
    }

    public void createLocationServiceError(final Activity activityObj) {

        // show alert dialog if Internet is not connected
        AlertDialog.Builder builder = new AlertDialog.Builder(activityObj);

        builder.setMessage(
                "You need to activate location service to use this feature. Please turn on network or GPS mode in location settings")
                .setTitle("!Location Not Found")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                activityObj. startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);

//                                Intent intent = new Intent(
//                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                //activityObj.startActivity(intent);
                                alert.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alert.dismiss();
                            }
                        });
        alert = builder.create();
        alert.show();
    }









}
