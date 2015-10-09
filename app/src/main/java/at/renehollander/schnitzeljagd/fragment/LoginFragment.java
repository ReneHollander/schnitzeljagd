package at.renehollander.schnitzeljagd.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import at.renehollander.schnitzeljagd.application.Util;

public class LoginFragment extends Fragment {

    private EditText teamName;
    private EditText password;
    private Button btnLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        this.teamName = (EditText) rootView.findViewById(R.id.teamName);
        this.password = (EditText) rootView.findViewById(R.id.password);
        this.btnLogin = (Button) rootView.findViewById(R.id.btnLogin);

        this.btnLogin.setOnClickListener(this::onLoginButtonClick);

        return rootView;
    }

    public void onLoginButtonClick(View view) {
        String teamNameString = this.teamName.getText().toString();
        String passwordString = this.password.getText().toString();

        Schnitzeljagd sj = Util.getSchnitzeljagd();
        sj.getTeamCredentials().setName(teamNameString);
        sj.getTeamCredentials().setPassword(passwordString);

        // TODO login
        Util.changeFragment(this.getActivity(), R.id.container, Fragments.CONTENT);
    }

}
