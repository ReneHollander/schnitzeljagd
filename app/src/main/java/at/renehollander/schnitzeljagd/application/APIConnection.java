package at.renehollander.schnitzeljagd.application;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import at.renehollander.socketiowrapper.SocketIOW;
import at.renehollander.socketiowrapper.annotations.SubscribeEvent;
import at.renehollander.socketiowrapper.interfaces.Listener;

public class APIConnection implements Listener {

    private static final String TAG = "APIConnection";
    private static final String API_URL = "http://10.0.101.16:3000/user";

    private Schnitzeljagd schnitzeljagd;
    private SocketIOW socket;

    public APIConnection(Schnitzeljagd schnitzeljagd) {
        this.schnitzeljagd = schnitzeljagd;

        socket = new SocketIOW(URI.create(API_URL));
        socket.register(this);

        socket.setReconnectAttemptCallback(() -> {
            System.out.println("reconnect");
        });

        socket.setConnectCallback(() -> {
            Log.d("networking", "connected");
            socket.emit("ping", "");
        });
    }

    public void connect() {
        if (!schnitzeljagd.getTeamCredentials().hasCredentials()) {
            throw new IllegalStateException("no credentials set");
        }

        JSONObject credentials = new JSONObject();
        try {
            credentials.put("name", schnitzeljagd.getTeamCredentials().getName());
            credentials.put("password", schnitzeljagd.getTeamCredentials().getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("networking", "connecting to socketio at " + API_URL);
        socket.connect(credentials);
    }

    @SubscribeEvent(eventName = "pong")
    public void onPong(SocketIOW socketIOW, Throwable error, String data) {
        Log.d("networking", "pong");
    }

    public void disconnect() {
        socket.disconnect();
    }

    public SocketIOW getSocket() {
        return socket;
    }

}
