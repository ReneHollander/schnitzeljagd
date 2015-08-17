package at.renehollander.schnitzeljagd.objects.submit.request;

public class SubmitRequest {

    public enum Type {
        QR("qr"), QUESTION("question");

        private String keyname;

        Type(String keyname) {
            this.keyname = keyname;
        }

        public String getKeyname() {
            return keyname;
        }
    }

    protected Type type;

    public SubmitRequest(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SubmitRequest{" +
                "type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubmitRequest)) return false;

        SubmitRequest that = (SubmitRequest) o;

        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}
