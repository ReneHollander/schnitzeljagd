package at.renehollander.socketiowrapper.annotations;

import org.json.JSONObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Parser {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface JSON {

        Class<?> dataType() default JSONObject.class;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Gson {

        Class<?> type();

        Class<?> deserializer() default Void.class;

    }

}
