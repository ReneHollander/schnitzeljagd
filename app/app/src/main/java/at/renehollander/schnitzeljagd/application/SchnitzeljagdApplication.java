package at.renehollander.schnitzeljagd.application;

import android.app.Application;

public class SchnitzeljagdApplication extends Application {

    private Schnitzeljagd schnitzeljagd;

    public void setup() {
        this.schnitzeljagd = new Schnitzeljagd(this);
    }

    public Schnitzeljagd getSchnitzeljagd() {
        if (this.schnitzeljagd == null)
            throw new IllegalStateException("setup() was not yet called");
        return schnitzeljagd;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (getSchnitzeljagd() != null) {
            getSchnitzeljagd().getConnection().disconnect();
        }
    }
}
