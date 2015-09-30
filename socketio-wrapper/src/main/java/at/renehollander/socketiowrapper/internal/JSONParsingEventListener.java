package at.renehollander.socketiowrapper.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import at.renehollander.socketiowrapper.SocketIOW;
import io.socket.emitter.Emitter;

public class JSONParsingEventListener implements Emitter.Listener {

    private SocketIOW socketIOW;
    private Object reciever;
    private Method callback;
    private Class<?> dataType;

    public JSONParsingEventListener(SocketIOW socketIOW, Object reciever, Method callback, Class<?> dataType) {
        this.socketIOW = socketIOW;
        this.reciever = reciever;
        this.callback = callback;
        this.dataType = dataType;
    }

    @Override
    public void call(Object... args) {
        Throwable error = null;
        Object data = null;
        if (args.length >= 1) {
            data = args[0];
            if (!this.dataType.isInstance(data)) {
                error = new IllegalArgumentException("Data parsed is not a type of " + dataType);
                data = null;
            }
        }
        try {
            this.callback.invoke(this.reciever, this.socketIOW, error, data);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
