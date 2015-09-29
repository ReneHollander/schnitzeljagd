package at.renehollander.socketiohelper;

import org.json.JSONObject;

import at.renehollander.socketiohelper.annotations.Parser;
import at.renehollander.socketiohelper.annotations.SubscribeEvent;
import io.socket.client.Socket;

public class SocketIOHelper {

    public SocketIOHelper() {

    }

    @Parser.JSON
    @SubscribeEvent(eventName = Socket.EVENT_CONNECT)
    public void onConnect(JSONObject object) {

    }

}
