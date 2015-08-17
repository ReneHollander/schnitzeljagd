package at.renehollander.schnitzeljagd.objects.station;

import java.util.UUID;

public class QrStation extends Station {

    public QrStation(UUID id, String name, String content) {
        super(Type.QR, id, name, content);
    }

    @Override
    public String toString() {
        return "QrStation [type=" + type + ", id=" + id + ", name=" + name + ", content=" + content + "]";
    }

}
