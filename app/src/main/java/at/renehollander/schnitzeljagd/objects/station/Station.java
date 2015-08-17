package at.renehollander.schnitzeljagd.objects.station;

import java.io.Serializable;
import java.util.UUID;

public abstract class Station implements Serializable {

    public enum Type {
        QR("qr"), QUESTION("question");

        private String typeName;

        Type(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeName() {
            return this.typeName;
        }

        public static Type getFromTypeName(String typeName) {
            for (Type type : Type.values()) {
                if (type.getTypeName().equals(typeName)) {
                    return type;
                }
            }
            return null;
        }
    }

    protected Type type;
    protected UUID id;
    protected String name;
    protected String content;

    public Station(Type type, UUID id, String name, String content) {
        super();
        this.type = type;
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public Type getType() {
        return this.type;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public String toString() {
        return "Station [type=" + type + ", id=" + id + ", name=" + name + ", content=" + content + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Station other = (Station) obj;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

}
