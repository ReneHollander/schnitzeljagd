package at.renehollander.schnitzeljagd.application;

import android.util.Log;

import java.net.URL;
import java.util.UUID;

public class Static {

    public static final String SERVER = "server.rene8888.at:3000";

    public static URL currentStationUrl(UUID teamkey) {
        try {
            return new URL("http://" + SERVER + "/team/" + teamkey.toString() + "/currentstation/");
        } catch (Exception e) {
            Log.e("URI", "Error generating URI", e);
            return null;
        }
    }

    public static URL startUrl(UUID teamkey) {
        try {
            return new URL("http://" + SERVER + "/team/" + teamkey.toString() + "/start/");
        } catch (Exception e) {
            Log.e("URI", "Error generating URI", e);
            return null;
        }
    }

    public static URL submitUrl(UUID teamkey) {
        try {
            return new URL("http://" + SERVER + "/team/" + teamkey.toString() + "/submit/");
        } catch (Exception e) {
            Log.e("URI", "Error generating URI", e);
            return null;
        }
    }

    public static URL updateCoordsUrl(UUID teamkey) {
        try {
            return new URL("http://" + SERVER + "/team/" + teamkey.toString() + "/updatecoords/");
        } catch (Exception e) {
            Log.e("URI", "Error generating URI", e);
            return null;
        }
    }

}
