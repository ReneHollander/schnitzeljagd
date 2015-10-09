package at.renehollander.socketiowrapper.util;

import java8.lang.FunctionalInterface;

@FunctionalInterface
public interface Callback<T> {

    public void call(T data, Throwable error);

}
