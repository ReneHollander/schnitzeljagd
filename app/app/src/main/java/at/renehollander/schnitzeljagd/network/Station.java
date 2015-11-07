package at.renehollander.schnitzeljagd.network;

import android.location.Location;

import java.util.Arrays;

import io.socket.client.Socket;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
public class Station {

    private final String name;
    private final String description;
    private final Navigation navigation;

    public static Station getCurrentStation(Connection.Callback<String> cb) {

        Socket socket = Connection.instance().getSocket();

        socket.emit("get_current_station", null, (data) -> {
            System.out.println(Arrays.toString(data));
            if (data.length >= 1) {
                Object obj = data[0];
                if (obj == null) {
                    cb.call(new NullPointerException("data recieved from server was null"));
                } else {
                    cb.call(obj.toString());
                }
            }
            // TODO parse data
        });

        return null;
    }

}
