package at.renehollander.schnitzeljagd.application;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;

public class APIConnection {

    public static final String SOCKET_AUTHENTICATED = "authenticated";
    public static final String SOCKET_UNAUTHORIZED = "unauthorized";
    public static final String SOCKET_AUTHENTICATION = "authentication";

    private static final String API_URL = "http://10.0.0.3:3000/user";

    private Schnitzeljagd schnitzeljagd;
    private Socket socket;

    public APIConnection(Schnitzeljagd schnitzeljagd) {
        this.schnitzeljagd = schnitzeljagd;

        socket = IO.socket(URI.create(API_URL));

        socket.on(Socket.EVENT_CONNECT, (objects) -> {
            JSONObject credentials = new JSONObject();
            try {
                credentials.put("name", schnitzeljagd.getTeamCredentials().getName());
                credentials.put("password", schnitzeljagd.getTeamCredentials().getPassword());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit(SOCKET_AUTHENTICATION, credentials);
        });
    }

    public void connect() {
        if (!schnitzeljagd.getTeamCredentials().hasCredentials()) {
            throw new IllegalStateException("no credentials set");
        }
        Log.d("networking", "connecting to socketio at " + API_URL);
        socket.connect();
    }

    public void disconnect() {
        socket.disconnect();
    }

    public Socket getSocket() {
        return socket;
    }

}
