package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;

public class SetPrevNodeMessage implements Message, Serializable {
    private int type = Constants.SET_PREV_MESSAGE_TYPE;
    private Node prev;

    public SetPrevNodeMessage(Node node) {
        prev = node;
    }

    @Override
    public int getType() {
        return 0;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}

