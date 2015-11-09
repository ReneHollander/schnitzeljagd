package at.renehollander.schnitzeljagd.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import at.renehollander.schnitzeljagd.application.Util;
import at.renehollander.schnitzeljagd.fragment.SimpleFragment;
import at.renehollander.schnitzeljagd.fragment.navigation.CompassNavigationFragment;
import at.renehollander.schnitzeljagd.fragment.navigation.MapNavigationFragment;
import at.renehollander.schnitzeljagd.fragment.navigation.TextNavigationFragment;
import at.renehollander.schnitzeljagd.network.Navigation;

public class NavigationActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setNavigationFragment(false);
    }

    public void setNavigationFragment(boolean force) {
        Schnitzeljagd schnitzeljagd = Util.getSchnitzeljagd(this);
        schnitzeljagd.getCurrentStation(this, force, (err, station) -> {
            if (station != null) {
                if (station.getNavigation() instanceof Navigation.Text) {
                    getFragmentManager().beginTransaction().replace(R.id.container, new TextNavigationFragment()).commit();
                } else if (station.getNavigation() instanceof Navigation.Compass) {
                    getFragmentManager().beginTransaction().replace(R.id.container, new CompassNavigationFragment()).commit();
                } else if (station.getNavigation() instanceof Navigation.Map) {
                    getFragmentManager().beginTransaction().replace(R.id.container, new MapNavigationFragment()).commit();
                } else {
                    Log.e("navigation", "unknown navigation type");
                }
            } else {
                // TODO display wait message
                getFragmentManager().beginTransaction().replace(R.id.container, new SimpleFragment()).commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_submit_answer) {
            startActivity(new Intent(this, AnswerActivity.class));
            return true;
        } else if (id == R.id.action_force_update) {
            setNavigationFragment(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
