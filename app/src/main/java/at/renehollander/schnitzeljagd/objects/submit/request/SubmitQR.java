package at.renehollander.schnitzeljagd.objects.submit.request;

import java.util.UUID;

public class SubmitQR extends SubmitRequest {

    private UUID qr;

    public SubmitQR(UUID qr) {
        super(Type.QR);
        this.qr = qr;
    }

    public UUID getQr() {
        return qr;
    }

    @Override
    public String toString() {
        return "SubmitQR{" +
                "qr=" + qr +
                '}';
    }
}
