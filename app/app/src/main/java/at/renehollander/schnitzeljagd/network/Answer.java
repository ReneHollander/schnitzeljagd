package at.renehollander.schnitzeljagd.network;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public abstract class Answer {

    private final String text;

    @ToString
    @EqualsAndHashCode(callSuper = false)
    public static class Scan extends Answer {
        public Scan(String text) {
            super(text);
        }
    }

    @ToString
    @EqualsAndHashCode(callSuper = false)
    public static class Question extends Answer {
        public Question(String text) {
            super(text);
        }
    }

    @ToString
    @EqualsAndHashCode(callSuper = false)
    public static class Area extends Answer {
        public Area(String text) {
            super(text);
        }
    }
}
