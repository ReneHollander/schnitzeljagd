package at.renehollander.schnitzeljagd.fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.zxing.Result;
import com.google.zxing.client.android.fragment.BarCodeScannerFragment;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import at.renehollander.schnitzeljagd.application.Util;


public class QRCodeFragment extends BarCodeScannerFragment implements BarCodeScannerFragment.IResultCallback, View.OnKeyListener {

    private Schnitzeljagd schnitzeljagd;
    private FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.schnitzeljagd = Util.getSchnitzeljagd(this.getActivity());
    }

    public Schnitzeljagd getSchnitzeljagd() {
        return schnitzeljagd;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.fm = this.getActivity().getFragmentManager();
        this.setmCallBack(this);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(this);
    }

    @Override
    public void result(Result result) {
        Log.d("Schnitzeljagd", result.getText());
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            fm.beginTransaction().replace(R.id.container, Fragments.CONTENT).commit();
            return true;
        }
        return false;
    }
}
