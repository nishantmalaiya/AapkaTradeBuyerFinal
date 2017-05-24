package com.aapkatrade.buyer.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.interfaces.CommonInterface;
import com.aapkatrade.buyer.location.AddressEnum;
import com.aapkatrade.buyer.location.GeoCoderAddress;
import com.google.android.gms.maps.model.LatLng;

public class LocationService extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    public static CommonInterface commonInterface;
    Intent intent;
    int counter = 0;
    AppSharedPreference appSharedpreference;
    private GeoCoderAddress geoCoderAddressAsync;
    private String AddressAsync;


    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        appSharedpreference = new AppSharedPreference(LocationService.this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_NOT_STICKY;
        }
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        return START_NOT_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }


    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {

            if (isBetterLocation(loc, previousBestLocation)) {
                LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());


                geoCoderAddressAsync = new GeoCoderAddress(LocationService.this, loc.getLatitude(), loc.getLongitude());

                try {


                    AddressAsync = geoCoderAddressAsync.get_state_name().get(AddressEnum.STATE);
                    Log.e("AddressAsync", AddressAsync);
                } catch (Exception e) {
                    Log.e("Exception_geocoder", e.toString());

                }

                appSharedpreference.setSharedPref(SharedPreferenceConstants.CURRENT_LATTITUDE.toString(), String.valueOf(loc.getLatitude()));
                appSharedpreference.setSharedPref(SharedPreferenceConstants.CURRENT_LONGITUDE.toString(), String.valueOf(loc.getLongitude()));
                appSharedpreference.setSharedPref(SharedPreferenceConstants.CURRENT_STATE_NAME.toString(), AddressAsync);
                intent.putExtra("Latitude", "" + loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                Log.e("****", "Location changed1" + loc.getLatitude());
                Log.e("****", "Location changed2" + loc.getLongitude());
                sendBroadcast(intent);
                stopSelf();

            }
        }

        public void onProviderDisabled(String provider) {

        }


        public void onProviderEnabled(String provider) {

        }


        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }


}
