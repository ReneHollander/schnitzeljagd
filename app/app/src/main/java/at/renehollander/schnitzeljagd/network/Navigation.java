package at.renehollander.schnitzeljagd.network;

import android.location.Location;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Data
public abstract class Navigation {

    private final String text;

    @ToString
    @EqualsAndHashCode(callSuper = false)
    public static class Compass extends Navigation {
        @Getter
        private final Location target;
        @Getter
        private final boolean showDistance;

        public Compass(String text, Location target, boolean showDistance) {
            super(text);
            this.target = target;
            this.showDistance = showDistance;
        }
    }

    @ToString
    @EqualsAndHashCode(callSuper = false)
    public static class Map extends Navigation {
        @Getter
        private final Location target;

        public Map(String text, Location target) {
            super(text);
            this.target = target;
        }
    }

    @ToString
    @EqualsAndHashCode(callSuper = false)
    public static class Text extends Navigation {
        @Getter
        private final String content;

        public Text(String text, String content) {
            super(text);
            this.content = content;
        }
    }

}