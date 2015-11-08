package at.renehollander.schnitzeljagd.fragment;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import at.renehollander.schnitzeljagd.R;
import at.renehollander.schnitzeljagd.application.Schnitzeljagd;
import at.renehollander.schnitzeljagd.application.Util;
import at.renehollander.schnitzeljagd.sensor.CompassSensor;

public class CompassFragment extends Fragment {

    private Schnitzeljagd schnitzeljagd;
    private ImageView arrowImage;
    private TextView orientationText;
    private CompassSensor compassSensor;

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
        View rootView = inflater.inflate(R.layout.fragment_compass, container, false);
        arrowImage = (ImageView) rootView.findViewById(R.id.imageViewArrow);
        orientationText = (TextView) rootView.findViewById(R.id.orientationDegrees);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Location target = new Location("dummyprovider");
        target.setLatitude(48.305407);
        target.setLongitude(16.326075);

        compassSensor = new CompassSensor(Util.getSchnitzeljagd(getActivity()), this.getActivity(), target);

        compassSensor.setChangeListener((rotation) -> {
            orientationText.setText(Math.round(rotation) + "°");
            rotation *= -1;
            arrowImage.setRotation(rotation);

        });

        compassSensor.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        compassSensor.stop();
    }
}
