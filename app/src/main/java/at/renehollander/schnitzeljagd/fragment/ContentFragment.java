package at.renehollander.schnitzeljagd.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;

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
        Schnitzeljagd sj = (Schnitzeljagd) getActivity().getApplication();
        this.contentView.loadData(sj.getCurrentStationContent(), "text/html", "utf8");
    }


}
