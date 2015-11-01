package at.renehollander.schnitzeljagd.application;

import android.content.Context;
import android.content.SharedPreferences;

public class Credentials {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    private Context context;
    private String storeName;

    private SharedPreferences sharedPreferences;

    public Credentials(Context context, String storeName) {
        this.context = context;
        this.storeName = storeName;
    }

    public void setEmail(String newName) {
        getPreferences().edit().putString(EMAIL, newName).commit();
    }

    public void setPassword(String newPassword) {
        getPreferences().edit().putString(PASSWORD, newPassword).commit();
    }

    public String getEmail() {
        return getPreferences().getString(EMAIL, null);
    }

    public String getPassword() {
        return getPreferences().getString(PASSWORD, null);
    }

    public boolean hasCredentials() {
        return getEmail() != null && getPassword() != null;
    }

    public String getStoreName() {
        return storeName;
    }

    public Context getContext() {
        return context;
    }

    private SharedPreferences getPreferences() {
        if (this.sharedPreferences == null) {
            this.sharedPreferences = getContext().getSharedPreferences(getStoreName(), Context.MODE_PRIVATE);
        }
        return this.sharedPreferences;
    }

}
