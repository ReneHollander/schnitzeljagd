package at.renehollander.schnitzeljagd.util;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtil {

    public static <T, U> List<Pair<T, U>> mapArrayToPairList(T[] arr1, U[] arr2) {
        if (arr1.length != arr2.length)
            throw new IllegalArgumentException("arr1 and arr2 have to of equal length");
        List<Pair<T, U>> list = new ArrayList<>();
        for (int i = 0; i < arr1.length; i++) {
            list.add(new Pair<>(arr1[i], arr2[i]));
        }
        return list;
    }

}
