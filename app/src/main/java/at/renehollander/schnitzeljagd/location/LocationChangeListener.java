package at.renehollander.schnitzeljagd.location;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import at.renehollander.schnitzeljagd.application.Schnitzeljagd;

public class LocationChangeListener implements android.location.LocationListener {

    private Schnitzeljagd sj;

    public LocationChangeListener(Schnitzeljagd sj) {
        this.sj = sj;
    }

    @Override
    public void onLocationChanged(Location location) {
        sj.updateLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
