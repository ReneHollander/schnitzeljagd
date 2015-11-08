package at.renehollander.schnitzeljagd.network;

import android.location.Location;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Data
public abstract class Answer {

    private final String text;

    @ToString
    @EqualsAndHashCode(callSuper = false)
    public static class QR extends Answer {
        public QR(String text) {
            super(text);
        }
    }
}
