package at.renehollander.socketiowrapper;

import org.json.JSONObject;

import at.renehollander.socketiowrapper.annotations.Parser;
import at.renehollander.socketiowrapper.annotations.SubscribeEvent;
import io.socket.client.Socket;

public class SocketIOHelper {

    public SocketIOHelper() {

    }

    @Parser.JSON
    @SubscribeEvent(eventName = Socket.EVENT_CONNECT)
    public void onConnect(SocketIOW socketIOW, Throwable error, JSONObject object) {

    }

}
