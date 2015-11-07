package at.renehollander.schnitzeljagd.util;

import android.location.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
public class Polygon {

    private final List<Location> vertices;

    public Polygon(@NonNull Location... vertices) {
        this.vertices = new ArrayList<>(Arrays.asList(vertices));
    }

    public void contains(Location location) {
        // TODO implement
    }

}
