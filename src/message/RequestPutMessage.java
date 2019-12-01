package message;

import constant.Constants;

import java.io.Serializable;

public class RequestPutMessage  implements Message, Serializable {
    private int type = Constants.REQUEST_PUT_MESSAGE_TYPE;
    private String key;
    private String value;
    private boolean isFirst = true;

    public RequestPutMessage(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
}
