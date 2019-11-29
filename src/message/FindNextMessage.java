package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;

// message to find next node (successor)
public class FindNextMessage implements Message, Serializable {
    private int type = Constants.FIND_NEXT_MESSAGE_TYPE;
    private boolean isFirst = true; // true if this the first visited node
    private Node minBiggerNode = null;  // minimal node that has id > request node
    private Node minNode = null; // minimal node in the ring
    private Node requestNode; // node that request to find its next node

    public FindNextMessage(Node node) {
        requestNode = node;
    }

    @Override
    public int getType() {
        return type;
    }

    public boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public Node getMinBiggerNode() {
        return minBiggerNode;
    }

    public void setMinBiggerNode(Node minBiggerNode) {
        this.minBiggerNode = minBiggerNode;
    }

    public Node getMinNode() {
        return minNode;
    }

    public void setMinNode(Node minNode) {
        this.minNode = minNode;
    }

    public Node getRequestNode() {
        return requestNode;
    }

    public void setRequestNode(Node requestNode) {
        this.requestNode = requestNode;
    }
}
