package at.renehollander.schnitzeljagd.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Arrays;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import at.renehollander.schnitzeljagd.application.Util;
import at.renehollander.schnitzeljagd.fragment.Fragments;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java8.lang.FunctionalInterface;
import lombok.NonNull;

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

    public static <T> T validateData(@NonNull Object[] data) {
        Log.d("networking", Arrays.toString(data));
        if (data.length >= 1) {
            return (T) data[0];
        } else {
            throw new IllegalArgumentException("data recieved from server is invalid");
        }
    }

    private class Worker {
        private Emitter.Listener connectError = null;
        private Emitter.Listener authenticated = null;
        private Emitter.Listener unautorized = null;

        private Activity activity;

        public Worker(Activity activity) {
            this.activity = activity;
        }

        public void destroyListeners() {
            socket.off(Socket.EVENT_CONNECT_ERROR, connectError);
            socket.off(Connection.SOCKET_AUTHENTICATED, authenticated);
            socket.off(Connection.SOCKET_UNAUTHORIZED, unautorized);
        }

        public void work() {
            ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Connecting");
            progressDialog.setMessage("Connecting to API Server...");
            progressDialog.show();

            connectError = (object) -> {
                destroyListeners();
                Log.d("networking", "Error connecting to API Server: " + Arrays.toString(object));
                Util.displayErrorDialogFromString(activity, "Error connecting to API Server", Arrays.toString(object));
                progressDialog.dismiss();
            };
            socket.once(Socket.EVENT_CONNECT_ERROR, connectError);

            authenticated = (object) -> {
                destroyListeners();
                Log.d("networking", "Successfully logged in!");
                progressDialog.dismiss();
                Util.replaceFragment(activity, R.id.container, Fragments.CONTENT);
            };
            socket.once(Connection.SOCKET_AUTHENTICATED, authenticated);

            unautorized = (object) -> {
                destroyListeners();
                Log.d("networking", "Invalid Teamname or Password");
                Util.displayErrorDialogFromString(activity, "Error connecting to API Server", "Invalid Teamname or Password");
                progressDialog.dismiss();
            };
            socket.once(Connection.SOCKET_UNAUTHORIZED, unautorized);

            Connection.this.connect();
        }

    }

    public void tryConnect(Activity activity) {
        new Worker(activity).work();
    }

    public void getCurrentStation(Connection.Callback<Station> cb) {
        getSocket().emit("get_current_station", null, (data) -> {
            try {
                JSONObject obj = Connection.validateData(data);
                if (obj == null) {
                    cb.call(null, null);
                } else {

                    Navigation navigation = null;
                    {
                        JSONObject navObj = obj.getJSONObject("navigation");
                        String type = navObj.getString("type");
                        String text = null;
                        if (navObj.has("text")) {
                            text = navObj.getString("text");
                        }
                        switch (type) {
                            case "text":
                                String content = navObj.getString("content");
                                navigation = new Navigation.Text(text, content);
                                break;
                            default:
                                throw new IllegalStateException("unknown navigation type " + type);
                        }
                    }

                    Answer answer = null;
                    {
                        JSONObject answerObj = obj.getJSONObject("answer");
                        String type = answerObj.getString("type");
                        String text = null;
                        if (answerObj.has("text")) {
                            text = answerObj.getString("text");
                        }
                        switch (type) {
                            case "qr":
                                answer = new Answer.QR(text);
                                break;
                            default:
                                throw new IllegalStateException("unknown answer type " + type);
                        }
                    }

                    Station station = new Station.StationBuilder().name(obj.getString("name")).description(obj.getString("description")).navigation(navigation).answer(answer).build();
                    cb.call(station);
                }
            } catch (Exception e) {
                cb.call(e);
            }
        });
    }

}
