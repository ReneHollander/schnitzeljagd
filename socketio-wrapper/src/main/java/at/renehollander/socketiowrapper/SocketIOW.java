package at.renehollander.socketiowrapper;

import java.lang.reflect.Method;
import java.net.URI;

import at.renehollander.socketiowrapper.annotations.Parser;
import at.renehollander.socketiowrapper.annotations.SubscribeEvent;
import at.renehollander.socketiowrapper.interfaces.Listener;
import at.renehollander.socketiowrapper.internal.JSONParsingEventListener;
import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketIOW {

    private Socket socket;

    public SocketIOW(URI uri) {
        this.socket = IO.socket(uri);
    }

    public void connect() {
        this.socket.connect();
    }

    public Socket getSocket() {
        return socket;
    }

    public void register(Listener listener) {
        for (Method method : listener.getClass().getMethods()) {
            SubscribeEvent subscribeEvent = method.getAnnotation(SubscribeEvent.class);
            if (subscribeEvent == null) continue;
            String eventName = subscribeEvent.eventName();

            Parser.JSON parserJSON = method.getAnnotation(Parser.JSON.class);
            if (parserJSON == null) throw new IllegalArgumentException(method + " doesn't have a parser porvided");
            else {
                if (method.getParameterCount() != 3) throw new IllegalArgumentException(method + " has to wrong amount of arguments");
                checkSocketIOWThrowableParameters(method);
                socket.on(eventName, new JSONParsingEventListener(this, listener, method, parserJSON.dataType()));
            }
        }
    }

    private static void checkSocketIOWThrowableParameters(Method method) {
        if (method.getParameterCount() < 2) {
            throw new IllegalArgumentException(method + " has to wrong amount of arguments");
        }
        Class<?> param0 = method.getParameterTypes()[0];
        Class<?> param1 = method.getParameterTypes()[1];
        if (param0 != SocketIOW.class) throw new IllegalArgumentException("First parameter of " + method + " must be of type SocketIOW");
        if (param1 != Throwable.class) throw new IllegalArgumentException("Second parameter of " + method + " must be of type Throwable");
    }

}
