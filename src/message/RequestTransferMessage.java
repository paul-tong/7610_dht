package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;

public class RequestTransferMessage implements Message, Serializable {
    private int type = Constants.REQUEST_TRANSFER_MESSAGE_TYPE;
    private int transferType;
    private Node requestNode;

    public RequestTransferMessage(Node node, int transferType) {
        requestNode = node;
        this.transferType = transferType;
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

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

}
