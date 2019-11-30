package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;

// notify next node the existence of a current node
public class NotifyNextMessage implements Message, Serializable {
    private int type = Constants.NOTIFY_NEXT_MESSAGE_TYPE;
    private Node curNode;

    public NotifyNextMessage(Node node) {
        curNode = node;
    }

    @Override
    public int getType() {
        return type;
    }

    public Node getCurNode() {
        return curNode;
    }

    public void setCurNode(Node curNode) {
        this.curNode = curNode;
    }
}
