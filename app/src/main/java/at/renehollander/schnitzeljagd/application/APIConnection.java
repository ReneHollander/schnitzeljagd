package at.renehollander.schnitzeljagd.application;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.UUID;

import at.renehollander.schnitzeljagd.activity.MainActivity;

public class APIConnection {

    private static final String TAG = "APIConnection";
    private static final String API_URL = "http://10.0.0.7:3000/user";

    private Schnitzeljagd schnitzeljagd;
    private Socket socket;

    public APIConnection(Schnitzeljagd schnitzeljagd) {
        this.schnitzeljagd = schnitzeljagd;
    }

    public void connect() {
        try {
            Log.d(TAG, "connecting to socketio at " + API_URL);
            socket = IO.socket(API_URL);
            registerListeners();
            socket.connect();
        } catch (URISyntaxException e) {
            Log.e(TAG, "URISyntaxException", e);
            MainActivity.instance().finish();
            System.exit(1);
        }
    }

    private void registerListeners() {
        socket.on(Socket.EVENT_CONNECT, args -> {
            Log.d(TAG, "Connected");
            JSONObject data = new JSONObject();
            try {
                data.put("uuid", UUID.randomUUID().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("authentication", data.toString());
            socket.on("authenticated", args1 -> Log.d(TAG, "'authenticated'"));
        });
        socket.on(Socket.EVENT_CONNECT_ERROR, args -> Log.d(TAG, "error connecting: " + Arrays.toString(args)));
    }

    public void disconnect() {
        socket.disconnect();
    }

}
