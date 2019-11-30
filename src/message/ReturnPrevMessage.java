package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;

public class ReturnPrevMessage implements Message, Serializable {
    private int type = Constants.RETURN_PREV_MESSAGE_TYPE;
    private Node preNode;

    public ReturnPrevMessage(Node node) {
        preNode = node;
    }

    @Override
    public int getType() {
        return type;
    }

    public Node getPreNode() {
        return preNode;
    }

    public void setPreNode(Node preNode) {
        this.preNode = preNode;
    }
}
