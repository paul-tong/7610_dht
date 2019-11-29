package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;

public class SetPrevMessage implements Message, Serializable {
    private int type = Constants.SET_PREV_MESSAGE_TYPE;
    private Node prev;

    public SetPrevMessage(Node node) {
        prev = node;
    }

    @Override
    public int getType() {
        return type;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}

