package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;

public class SetNextNodeMessage implements Message, Serializable {
    private int type = Constants.SET_NEXT_MESSAGE_TYPE;
    private Node next;

    public SetNextNodeMessage(Node node) {
        next = node;
    }

    @Override
    public int getType() {
        return 0;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
