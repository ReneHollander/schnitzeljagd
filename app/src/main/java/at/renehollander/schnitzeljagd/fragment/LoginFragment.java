package at.renehollander.schnitzeljagd.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import at.renehollander.schnitzeljagd.application.Util;

public class LoginFragment extends Fragment implements View.OnKeyListener {

    private EditText teamName;
    private EditText password;
    private Button btnLogin;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        this.teamName = (EditText) rootView.findViewById(R.id.teamName);
        this.password = (EditText) rootView.findViewById(R.id.password);
        this.btnLogin = (Button) rootView.findViewById(R.id.btnLogin);

        Schnitzeljagd sj = Util.getSchnitzeljagd();
        if (sj.getTeamCredentials().hasCredentials()) {
            this.teamName.setText(sj.getTeamCredentials().getName());
        }

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
        sj.getApiConnection().disconnect();
        sj.getApiConnection().connect();
        Util.addFragment(this.getActivity(), R.id.container, Fragments.CONTENT);

        View currentFocus = this.getActivity().getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Util.replaceFragment(this.getActivity(), R.id.container, Fragments.CONTENT);
            return true;
        }
        return false;
    }

}
