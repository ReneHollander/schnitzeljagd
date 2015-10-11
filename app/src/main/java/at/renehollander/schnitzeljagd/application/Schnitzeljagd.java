package at.renehollander.schnitzeljagd.application;

import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.util.Date;

import at.renehollander.schnitzeljagd.location.LocationChangeListener;

public class Schnitzeljagd extends Application {

    public static final String LOC_USED_LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;

    private static final int LOC_MIN_UPDATE_TIME = 1000 * 5;
    private static final int LOC_MIN_LOC_DIFFERENCE = 0;

    private Credentials teamCredentials;

    //private Station currentstation;

    private APIConnection apiConnection;
    private Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();

        this.teamCredentials = new Credentials(this, "Credentials");

        this.apiConnection = new APIConnection(this);
        if (this.getTeamCredentials().hasCredentials()) {
            this.apiConnection.connect();
        }


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

    public Credentials getTeamCredentials() {
        return teamCredentials;
    }

    /*
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
    */

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
                    fragmentManager.beginTransaction().replace(R.id.container, Fragments.CONTENT).commit();
                }
                if (pd != null) {
                    pd.dismiss();
                }
            }

            @Override
            public void onError(ErrorType errorType, int code, String msg) {
                Log.e("QR", errorType + ", " + code + ", " + msg);

                if (fragmentManager != null) {
                    fragmentManager.beginTransaction().replace(R.id.container, Fragments.CONTENT).commit();
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
                    fragmentManager.beginTransaction().replace(R.id.container, Fragments.CONTENT).commit();
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
