package at.renehollander.schnitzeljagd.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.nfc.NFCReader;


@TargetApi(Build.VERSION_CODES.KITKAT)
public class NFCFragment extends Fragment implements NFCReader.AccountCallback{
    public static final String TAG = "NFCFFragment";
    // Recommend NfcAdapter flags for reading from other Android devices. Indicates that this
    // activity is interested in NFC-A devices (including other Android devices).
    public static int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A;
    public NFCReader nfcReader;
    private TextView contentField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nfc, container, false);
        if (v != null) {
            contentField = (TextView) v.findViewById(R.id.nfc_content);
            contentField.setText(R.string.nfc_waiting);

            nfcReader = new NFCReader(this);

            // Disable Android Beam and register our card reader callback
            enableReaderMode();
        }

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        disableReaderMode();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableReaderMode();
    }

    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
        Activity activity = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.enableReaderMode(activity, nfcReader, READER_FLAGS, null);
        }
    }

    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
        Activity activity = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.disableReaderMode(activity);
        }
    }

    @Override
    public void onAccountReceived(final String content) {
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.
        getActivity().runOnUiThread(() -> contentField.setText(content));
    }
}
