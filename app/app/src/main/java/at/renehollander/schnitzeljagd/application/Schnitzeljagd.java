package at.renehollander.schnitzeljagd.application;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import at.renehollander.schnitzeljagd.network.Connection;
import at.renehollander.schnitzeljagd.sensor.CurrentLocationManager;

public class Schnitzeljagd {

    private final Activity activity;

    private Credentials credentials;
    private Connection connection;
    private Location currentLocation;
    private CurrentLocationManager locationManager;

    public Activity getActivity() {
        return activity;
    }

    public Schnitzeljagd(Activity activity) {
        this.activity = activity;
        this.credentials = new Credentials(getActivity(), "Credentials");
        this.connection = new Connection(this);
        this.locationManager = new CurrentLocationManager(this);
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
        if (loc == null)
            return;

        this.currentLocation = loc;
        Log.i("location", String.valueOf(loc));
        Log.i("location", "Bearing: " + loc.getBearing());

        // TODO implement
    }

    public void destroy() {
        this.getConnection().disconnect();
    }

}
