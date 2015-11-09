package at.renehollander.schnitzeljagd.application;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.location.Location;

import at.renehollander.schnitzeljagd.network.Connection;
import at.renehollander.schnitzeljagd.network.Station;
import at.renehollander.schnitzeljagd.sensor.CurrentLocationManager;

public class Schnitzeljagd {

    private final Application application;
    private Credentials credentials;
    private Connection connection;
    private Location currentLocation;
    private CurrentLocationManager locationManager;
    private Station currentStation;

    public Schnitzeljagd(Application application) {
        this.application = application;
        this.credentials = new Credentials(getApplication(), "Credentials");
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

    public void getCurrentStation(Activity activity, boolean forceUpdate, Connection.Callback<Station> cb) {
        if (currentStation != null && !forceUpdate) {
            cb.call(currentStation);
        } else {
            ProgressDialog pd = new ProgressDialog(activity);
            pd.setTitle("Station");
            pd.setMessage("Getting current station from Server...");
            pd.show();
            getCurrentStation(forceUpdate, (err, station) -> {
                pd.dismiss();
                if (err != null) {
                    Util.displayErrorDialogFromThrowable(activity, "Error getting station from server", err);
                } else {
                    cb.call(station);
                }
            });
        }
    }

    public void getCurrentStation(boolean forceUpdate, Connection.Callback<Station> cb) {
        if (currentStation == null) {
            getConnection().getCurrentStation((err, station) -> {
                currentStation = station;
                cb.call(err, station);
            });
        } else {
            cb.call(currentStation);
        }
    }

    public Application getApplication() {
        return application;
    }

}
