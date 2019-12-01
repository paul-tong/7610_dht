package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;

public class LookupMessage implements Message, Serializable {
    private int type = Constants.LOOKUP_MESSAGE_TYPE;
    private String key;
    private boolean isFirst = true;

    public LookupMessage(String key, boolean delete) {
        this.key = key;
        if (delete) {
            type = Constants.REQUEST_REMOVE_MESSAGE_TYPE;
        }
    }
    public boolean isFirst() {
        return isFirst;
    }
    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    @Override
    public int getType() {
        return type;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LookupMessage type(int type) {
        this.type = type;
        return this;
    }

    public LookupMessage key(String key) {
        this.key = key;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof LookupMessage)) {
            return false;
        }
        LookupMessage lookupMessage = (LookupMessage) o;
        return type == lookupMessage.type && key == lookupMessage.key;
    }

    @Override
    public String toString() {
        return "{" + " type='" + getType() + "'" + ", key='" + getKey() + "'" + "}";
    }

}
