package at.renehollander.schnitzeljagd.fragment;

import at.renehollander.schnitzeljagd.fragment.answer.NFCAnswerFragment;
import at.renehollander.schnitzeljagd.fragment.answer.QRAnswerFragment;
import at.renehollander.schnitzeljagd.fragment.answer.QuestionAnswerFragment;
import at.renehollander.schnitzeljagd.fragment.navigation.CompassNavigationFragment;
import at.renehollander.schnitzeljagd.fragment.navigation.TextNavigationFragment;

public class Fragments {

    public static final TextNavigationFragment CONTENT = new TextNavigationFragment();
    public static final QRAnswerFragment QR_CODE = new QRAnswerFragment();
    public static final NFCAnswerFragment NFC = new NFCAnswerFragment();
    public static final QuestionAnswerFragment QUESTION = new QuestionAnswerFragment();
    public static final CompassNavigationFragment COMPASS = new CompassNavigationFragment();
    public static final LoginFragment LOGIN = new LoginFragment();

}
