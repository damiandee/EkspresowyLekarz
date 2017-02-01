package praca_inzynierska.damian_deska.ekspresowylekarz.Controller;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;


import android.util.Log;

/**
 * Created by Damian Deska on 2017-01-18.
 */
public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;
    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;
    Location loc;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;

    public GPSTracker(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    private Location getLocation() {

        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            /*sprawdzenie dostepnosci pobrania lokalizacji z GPS*/
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            /*sprawdzenie dostepnosci pobrania lokalizacji na podstawie polaczenia z Internetem*/
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!checkGPS && !checkNetwork) {
            } else {
                this.canGetLocation = true;
                /*proba pobrania lokalizacji na podstawie sieci*/
                if (checkNetwork) {

                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            /*pobrania ostatniej znanej lokalizacji, o ile istnieje*/
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        }

                        if (loc != null) {
                            /*pobranie wspolrzednych pacjenta*/
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    } catch (SecurityException e) {

                    }
                }
            }
            /*pobranie lokalizacji pacjenta poprzez GPS, o ile mozliwe*/
            if (checkGPS) {
                if (loc == null) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (loc != null) {
                                latitude = loc.getLatitude();
                                longitude = loc.getLongitude();
                            }
                        }
                    } catch (SecurityException e) {

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return loc;
    }

    /*pobranie dlugosci geograficznej uzytkownika*/
    public double getLongitude() {
        if (loc != null) {
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    /*pobranie szerokosci geograficznej pacjenta*/
    public double getLatitude() {
        if (loc != null) {
            latitude = loc.getLatitude();
        }
        return latitude;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}