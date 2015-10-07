package at.renehollander.schnitzeljagd.fragment;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import at.renehollander.schnitzeljagd.location.SensorListener;

public class CompassFragment extends Fragment {

    private ImageView arrowImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_compass, container, false);
        arrowImage = (ImageView) rootView.findViewById(R.id.imageViewArrow);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Schnitzeljagd sj = (Schnitzeljagd) getActivity().getApplication();

        Location target = new Location("dummyprovider");
        target.setLatitude(48.305407);
        target.setLongitude(16.326075);
        SensorListener sl = new SensorListener(this.getActivity(), target);
        sl.setChangeListener((arrowImage::setRotation));
    }


}
