package at.renehollander.schnitzeljagd.activity;

import android.app.Activity;
import android.os.Bundle;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.fragment.LoginFragment;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        getFragmentManager().beginTransaction().add(R.id.container, new LoginFragment()).commit();
    }

}
