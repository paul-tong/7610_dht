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
    private Node requestNode; // node that request to find its next node. For look up, it contains key id of the data

    // attributes related to data operation(eg, add, remove..)
    private boolean isDataOperation = false;
    private int operationType;
    private int dataKey;
    private int dataVal;

    // constructor when looking up node for data operation
    public FindNextMessage(Node node, int operationType, int key, int val) {
        isDataOperation = true;
        requestNode = node; // node.id is the key id of operating data
        this.operationType = operationType;
        dataKey = key;
        dataVal = val;
    }

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

    public boolean isDataOperation() {
        return isDataOperation;
    }

    public void setDataOperation(boolean dataOperation) {
        isDataOperation = dataOperation;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public int getDataKey() {
        return dataKey;
    }

    public void setDataKey(int dataKey) {
        this.dataKey = dataKey;
    }

    public int getDataVal() {
        return dataVal;
    }

    public void setDataVal(int dataVal) {
        this.dataVal = dataVal;
    }
}
