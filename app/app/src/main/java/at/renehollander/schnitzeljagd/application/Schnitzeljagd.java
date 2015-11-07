package at.renehollander.schnitzeljagd.application;

import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.util.Date;

import at.renehollander.schnitzeljagd.activity.Activities;
import at.renehollander.schnitzeljagd.location.LocationChangeListener;
import at.renehollander.schnitzeljagd.network.Connection;

public class Schnitzeljagd extends Application {

    public static final String LOC_USED_LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;

    private static final int LOC_MIN_UPDATE_TIME = 1000 * 5;
    private static final int LOC_MIN_LOC_DIFFERENCE = 0;

    private Credentials credentials;
    private Connection connection;
    private Location currentLocation;

    @Override
    public void onCreate() {
        super.onCreate();

        this.credentials = new Credentials(this, "Credentials");

        this.connection = new Connection(this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.i("location", "provider=" + Schnitzeljagd.LOC_USED_LOCATION_PROVIDER + ", enabled=" + locationManager.isProviderEnabled(Schnitzeljagd.LOC_USED_LOCATION_PROVIDER));
        locationManager.requestLocationUpdates(Schnitzeljagd.LOC_USED_LOCATION_PROVIDER, LOC_MIN_UPDATE_TIME, LOC_MIN_LOC_DIFFERENCE, new LocationChangeListener(this));
        Location location = locationManager.getLastKnownLocation(Schnitzeljagd.LOC_USED_LOCATION_PROVIDER);
        this.updateLocation(location);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.getConnection().disconnect();
    }

    public Connection getConnection() {
        return connection;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void updateLocation(Location loc) {
        this.currentLocation = loc;
        Log.i("location", String.valueOf(loc));
        Log.i("location", "Bearing: " + loc.getBearing());

        // TODO implement
    }

    public static Schnitzeljagd instance() {
        return (Schnitzeljagd) Activities.MAIN.getApplication();
    }
}
