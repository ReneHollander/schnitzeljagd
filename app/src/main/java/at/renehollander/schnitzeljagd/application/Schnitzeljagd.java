package at.renehollander.schnitzeljagd.application;

import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.UUID;

import at.renehollander.schnitzeljagd.location.LocationChangeListener;
import at.renehollander.schnitzeljagd.location.SensorListener;
import at.renehollander.schnitzeljagd.objects.station.Station;
import at.renehollander.schnitzeljagd.objects.station.StationDeserializer;
import at.renehollander.schnitzeljagd.objects.submit.request.SubmitRequest;
import at.renehollander.schnitzeljagd.objects.submit.request.SubmitRequestSerializer;
import at.renehollander.schnitzeljagd.objects.submit.response.SubmitResponse;
import at.renehollander.schnitzeljagd.objects.submit.response.SubmitResponseDeserializer;

public class Schnitzeljagd extends Application {

    public static final String LOC_USED_LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;

    //private static final int LOC_MIN_UPDATE_TIME = 1000 * 60;
    //private static final int LOC_MIN_LOC_DIFFERENCE = 250;

    private static final int LOC_MIN_UPDATE_TIME = 1000 * 5;
    private static final int LOC_MIN_LOC_DIFFERENCE = 0;

    private static final String PREFS_NAME = "SchnitzeljagdPrefs";
    private static final String TEAM_KEY_PREF = "teamKey";
    private static final String STATION_FILE = "station.ser";

    private UUID teamKey;
    private Station currentstation;

    private SharedPreferences settings;

    private LocationManager locationManager;

    private APIConnection apiConnection;
    private Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();

        this.apiConnection = new APIConnection(this);

        this.settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.contains(TEAM_KEY_PREF)) {
            this.teamKey = UUID.fromString(settings.getString(TEAM_KEY_PREF, null));
        } else {
            this.teamKey = null;
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Station.class, new StationDeserializer());
        gsonBuilder.registerTypeAdapter(SubmitResponse.class, new SubmitResponseDeserializer());
        gsonBuilder.registerTypeAdapter(SubmitRequest.class, new SubmitRequestSerializer());
        this.gson = gsonBuilder.create();

        this.apiConnection.connect();

        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.i("location", "provider=" + Schnitzeljagd.LOC_USED_LOCATION_PROVIDER + ", enabled=" + locationManager.isProviderEnabled(Schnitzeljagd.LOC_USED_LOCATION_PROVIDER));
        locationManager.requestLocationUpdates(Schnitzeljagd.LOC_USED_LOCATION_PROVIDER, LOC_MIN_UPDATE_TIME, LOC_MIN_LOC_DIFFERENCE, new LocationChangeListener(this));
        Location location = locationManager.getLastKnownLocation(Schnitzeljagd.LOC_USED_LOCATION_PROVIDER);
        this.updateLocation(location);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.getApiConnection().disconnect();
    }

    public Gson getGson() {
        return gson;
    }

    public APIConnection getApiConnection() {
        return apiConnection;
    }

    public void setTeamKey(UUID teamKey) {
        this.teamKey = teamKey;
        this.settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(TEAM_KEY_PREF, teamKey.toString());
        editor.commit();

        Location location = locationManager.getLastKnownLocation(Schnitzeljagd.LOC_USED_LOCATION_PROVIDER);
        this.updateLocation(location);
    }

    public UUID getTeamKey() {
        return this.teamKey;
    }


    public void setCurrentStation(Station station) {
        try {
            this.currentstation = station;
            FileOutputStream stationFileOutput = openFileOutput(STATION_FILE, MODE_PRIVATE);
            ObjectOutputStream stationObjectOutput = new ObjectOutputStream(stationFileOutput);
            stationObjectOutput.writeObject(this.currentstation);
            stationObjectOutput.close();
            stationFileOutput.close();
        } catch (Exception e) {
            Log.e("Schnitzeljagd", "Error saving station to disk", e);
        }
    }

    public Station getCurrentStation() {
        return this.currentstation;
    }

    public String getCurrentStationContent() {
        if (this.teamKey == null || this.currentstation == null) {
            return "<html><body><h1>Please scan your teamkey!</h1></body></html>";
        } else {
            return "<html><body><h2>" + this.currentstation.getName() + "</h2>" + this.currentstation.getContent() + "</body></html>";
        }
    }

    private boolean fileExistance(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }


    public void updateCurrentStation(final Activity activity, final FragmentManager fragmentManager, final ProgressDialog pd) {
        /*
        this.getRestApiClient().get(Static.currentStationUrl(this.getTeamKey()), Station.class, new RestApiResponseCallback<Station>() {
            @Override
            public void onSuccess(Station object) {
                Schnitzeljagd.this.setCurrentStation(object);
                if (fragmentManager != null) {
                    fragmentManager.beginTransaction().replace(R.id.container, Fragments.CONTENT_FRAGMENT).commit();
                }
                if (pd != null) {
                    pd.dismiss();
                }
            }

            @Override
            public void onError(ErrorType errorType, int code, String msg) {
                Log.e("QR", errorType + ", " + code + ", " + msg);

                if (fragmentManager != null) {
                    fragmentManager.beginTransaction().replace(R.id.container, Fragments.CONTENT_FRAGMENT).commit();
                }
                if (pd != null) {
                    pd.dismiss();
                }
                if (activity != null) {
                    Util.displayErrorDialogFromJson(activity, Util.ERROR_UPDATING_CURRENTSTATION, msg);
                }
            }

            @Override
            public void onException(Exception e) {
                Log.e("QR", "error updating current station", e);

                if (fragmentManager != null) {
                    fragmentManager.beginTransaction().replace(R.id.container, Fragments.CONTENT_FRAGMENT).commit();
                }
                if (pd != null) {
                    pd.dismiss();
                }
                if (activity != null) {
                    Util.displayErrorDialogFromThrowable(activity, Util.EXCEPTION_UPDATING_CURRENTSTATION, e);
                }
            }

            @Override
            public void done() {
            }
        });
        */
    }

    private Location currentLocation;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void updateLocation(Location loc) {
        this.currentLocation = loc;
        Log.i("location", String.valueOf(loc));
        Log.i("location", "Bearing: " + loc.getBearing());
        Location loc2 = new Location("dummyprovider");
        loc2.setLatitude(48.336273);
        loc2.setLongitude(16.297766);
        loc2.setTime(new Date().getTime());

        System.out.println(loc.bearingTo(loc2));

        /*
        if (this.teamKey != null) {
            if (loc != null) {
                Log.d("location update", "current location: " + loc.toString());
                UpdateCoords updateCoords = new UpdateCoords(loc.getLatitude(), loc.getLongitude(), loc.getAltitude(), loc.getAccuracy(), loc.getSpeed(), loc.getBearing(), loc.getTime());
                this.getRestApiClient().post(Static.updateCoordsUrl(this.getTeamKey()), UpdateCoords.class, updateCoords, SubmitResponse.class, new RestApiResponseCallback<SubmitResponse>() {
                    @Override
                    public void onSuccess(SubmitResponse object) {
                        Log.d("location update", "sucess");
                    }

                    @Override
                    public void onError(ErrorType errorType, int code, String msg) {
                        Log.e("QR", errorType + ", " + code + ", " + msg);
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.e("QR", "error updating location", e);
                    }

                    @Override
                    public void done() {
                    }
                });
            } else {
                Log.e("location update", "location was null");
            }
        }
    */
    }
}
