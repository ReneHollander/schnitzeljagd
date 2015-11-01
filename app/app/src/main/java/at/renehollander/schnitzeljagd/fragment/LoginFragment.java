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
import io.socket.client.Socket;

public class LoginFragment extends Fragment implements View.OnKeyListener {

    private EditText email;
    private EditText password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Schnitzeljagd sj = Util.getSchnitzeljagd();
        if (sj.getCredentials().hasCredentials()) {
            Util.tryConnect(getActivity());
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        this.email = (EditText) rootView.findViewById(R.id.email);
        this.password = (EditText) rootView.findViewById(R.id.password);
        Button btnLogin = (Button) rootView.findViewById(R.id.btnLogin);

        Schnitzeljagd sj = Util.getSchnitzeljagd();
        if (sj.getCredentials().hasCredentials()) {
            this.email.setText(sj.getCredentials().getEmail());
            this.password.setText(sj.getCredentials().getPassword());
        }

        btnLogin.setOnClickListener(this::onLoginButtonClick);

        return rootView;
    }

    public void onLoginButtonClick(View view) {
        View currentFocus = this.getActivity().getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }

        String emailString = this.email.getText().toString();
        String passwordString = this.password.getText().toString();

        Schnitzeljagd sj = Util.getSchnitzeljagd();
        sj.getCredentials().setEmail(emailString);
        sj.getCredentials().setPassword(passwordString);

        if (sj.getApiConnection().getSocket().connected()) {
            sj.getApiConnection().getSocket().once(Socket.EVENT_DISCONNECT, (objects) -> this.getActivity().runOnUiThread(() -> Util.tryConnect(this.getActivity())));
            sj.getApiConnection().disconnect();
        } else {
            this.getActivity().runOnUiThread(() -> Util.tryConnect(this.getActivity()));
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