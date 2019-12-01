package message;

import constant.Constants;

import java.io.Serializable;

public class RequestRemoveMessage implements Message, Serializable {
    private int type = Constants.REQUEST_REMOVE_MESSAGE_TYPE;
    private String key;
    private boolean isFirst = true;

    public RequestRemoveMessage(String key) {
        this.key = key;
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

    public RequestRemoveMessage type(int type) {
        this.type = type;
        return this;
    }

    public RequestRemoveMessage key(String key) {
        this.key = key;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RequestRemoveMessage)) {
            return false;
        }
        RequestRemoveMessage lookupMessage = (RequestRemoveMessage) o;
        return type == lookupMessage.type && key == lookupMessage.key;
    }

    @Override
    public String toString() {
        return "{" + " type='" + getType() + "'" + ", key='" + getKey() + "'" + "}";
    }

}
