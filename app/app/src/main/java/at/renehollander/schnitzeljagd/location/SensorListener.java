package at.renehollander.schnitzeljagd.location;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;

import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import java8.lang.FunctionalInterface;

public class SensorListener implements SensorEventListener {

    private Context context;
    private Location target;

    private SensorManager mSensorManager;
    private Sensor mSensorRotationVector;

    public SensorListener(Context context, Location target) {
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
        Schnitzeljagd sj = (Schnitzeljagd) context.getApplicationContext();
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float[] rMat = new float[9];
            float[] orientation = new float[3];
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            double azimut = Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]);
            GeomagneticField geoField = new GeomagneticField((float) sj.getCurrentLocation().getLatitude(), (float) sj.getCurrentLocation().getLongitude(), (float) sj.getCurrentLocation().getAltitude(), System.currentTimeMillis());
            azimut += geoField.getDeclination();
            float bearing = sj.getCurrentLocation().bearingTo(target);
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
