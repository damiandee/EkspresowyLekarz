package praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities;

import android.content.Context;
import android.content.IntentSender;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import android.location.Address;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.GPSTracker;
import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.UserSession;
import praca_inzynierska.damian_deska.ekspresowylekarz.Model.DoctorModel;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;


public class MapsActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback {

    public static final String TAG = MapsActivity.class.getSimpleName();

    double currentLatitude;
    double currentLongitude;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private GPSTracker gpsTracker;

    private static MapsActivity instance = null;

    public static MapsActivity getInstance() {
        if(instance == null) {
            instance = new MapsActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*inicjalizacja trackera gps*/
        gpsTracker = new GPSTracker(getApplicationContext());

        UserSession.getSession().setMapsActivity(this);

        /*inicjalizacja google api clienta, niezbednego do polaczenia z google maps*/
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        /*locationRequest pobiera aktualna pozycje uzytkownika*/
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 * 1000 milisekund = 10sekund
                .setFastestInterval(1 * 1000); // 1 * 1000 milisekund = 10 sekund
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    /*mapa przypisywana do obiektu mapy mMap dopiero po jej pelnym zaladowaniu*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    /*funkcja dodajaca na mapie znaczniki wszystkich znalezionych lekarzy*/
    public void makeDoctorsMarks(Context context) {
        try {
            ArrayList<DoctorModel> foundedDoctorsList= UserSession.getSession().getFoundedDoctorsList();
            for(DoctorModel doctorModel : foundedDoctorsList) {
                Geocoder geocoder = new Geocoder(context);
                /*geocoder pobiera adres na podstawie danych lekarza*/
                List<Address> address = geocoder.getFromLocationName(doctorModel.getDoctorCity() + ", " + doctorModel.getDoctorStreet(), 1);
                /*pobranie dlugosci i szerokosci geograficznej*/
                double doctorLatitude= address.get(0).getLatitude();
                double doctorLongitude= address.get(0).getLongitude();
                /*obliczenie odleglosci pacjenta do lekarza (w kilometrach)*/
                float distanceToDoctor = getDistanceFromCurrentLocation((float)currentLatitude, (float)currentLongitude,
                        (float)doctorLatitude, (float)doctorLongitude)/1000;
                /*zaokraglenie otrzymanego wyniku i zapisanie go*/
                doctorModel.setDoctorDistance(round(distanceToDoctor, 1));
                /*dodanie znacznika do mapy*/
                mMap.addMarker(new MarkerOptions().position(new LatLng(doctorLatitude, doctorLongitude)).title(doctorModel.getDoctorName() +
                        " " + doctorModel.getDoctorSurname()));
                geocoder = null;
                address = null;
            }
        } catch(Exception e) {

        }
    }

    /*usuniecie wszystkich znacznikow i wycentrowanie mapy na lokalizacji uzytkownika*/
    public void clearAllMarks() {
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 12.0f));
    }

    /*funkcja liczaca odleglosc pacjenta do lekarza*/
    public static float getDistanceFromCurrentLocation(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    /*funkcja zaokraglajaca wyliczona odleglosc do 2 miejsc po przecinku*/
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    /*funkcja wyznaczajaca wspolrzedne pacjenta*/
    private void handleNewLocation(Location location) {

        currentLatitude = gpsTracker.getLatitude();
        currentLongitude = gpsTracker.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        /*dodanie znacznika lokalizacji uzytkownika i wycentrowanie na nim widoku mapy*/
        mMap.addMarker(new MarkerOptions().position(latLng).title("Twoja lokalizacja"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }

    /*przy polaczeniu funkcja sprawdza, czy jest juz zapisana ostatnia lokalizacja uzytkownika, jezeli nie, tworzy nowa*/
    @Override
    public void onConnected(Bundle bundle) {
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
            else {
                handleNewLocation(location);
            }
        } catch (SecurityException se) {

        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    /*w przypadku wykrycia bledu funkcja probuje go naprawic*/
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*jezeli blad posiada 'resolution', podejmowana jest proba uruchomienia serwisu Google Play*/
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
            /*w innym przypadku wyswietlany jest komunikat*/
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

}
