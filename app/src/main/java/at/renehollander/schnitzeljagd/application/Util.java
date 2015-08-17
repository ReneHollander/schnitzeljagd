package at.renehollander.schnitzeljagd.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public static void displayErrorDialogFromString(Activity activity, String title, String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setCancelable(false);

        builder.setTitle(title);
        builder.setMessage(msg);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public static void displayErrorDialogFromThrowable(Activity activity, String title, Throwable t) {
        String text = t.getMessage();

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setCancelable(false);

        builder.setTitle(title);
        builder.setMessage(text);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public static void displayWonDialog(Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setCancelable(false);

        builder.setTitle("YOU MADE IT!");
        builder.setMessage("You made it!\nCongrats m80's");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
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

}
