package at.renehollander.schnitzeljagd.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import at.renehollander.schnitzeljagd.application.Util;
import at.renehollander.schnitzeljagd.fragment.Fragments;

public class MainActivity extends Activity {

    private MenuItem scanQr;
    private MenuItem submitAnswer;
    private MenuItem forceUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activities.MAIN = this;
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            Util.addFragment(this, R.id.container, Fragments.LOGIN);
        }
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

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        Schnitzeljagd sj = (Schnitzeljagd) this.getApplication();

        //if (sj.getTeamKey() == null) {
        //    this.scanQr.setEnabled(true);
        //} else {
        this.forceUpdate.setEnabled(true);
            /*
            if (sj.getCurrentStation() != null) {
                if (sj.getCurrentStation() instanceof QrStation) {
                    this.submitAnswer.setEnabled(false);
                    this.scanQr.setEnabled(true);
                } else if (sj.getCurrentStation() instanceof QuestionStation) {
                    this.scanQr.setEnabled(false);
                    this.submitAnswer.setEnabled(true);
                }
            }
            */
        //}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_scan_qrcode) {
            getFragmentManager().beginTransaction().replace(R.id.container, Fragments.QR_CODE).commit();
            return true;
        } else if (id == R.id.action_submit_answer) {
            getFragmentManager().beginTransaction().replace(R.id.container, Fragments.QUESTION).commit();
            return true;
        } else if (id == R.id.action_force_update) {
            final ProgressDialog progressDialog = Util.createProgressDialog(this);
            final Schnitzeljagd sj = (Schnitzeljagd) this.getApplication();
            sj.updateCurrentStation(this, null, progressDialog);
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
                String contents = data.getStringExtra("SCAN_RESULT"); //this is the result
                Log.d("Schnitzeljagd", contents);
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

            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }

            } */
            }

        }

    }
}