package at.renehollander.schnitzeljagd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import at.renehollander.schnitzeljagd.application.Util;
import at.renehollander.schnitzeljagd.fragment.Fragments;

public class MainActivity extends Activity implements SchnitzeljagdActivity {

    private MenuItem scanQr;
    private MenuItem submitAnswer;
    private MenuItem forceUpdate;
    private MenuItem readNFC;

    private Schnitzeljagd schnitzeljagd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.schnitzeljagd = new Schnitzeljagd(this);

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            Util.addFragment(this, R.id.container, Fragments.LOGIN);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        schnitzeljagd.destroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.scanQr = menu.findItem(R.id.action_scan_qrcode);
        this.submitAnswer = menu.findItem(R.id.action_submit_answer);
        this.forceUpdate = menu.findItem(R.id.action_force_update);
        this.readNFC = menu.findItem(R.id.action_read_nfc);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        this.forceUpdate.setEnabled(true);
        this.scanQr.setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_scan_qrcode) {
            getFragmentManager().beginTransaction().replace(R.id.container, Fragments.QR_CODE).commit();
            return true;
        } else if (id == R.id.action_read_nfc) {
            getFragmentManager().beginTransaction().replace(R.id.container, Fragments.NFC).commit();
            return true;
        } else if (id == R.id.action_submit_answer) {
            getFragmentManager().beginTransaction().replace(R.id.container, Fragments.QUESTION).commit();
            return true;
        } else if (id == R.id.action_force_update) {
            getFragmentManager().beginTransaction().detach(Fragments.CONTENT).commit();
            getFragmentManager().beginTransaction().attach(Fragments.CONTENT).commit();
            return true;
        } else if (id == R.id.action_compass) {
            getFragmentManager().beginTransaction().replace(R.id.container, Fragments.COMPASS).commit();
            return true;
        } else if (id == R.id.action_logindata) {
            getFragmentManager().beginTransaction().replace(R.id.container, Fragments.LOGIN).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                Log.d("Schnitzeljagd", contents);
            }
        }
    }

    public Schnitzeljagd getSchnitzeljagd() {
        return schnitzeljagd;
    }
}