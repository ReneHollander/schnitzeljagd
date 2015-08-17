package at.renehollander.schnitzeljagd.objects.misc;

public class UpdateCoords {

    private double latitude;
    private double longitude;
    private double altitude;
    private float accuracy;
    private float velocity;
    private float bearing;
    private long time;

    public UpdateCoords(double latitude, double longitude, double altitude, float accuracy, float velocity, float bearing, long time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.velocity = velocity;
        this.bearing = bearing;
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public float getVelocity() {
        return velocity;
    }

    public float getBearing() {
        return bearing;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "UpdateCoords{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", accuracy=" + accuracy +
                ", velocity=" + velocity +
                ", bearing=" + bearing +
                ", time=" + time +
                '}';
    }
}
