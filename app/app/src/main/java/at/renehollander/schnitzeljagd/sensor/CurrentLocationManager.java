package at.renehollander.schnitzeljagd.sensor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import at.renehollander.schnitzeljagd.application.Schnitzeljagd;

public class CurrentLocationManager implements android.location.LocationListener {

    private static final String LOC_USED_LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    private static final int LOC_MIN_UPDATE_TIME = 1000 * 5;
    private static final int LOC_MIN_LOC_DIFFERENCE = 0;

    private final Schnitzeljagd schnitzeljagd;
    private final LocationManager locationManager;

    private Location location;

    public CurrentLocationManager(Schnitzeljagd schnitzeljagd) {
        this.schnitzeljagd = schnitzeljagd;

        locationManager = (android.location.LocationManager) getSchnitzeljagd().getActivity().getSystemService(Context.LOCATION_SERVICE);
        Log.i("location", "provider=" + LOC_USED_LOCATION_PROVIDER + ", enabled=" + locationManager.isProviderEnabled(LOC_USED_LOCATION_PROVIDER));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getSchnitzeljagd().getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getSchnitzeljagd().getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        locationManager.requestLocationUpdates(LOC_USED_LOCATION_PROVIDER, LOC_MIN_UPDATE_TIME, LOC_MIN_LOC_DIFFERENCE, this);
        this.updateLocation(getLastKnownLocation());
    }

    public Schnitzeljagd getSchnitzeljagd() {
        return schnitzeljagd;
    }

    private void updateLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getSchnitzeljagd().getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getSchnitzeljagd().getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.updateLocation(location);
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
