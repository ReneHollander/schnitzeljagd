package at.renehollander.schnitzeljagd.sensor;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;

import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import java8.lang.FunctionalInterface;

public class CompassSensor implements SensorEventListener {

    private final Schnitzeljagd schnitzeljagd;
    private Context context;
    private Location target;

    private SensorManager mSensorManager;
    private Sensor mSensorRotationVector;

    public CompassSensor(Schnitzeljagd schnitzeljagd, Context context, Location target) {
        this.schnitzeljagd = schnitzeljagd;
        this.context = context;
        this.target = target;

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    public void start() {
        mSensorManager.registerListener(this, mSensorRotationVector, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    public void setTarget(Location target) {
        this.target = target;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Location loc = schnitzeljagd.getLocationManager().getLocation();
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float[] rMat = new float[9];
            float[] orientation = new float[3];
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            double azimut = Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]);
            GeomagneticField geoField = new GeomagneticField((float) loc.getLatitude(), (float) loc.getLongitude(), (float) loc.getAltitude(), System.currentTimeMillis());
            azimut += geoField.getDeclination();
            float bearing = schnitzeljagd.getLocationManager().getLocation().bearingTo(target);
            float direction = (float) (azimut - bearing);
            //float direction = (float) azimut;
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
