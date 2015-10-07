package at.renehollander.schnitzeljagd.location;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java8.lang.FunctionalInterface;

public class SensorListener implements SensorEventListener, LocationListener {

    private Location mLocation;
    private Location target;

    public SensorListener(Context context, Location target) {
        this.target = target;

        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor mSensorRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        mSensorManager.registerListener(this, mSensorRotationVector, SensorManager.SENSOR_DELAY_FASTEST);

        for (final String provider : mLocationManager.getProviders(true)) {
            if (LocationManager.GPS_PROVIDER.equals(provider) || LocationManager.PASSIVE_PROVIDER.equals(provider) || LocationManager.NETWORK_PROVIDER.equals(provider)) {
                if (mLocation == null) {
                    mLocation = mLocationManager.getLastKnownLocation(provider);
                }
                mLocationManager.requestLocationUpdates(provider, 0, 100.0f, this);
            }
        }
    }

    public void setTarget(Location target) {
        this.target = target;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float[] rMat = new float[9];
            float[] orientation = new float[3];
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            double azimut = Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]);
            GeomagneticField geoField = new GeomagneticField((float) mLocation.getLatitude(), (float) mLocation.getLongitude(), (float) mLocation.getAltitude(), System.currentTimeMillis());
            azimut += geoField.getDeclination();
            float bearing = mLocation.bearingTo(target);
            float direction = (float) (azimut - bearing);
            if (changeListener != null) {
                changeListener.onChange(direction);
            }
        }
    }

    @FunctionalInterface
    public interface ChangeListener {
        void onChange(float newDirection);
    }

    private ChangeListener changeListener;

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.mLocation = location;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
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
