package at.renehollander.schnitzeljagd.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;

import com.squareup.okhttp.MediaType;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import at.renehollander.schnitzeljagd.network.Station;

public class ContentFragment extends Fragment {

    private WebView contentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content, container, false);

        this.contentView = (WebView) rootView.findViewById(R.id.contentWebView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Station.getCurrentStation((err, station) -> {
            getActivity().runOnUiThread(() -> {
                if (err != null) {
                    contentView.loadData(err.toString(), "plain/text", "utf-8");
                } else {
                    contentView.loadData(station, "application/json", "utf-8");
                }
            });
        });
    }


}
