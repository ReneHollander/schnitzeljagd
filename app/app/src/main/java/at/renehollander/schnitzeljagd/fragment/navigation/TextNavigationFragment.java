package at.renehollander.schnitzeljagd.fragment.navigation;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import at.renehollander.schnitzeljagd.application.Util;

public class TextNavigationFragment extends Fragment {

    private Schnitzeljagd schnitzeljagd;
    private WebView contentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.schnitzeljagd = Util.getSchnitzeljagd(this.getActivity());
    }

    public Schnitzeljagd getSchnitzeljagd() {
        return schnitzeljagd;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content, container, false);

        this.contentView = (WebView) rootView.findViewById(R.id.contentWebView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getSchnitzeljagd().getConnection().getCurrentStation((err, station) -> {
            getActivity().runOnUiThread(() -> {
                Log.e("networking", "Error getting Station", err);
                if (err != null) {
                    contentView.loadData(err.toString(), "plain/text", "utf-8");
                } else {
                    contentView.loadData(station.toString(), "application/json", "utf-8");
                }
            });
        });
    }


}
