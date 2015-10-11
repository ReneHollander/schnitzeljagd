package at.renehollander.socketiowrapper;

import java.lang.reflect.Method;
import java.net.URI;

import at.renehollander.socketiowrapper.annotations.SubscribeEvent;
import at.renehollander.socketiowrapper.interfaces.Listener;
import at.renehollander.socketiowrapper.internal.EventListener;
import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketIOW implements Listener {

    private Socket socket;

    public SocketIOW(URI uri) {
        this.socket = IO.socket(uri);
    }

    public void connect() {
        connect(null);
    }

    public void connect(final Object authData) {
        if (authData != null) {
            getSocket().on(Socket.EVENT_CONNECT, args -> {
                getSocket().on("authenticated", args1 -> {
                    if (this.connectCallback != null) {
                        this.connectCallback.call();
                    }
                });

                getSocket().emit("authentication", authData);
            });
        } else {
            getSocket().on(Socket.EVENT_CONNECT, args -> {
                if (this.connectCallback != null) {
                    this.connectCallback.call();
                }
            });
        }

        this.register(this);
        this.socket.connect();
    }

    public Socket getSocket() {
        return socket;
    }

    public void emit(String event, Object data) {
        this.getSocket().emit(event, data);
    }

    @SuppressWarnings("unchecked")
    public <T> void emit(String event, Object data, Acknowledge<T> acknowledge) {
        this.getSocket().emit(event, new Object[]{data}, args -> {
            if (args.length > 0) {
                try {
                    acknowledge.call((T) args[0], null);
                } catch (ClassCastException e) {
                    acknowledge.call(null, new IllegalStateException("answer from server is not the provided type"));
                }
            } else {
                acknowledge.call(null, null);
            }
        });
    }

    public void disconnect() {
        this.getSocket().disconnect();
    }

    public void register(Listener listener) {
        for (Method method : listener.getClass().getMethods()) {
            SubscribeEvent subscribeEvent = method.getAnnotation(SubscribeEvent.class);
            if (subscribeEvent == null) continue;
            String eventName = subscribeEvent.eventName();

            if (method.getParameterTypes().length != 3) throw new IllegalArgumentException(method + " has to wrong amount of arguments");
            checkSocketIOWThrowableParameters(method);
            getSocket().on(eventName, new EventListener(this, listener, method, method.getParameterTypes()[2]));
        }
    }

    private static void checkSocketIOWThrowableParameters(Method method) {
        if (method.getParameterTypes().length < 2) {
            throw new IllegalArgumentException(method + " has to wrong amount of arguments");
        }
        Class<?> param0 = method.getParameterTypes()[0];
        Class<?> param1 = method.getParameterTypes()[1];
        if (param0 != SocketIOW.class) throw new IllegalArgumentException("First parameter of " + method + " must be of type SocketIOW");
        if (param1 != Throwable.class) throw new IllegalArgumentException("Second parameter of " + method + " must be of type Throwable");
    }

    public interface Callbacks {

        @FunctionalInterface
        interface Connect {
            void call();
        }

        @FunctionalInterface
        interface Disconnect {
            void call();
        }

        @FunctionalInterface
        interface Error {
            void call(Throwable error);
        }

        @FunctionalInterface
        interface ConnectError {
            void call(Throwable error);
        }

        @FunctionalInterface
        interface ConnectTimeout {
            void call();
        }

        @FunctionalInterface
        interface Reconnect {
            void call();
        }

        @FunctionalInterface
        interface ReconnectError {
            void call(Throwable error);
        }

        @FunctionalInterface
        interface ReconnectFailed {
            void call();
        }

        @FunctionalInterface
        interface ReconnectAttempt {
            void call();
        }

        @FunctionalInterface
        interface Reconnecting {
            void call();
        }

    }

    @FunctionalInterface
    public interface Acknowledge<T> {
        void call(T data, Throwable error);
    }

    private Callbacks.Connect connectCallback;
    private Callbacks.Disconnect disconnectCallback;
    private Callbacks.Error errorCallback;
    private Callbacks.ConnectError connectErrorCallback;
    private Callbacks.ConnectTimeout connectTimeoutCallback;
    private Callbacks.Reconnect reconnectCallback;
    private Callbacks.ReconnectError reconnectErrorCallback;
    private Callbacks.ReconnectFailed reconnectFailedCallback;
    private Callbacks.ReconnectAttempt reconnectAttemptCallback;
    private Callbacks.Reconnecting reconnectingCallback;

    public void setConnectCallback(Callbacks.Connect connectCallback) {
        this.connectCallback = connectCallback;
    }

    public void setDisconnectCallback(Callbacks.Disconnect disconnectCallback) {
        this.disconnectCallback = disconnectCallback;
    }

    public void setErrorCallback(Callbacks.Error errorCallback) {
        this.errorCallback = errorCallback;
    }

    public void setConnectErrorCallback(Callbacks.ConnectError connectErrorCallback) {
        this.connectErrorCallback = connectErrorCallback;
    }

    public void setConnectTimeoutCallback(Callbacks.ConnectTimeout connectTimeoutCallback) {
        this.connectTimeoutCallback = connectTimeoutCallback;
    }

    public void setReconnectCallback(Callbacks.Reconnect reconnectCallback) {
        this.reconnectCallback = reconnectCallback;
    }

    public void setReconnectErrorCallback(Callbacks.ReconnectError reconnectErrorCallback) {
        this.reconnectErrorCallback = reconnectErrorCallback;
    }

    public void setReconnectFailedCallback(Callbacks.ReconnectFailed reconnectFailedCallback) {
        this.reconnectFailedCallback = reconnectFailedCallback;
    }

    public void setReconnectAttemptCallback(Callbacks.ReconnectAttempt reconnectAttemptCallback) {
        this.reconnectAttemptCallback = reconnectAttemptCallback;
    }

    public void setReconnectingCallback(Callbacks.Reconnecting reconnectingCallback) {
        this.reconnectingCallback = reconnectingCallback;
    }

    @SubscribeEvent(eventName = Socket.EVENT_DISCONNECT)
    public void onDisconnect(SocketIOW socketIOW, Throwable error, Object object) {
        if (this.disconnectCallback != null) {
            this.disconnectCallback.call();
        }
    }

    @SubscribeEvent(eventName = Socket.EVENT_ERROR)
    public void onError(SocketIOW socketIOW, Throwable error, Object object) {
        if (this.errorCallback != null) {
            // TODO throwable
            this.errorCallback.call(null);
        }
    }

    @SubscribeEvent(eventName = Socket.EVENT_CONNECT_ERROR)
    public void onConnectError(SocketIOW socketIOW, Throwable error, Object object) {
        if (this.connectErrorCallback != null) {
            // TODO throwable
            this.connectErrorCallback.call(null);
        }
    }

    @SubscribeEvent(eventName = Socket.EVENT_CONNECT_TIMEOUT)
    public void onConnectTimeout(SocketIOW socketIOW, Throwable error, Object object) {
        if (this.connectTimeoutCallback != null) {
            this.connectTimeoutCallback.call();
        }
    }

    @SubscribeEvent(eventName = Socket.EVENT_RECONNECT)
    public void onReconnect(SocketIOW socketIOW, Throwable error, Object object) {
        if (this.reconnectCallback != null) {
            this.reconnectCallback.call();
        }
    }

    @SubscribeEvent(eventName = Socket.EVENT_RECONNECT_ERROR)
    public void onReconnectError(SocketIOW socketIOW, Throwable error, Object object) {
        if (this.reconnectErrorCallback != null) {
            // TODO throwable
            this.reconnectErrorCallback.call(null);
        }
    }

    @SubscribeEvent(eventName = Socket.EVENT_RECONNECT_FAILED)
    public void onReconnectFailed(SocketIOW socketIOW, Throwable error, Object object) {
        if (this.reconnectFailedCallback != null) {
            this.reconnectFailedCallback.call();
        }
    }

    @SubscribeEvent(eventName = Socket.EVENT_RECONNECT_ATTEMPT)
    public void onReconnectAttempt(SocketIOW socketIOW, Throwable error, Object object) {
        if (this.reconnectAttemptCallback != null) {
            this.reconnectAttemptCallback.call();
        }
    }

    @SubscribeEvent(eventName = Socket.EVENT_RECONNECTING)
    public void onReconnecting(SocketIOW socketIOW, Throwable error, Object object) {
        if (this.reconnectingCallback != null) {
            this.reconnectingCallback.call();
        }
    }

}
