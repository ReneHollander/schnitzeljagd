package at.renehollander.schnitzeljagd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import at.renehollander.schnitzeljagd.application.Util;
import at.renehollander.schnitzeljagd.fragment.SimpleFragment;
import at.renehollander.schnitzeljagd.fragment.answer.AreaAnswerFragment;
import at.renehollander.schnitzeljagd.fragment.answer.QRAnswerFragment;
import at.renehollander.schnitzeljagd.fragment.answer.QuestionAnswerFragment;
import at.renehollander.schnitzeljagd.network.Answer;

public class AnswerActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setAnswerFragment(false);
    }

    public void setAnswerFragment(boolean force) {
        Schnitzeljagd schnitzeljagd = Util.getSchnitzeljagd(this);
        schnitzeljagd.getCurrentStation(this, force, (err, station) -> {
            if (station != null) {
                if (station.getAnswer() instanceof Answer.Scan) {
                    getFragmentManager().beginTransaction().add(R.id.container, new QRAnswerFragment()).commit();
                } else if (station.getAnswer() instanceof Answer.Question) {
                    getFragmentManager().beginTransaction().add(R.id.container, new QuestionAnswerFragment()).commit();
                } else if (station.getAnswer() instanceof Answer.Area) {
                    getFragmentManager().beginTransaction().add(R.id.container, new AreaAnswerFragment()).commit();
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
        if (id == R.id.action_navigation) {
            startActivity(new Intent(this, NavigationActivity.class));
            return true;
        } else if (id == R.id.action_force_update) {
            setAnswerFragment(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
