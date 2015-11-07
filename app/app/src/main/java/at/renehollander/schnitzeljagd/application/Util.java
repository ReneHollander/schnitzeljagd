package at.renehollander.schnitzeljagd.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.activity.Activities;
import at.renehollander.schnitzeljagd.fragment.Fragments;
import at.renehollander.schnitzeljagd.network.Connection;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Util {

    public static final String EXCEPTION_HANDLING_RESULT = "An error occured while handling result";

    public static final String ERROR_SUBMITTING_QR = "An error occured while submitting qr code";
    public static final String EXCEPTION_SUBMITTING_QR = "An exception occured while submitting qr code";

    public static final String ERROR_SUBMITTING_QUSTION = "An error occured while submitting answer to question";
    public static final String EXCEPTION_SUBMITTING_QUSTION = "An exception occured while submitting answer to question";

    public static final String ERROR_SUBMITTING_TEAMKEY = "An error occured while submitting teamkey";
    public static final String EXCEPTION_SUBMITTING_TEAMKEY = "An exception occured while submitting teamkey";

    public static final String ERROR_UPDATING_CURRENTSTATION = "An error occured while updating current station";
    public static final String EXCEPTION_UPDATING_CURRENTSTATION = "An exception occured while updating current station";

    public static void displayErrorDialogFromJson(Activity activity, String title, String jsonmessage) {
        String text = jsonmessage;

        try {
            JSONObject msgObject = new JSONObject(jsonmessage);
            text = msgObject.getString("text");
        } catch (JSONException e) {
            Log.e("errordialog", "error parsing json message", e);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setCancelable(false);

        builder.setTitle(title);
        builder.setMessage(text);

        builder.setPositiveButton("Ok", (dialog, which) -> {
            dialog.dismiss();
        });

        activity.runOnUiThread(() -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    public static void displayErrorDialogFromString(Activity activity, String title, String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setCancelable(false);

        builder.setTitle(title);
        builder.setMessage(msg);

        builder.setPositiveButton("Ok", (dialog, which) -> {
            dialog.dismiss();
        });

        activity.runOnUiThread(() -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    public static void displayErrorDialogFromThrowable(Activity activity, String title, Throwable t) {
        String text = t.getMessage();

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setCancelable(false);

        builder.setTitle(title);
        builder.setMessage(text);

        builder.setPositiveButton("Ok", (dialog, which) -> {
            dialog.dismiss();
        });

        activity.runOnUiThread(() -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    public static void displayWonDialog(Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setCancelable(false);

        builder.setTitle("YOU MADE IT!");
        builder.setMessage("You made it!\nCongrats m80's");
        builder.setPositiveButton("Ok", (dialog, which) -> {
            dialog.dismiss();
        });

        activity.runOnUiThread(() -> {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    public static ProgressDialog createProgressDialog(Activity activity) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Laden...");
        progressDialog.setMessage("Lade Daten herunter, bitte warten...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        return progressDialog;
    }

    public static void addFragment(Activity activity, int id, Fragment target) {
        activity.getFragmentManager().beginTransaction().add(id, target).commit();
    }

    public static void removeFragment(Activity activity, Fragment target) {
        activity.getFragmentManager().beginTransaction().remove(target).commit();
    }

    public static void replaceFragment(Activity activity, int id, Fragment target) {
        activity.getFragmentManager().beginTransaction().replace(id, target).commit();
    }

    private static class Worker {
        private Emitter.Listener connectError = null;
        private Emitter.Listener authenticated = null;
        private Emitter.Listener unautorized = null;

        private Activity activity;
        private Socket socket;

        public Worker(Activity activity) {
            this.activity = activity;
            this.socket = Schnitzeljagd.instance().getConnection().getSocket();
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

            Schnitzeljagd.instance().getConnection().connect();
        }

    }

    public static void tryConnect(Activity activity) {
        new Worker(activity).work();
    }

}
