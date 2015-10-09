package at.renehollander.schnitzeljagd.fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.zxing.Result;
import com.google.zxing.client.android.fragment.BarCodeScannerFragment;

import at.renehollander.schnitzeljagd.R;


public class QRCodeFragment extends BarCodeScannerFragment implements BarCodeScannerFragment.IResultCallback, View.OnKeyListener {

    private FragmentManager fm;

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
        /*
        final ProgressDialog progressDialog = Util.createProgressDialog(this.getActivity());
        final Schnitzeljagd sj = (Schnitzeljagd) getActivity().getApplication();

        try {
            JSONObject qrJson = new JSONObject(result.getText());
            String type = qrJson.getString("type");
            String code = qrJson.getString("code");
            if (type.equals("teamkey")) {
                sj.setTeamKey(UUID.fromString(code));

                sj.getRestApiClient().get(Static.startUrl(sj.getTeamKey()), SubmitResponse.class, new RestApiResponseCallback<SubmitResponse>() {

                    @Override
                    public void onSuccess(SubmitResponse object) {
                        sj.updateCurrentStation(QRCodeFragment.this.getActivity(), fm, progressDialog);
                    }

                    @Override
                    public void onError(ErrorType errorType, int code, String msg) {
                        Log.e("schnitzeljagd", errorType + ", " + code + ", " + msg);
                        fm.beginTransaction().replace(R.id.container, Fragments.CONTENT).commit();
                        progressDialog.dismiss();
                        Util.displayErrorDialogFromJson(QRCodeFragment.this.getActivity(), Util.ERROR_SUBMITTING_TEAMKEY, msg);
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.e("schnitzeljagd", "", e);
                        fm.beginTransaction().replace(R.id.container, Fragments.CONTENT).commit();
                        progressDialog.dismiss();
                        Util.displayErrorDialogFromThrowable(QRCodeFragment.this.getActivity(), Util.EXCEPTION_SUBMITTING_TEAMKEY, e);
                    }

                    @Override
                    public void done() {
                    }
                });
            } else if (type.equals("station")) {
                SubmitQR submitQR = new SubmitQR(UUID.fromString(code));
                sj.getRestApiClient().post(Static.submitUrl(sj.getTeamKey()), SubmitRequest.class, submitQR, SubmitResponse.class, new RestApiResponseCallback<SubmitResponse>() {
                    @Override
                    public void onSuccess(SubmitResponse object) {
                        if (object.getType() == SubmitResponse.Type.SUCCESS) {
                            Success success = (Success) object;
                            if (success.getSuccess() == true) {
                                sj.updateCurrentStation(QRCodeFragment.this.getActivity(), fm, progressDialog);
                            }
                        } else if (object.getType() == SubmitResponse.Type.WON) {
                            fm.beginTransaction().replace(R.id.container, Fragments.CONTENT).commit();
                            progressDialog.dismiss();
                            Util.displayWonDialog(QRCodeFragment.this.getActivity());
                        }
                    }

                    @Override
                    public void onError(ErrorType errorType, int code, String msg) {
                        Log.e("schnitzeljagd", errorType + ", " + code + ", " + msg);
                        fm.beginTransaction().replace(R.id.container, Fragments.CONTENT).commit();
                        progressDialog.dismiss();
                        Util.displayErrorDialogFromJson(QRCodeFragment.this.getActivity(), Util.ERROR_SUBMITTING_QR, msg);
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.e("schnitzeljagd", "", e);
                        fm.beginTransaction().replace(R.id.container, Fragments.CONTENT).commit();
                        progressDialog.dismiss();
                        Util.displayErrorDialogFromThrowable(QRCodeFragment.this.getActivity(), Util.EXCEPTION_SUBMITTING_QR, e);
                    }

                    @Override
                    public void done() {
                    }
                });
            }
        } catch (Exception e) {
            Log.e("qr", "error handling result", e);
            fm.beginTransaction().replace(R.id.container, Fragments.CONTENT).commit();
            progressDialog.dismiss();
            Util.displayErrorDialogFromThrowable(QRCodeFragment.this.getActivity(), Util.EXCEPTION_HANDLING_RESULT, e);
        }
        */
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
