package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;

public class RequestPrevMessage implements Message, Serializable {
    private int type = Constants.REQUEST_PREV_MESSAGE_TYPE;
    private Node requestNode;

    public RequestPrevMessage(Node node) {
        requestNode = node;
    }

    @Override
    public int getType() {
        return type;
    }

    public Node getRequestNode() {
        return requestNode;
    }

    public void setRequestNode(Node requestNode) {
        this.requestNode = requestNode;
    }
}
