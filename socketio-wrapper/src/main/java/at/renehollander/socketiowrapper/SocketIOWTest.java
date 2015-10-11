package at.renehollander.socketiowrapper;

import org.json.JSONObject;

import java.net.URI;

import at.renehollander.socketiowrapper.annotations.SubscribeEvent;
import at.renehollander.socketiowrapper.interfaces.Listener;
import io.socket.client.Socket;

public class SocketIOWTest implements Listener {

    public SocketIOWTest() {
        SocketIOW socketIOW = new SocketIOW(URI.create("http://localhost:3000/"));
        socketIOW.register(this);
        socketIOW.connect();

    }

    @SubscribeEvent(eventName = Socket.EVENT_CONNECT)
    public void onConnect(SocketIOW socketIOW, Throwable error, Object object) {
        System.out.println("connected: " + object);
        socketIOW.<JSONObject>emit("lol", "shieet", (data, err) -> {
            System.out.println(data);
        });
    }

    @SubscribeEvent(eventName = "testEvent")
    public void onTestEvent(SocketIOW socketIOW, Throwable error, JSONObject object) {
        System.out.println(object);
    }

    public static void main(String[] args) {
        new SocketIOWTest();
    }

}
