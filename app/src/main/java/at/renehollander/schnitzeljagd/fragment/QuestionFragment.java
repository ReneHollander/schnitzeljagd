package at.renehollander.schnitzeljagd.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import at.renehollander.schnitzeljagd.application.Util;

public class QuestionFragment extends Fragment implements Button.OnClickListener, View.OnKeyListener {

    private FragmentManager fm;

    private TextView question;
    private RadioGroup answerGroup;
    private RadioButton[] answerRadios;
    private Button submit;

    public QuestionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        this.fm = this.getActivity().getFragmentManager();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);

        this.question = (TextView) view.findViewById(R.id.question);
        this.answerGroup = (RadioGroup) view.findViewById(R.id.answerGroup);
        this.answerRadios = new RadioButton[4];
        this.answerRadios[0] = (RadioButton) view.findViewById(R.id.answer0);
        this.answerRadios[1] = (RadioButton) view.findViewById(R.id.answer1);
        this.answerRadios[2] = (RadioButton) view.findViewById(R.id.answer2);
        this.answerRadios[3] = (RadioButton) view.findViewById(R.id.answer3);
        this.submit = (Button) view.findViewById(R.id.submit);

        this.submit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onResume();
        Schnitzeljagd sj = (Schnitzeljagd) getActivity().getApplication();
        /*
        if (sj.getCurrentStation() instanceof QuestionStation) {
            QuestionStation qs = (QuestionStation) sj.getCurrentStation();
            this.question.setText(qs.getQuestion());
            this.answerRadios[0].setText(qs.getAnswers()[0]);
            this.answerRadios[1].setText(qs.getAnswers()[1]);
            this.answerRadios[2].setText(qs.getAnswers()[2]);
            this.answerRadios[3].setText(qs.getAnswers()[3]);
        }
        */
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            fm.beginTransaction().replace(R.id.container, Fragments.CONTENT_FRAGMENT).commit();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int checkedButtonId = this.answerGroup.getCheckedRadioButtonId();
        if (checkedButtonId == -1) {
            Util.displayErrorDialogFromString(this.getActivity(), "Error submitting Answer", "You didn't choose an answer!");
        } else {

            final ProgressDialog progressDialog = Util.createProgressDialog(this.getActivity());
            final Schnitzeljagd sj = (Schnitzeljagd) getActivity().getApplication();

            int pos = this.findAnswerRadioPosById(checkedButtonId);
            /*
            SubmitQuestion submitQuestion = new SubmitQuestion(pos);
            sj.getRestApiClient().post(Static.submitUrl(sj.getTeamKey()), SubmitRequest.class, submitQuestion, SubmitResponse.class, new RestApiResponseCallback<SubmitResponse>() {
                @Override
                public void onSuccess(SubmitResponse object) {
                    if (object.getType() == SubmitResponse.Type.SUCCESS) {
                        Success success = (Success) object;
                        if (success.getSuccess() == true) {
                            sj.updateCurrentStation(QuestionFragment.this.getActivity(), fm, progressDialog);
                        }
                    } else if (object.getType() == SubmitResponse.Type.WON) {
                        fm.beginTransaction().replace(R.id.container, Fragments.CONTENT_FRAGMENT).commit();
                        progressDialog.dismiss();
                        Util.displayWonDialog(QuestionFragment.this.getActivity());
                    }
                }

                @Override
                public void onError(ErrorType errorType, int code, String msg) {
                    Log.e("schnitzeljagd", errorType + ", " + code + ", " + msg);
                    fm.beginTransaction().replace(R.id.container, Fragments.CONTENT_FRAGMENT).commit();
                    progressDialog.dismiss();
                    Util.displayErrorDialogFromJson(QuestionFragment.this.getActivity(), Util.ERROR_SUBMITTING_QUSTION, msg);
                }

                @Override
                public void onException(Exception e) {
                    Log.e("schnitzeljagd", "", e);
                    fm.beginTransaction().replace(R.id.container, Fragments.CONTENT_FRAGMENT).commit();
                    progressDialog.dismiss();
                    Util.displayErrorDialogFromThrowable(QuestionFragment.this.getActivity(), Util.EXCEPTION_SUBMITTING_QUSTION, e);
                }

                @Override
                public void done() {
                }
            });
            */
        }
    }

    private int findAnswerRadioPosById(int viewid) {
        int ret = -1;
        for (int i = 0; i < this.answerRadios.length; i++) {
            if (this.answerRadios[i].getId() == viewid) {
                ret = i;
            }
        }
        return ret;
    }

}
