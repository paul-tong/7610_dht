package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;

public class RespondClientMessage implements Message, Serializable {

    private int type = Constants.RESPOND_CLIENT_MESSAGE_TYPE;
    private String note;

    public RespondClientMessage(String note) {
        this.note = note;
    }

    @Override
    public int getType() {
        return type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}