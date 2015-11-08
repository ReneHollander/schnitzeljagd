package at.renehollander.schnitzeljagd.application;

import android.app.Activity;
import android.location.Location;

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

    public CurrentLocationManager getLocationManager() {
        return locationManager;
    }

    public void destroy() {
        this.getConnection().disconnect();
    }

}
