package at.renehollander.schnitzeljagd.objects.submit.response;

import java.io.Serializable;

public abstract class SubmitResponse implements Serializable {

    public enum Type {
        SUCCESS, WON;
    }

    protected Type type;

    public SubmitResponse(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SubmitResponse [type=" + type + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SubmitResponse other = (SubmitResponse) obj;
        if (type != other.type)
            return false;
        return true;
    }

}
