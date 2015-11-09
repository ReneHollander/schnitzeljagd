package at.renehollander.schnitzeljagd.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Util;
import at.renehollander.schnitzeljagd.fragment.Fragments;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        if (savedInstanceState == null) {
            Util.addFragment(this, R.id.container, Fragments.LOGIN);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logindata) {
            getFragmentManager().beginTransaction().replace(R.id.container, Fragments.LOGIN).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}