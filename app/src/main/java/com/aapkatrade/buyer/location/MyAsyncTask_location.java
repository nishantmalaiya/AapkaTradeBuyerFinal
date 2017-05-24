package com.aapkatrade.buyer.location;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by PPC17 on 24-Mar-17.
 */

public class MyAsyncTask_location extends AsyncTask<Void, Void, Void>  {
    Dialog progress;
    private String providerAsync;
    int clicked_item = 0;
    private LocationManager locationManagerAsync;

    String thikanaAsync = "Scan sms for location";


    GeoCoderAddress geoCoderAddressAsync;


    String AddressAsync;
    private Context ContextAsync;

    // Flag for GPS status
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;

    Location location; // Location
    public  double latitude; // Latitude
    public  double longitude; // Longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    ProgressBarHandler progressBarHandler;

    String class_name;


    public MyAsyncTask_location(Context context, String class_name) {
        this.ContextAsync = context;
        this.class_name = class_name;
        progressBarHandler = new ProgressBarHandler(ContextAsync);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBarHandler.show();
        // progress = ProgressDialog.show(ContextAsync, "Loading data", "Please wait...");

    }


    @Override
    protected Void doInBackground(Void... arg0) {
        if (Looper.myLooper() == null) {
            Looper.prepare();

        }

        locationManager = (LocationManager) ContextAsync
                .getSystemService(LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {



            Log.e("Network", "no network found");

            // no network provider is enabled
        } else {
            this.canGetLocation = true;
            // First get location from Network Provider
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(ContextAsync, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ContextAsync, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return null;
                }

                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(true);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                String provider = locationManager.getBestProvider(criteria, true);
                Log.e("provider", provider);


                locationManager.requestLocationUpdates(
                        provider,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {

                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    Log.e("latitude2_network",""+latitude);
                                    Log.e("longitude2_network",""+longitude);


                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                                Log.e("latitude_status_changes",provider+"***"+status+"***"+extras.toString());

                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                                Log.e("onProviderEnabled",""+provider);

                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                                Log.e("onProviderDisabled",""+provider);

                            }
                        });

//                       


            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (location == null) {

                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setAltitudeRequired(false);
                    criteria.setBearingRequired(false);
                    criteria.setCostAllowed(true);
                    criteria.setPowerRequirement(Criteria.POWER_LOW);
                    String provider = locationManager.getBestProvider(criteria, true);
                    Log.e("provider", provider);


                    locationManager.requestLocationUpdates(
                            provider,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {

                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                        Log.e("latitude2_gps",""+latitude);
                                        Log.e("longitude2_gps",""+longitude);



                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {
                                    Log.e("latitude_status_changes",provider+"***"+status+"***"+extras.toString());
                                }

                                @Override
                                public void onProviderEnabled(String provider) {
                                    Log.e("onProviderEnabled",""+provider);
                                }

                                @Override
                                public void onProviderDisabled(String provider) {
                                    Log.e("onProviderDisabled",""+provider);

                                }
                            });
                    Log.e("GPS Enabled", "GPS Enabled");

                } else {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }


            if (latitude != 0.0 && longitude != 0.0) {


                Log.e("latlng_asynctask", latitude + "**" + longitude + "");
                List<Address> addresses = null;
                geoCoderAddressAsync = new GeoCoderAddress(ContextAsync, latitude, longitude);

                try {


                    AddressAsync = geoCoderAddressAsync.get_state_name().get(AddressEnum.STATE);
                    Log.e("AddressAsync", AddressAsync);
                } catch (Exception e) {
                    Log.e("Exception_geocoder", e.toString());

                }
            } else {
               // AddressAsync = "Haryana";
            }


        }





        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        super.onPostExecute(result);
        progressBarHandler.hide();

        Goto_search(class_name);




    }

    private void Goto_search(String class_name) {

        if (class_name.contains("homeactivity")) {

//            Intent intentAsync = new Intent(ContextAsync, Search.class);
//            intentAsync.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intentAsync.putExtra("classname", "homeactivity");
//            intentAsync.putExtra("state_name", AddressAsync);
//            intentAsync.putExtra("latitude",latitude);
//            intentAsync.putExtra("longitude",longitude);
//            ContextAsync.startActivity(intentAsync);
//
//            ((Activity) ContextAsync).finish();


        } else {
            Log.e("class not found", "class not found");
        }

    }


//    @Override
//    public void onLocationChanged(Location location) {
//        // TODO Auto-generated method stub
//        if (ActivityCompat.checkSelfPermission(ContextAsync, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ContextAsync, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//
//            return;
//        }
//        //locationManagerAsync.requestLocationUpdates(location.getProvider(), 0, 0, this);
//
//
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        Log.e("latitude2 onProviderDisabled",""+latitude+"error");
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//        // TODO Auto-generated method stub
//
//    }
}