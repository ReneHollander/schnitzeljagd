package at.renehollander.schnitzeljagd.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import at.renehollander.schnitzeljagd.activity.Activities;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import io.socket.client.IO;
import io.socket.client.Socket;
import java8.lang.FunctionalInterface;

public class Connection {

    public static final String SOCKET_AUTHENTICATED = "authenticated";
    public static final String SOCKET_UNAUTHORIZED = "unauthorized";
    public static final String SOCKET_AUTHENTICATION = "authentication";

    private static final String API_URL = "http://10.0.0.76:3000/user";

    private Schnitzeljagd schnitzeljagd;
    private Socket socket;

    public Connection(Schnitzeljagd schnitzeljagd) {
        this.schnitzeljagd = schnitzeljagd;

        socket = IO.socket(URI.create(API_URL));

        socket.on(Socket.EVENT_CONNECT, (objects) -> {
            JSONObject credentials = new JSONObject();
            try {
                credentials.put("email", schnitzeljagd.getCredentials().getEmail());
                credentials.put("password", schnitzeljagd.getCredentials().getPassword());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit(SOCKET_AUTHENTICATION, credentials);
        });
        // TODO on disconnect go to login fragment and display loader
    }

    public void connect() {
        if (!schnitzeljagd.getCredentials().hasCredentials()) {
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

    public static Connection instance() {
        return Schnitzeljagd.instance().getConnection();
    }

    @FunctionalInterface
    public interface Callback<P> {

        void call(Throwable throwable, P param);

        default void call(P param) {
            call(null, param);
        }

        default void call(Throwable throwable) {
            call(throwable, null);
        }

    }

}
