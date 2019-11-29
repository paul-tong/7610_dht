package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;

// message to find next node (successor)
public class FindNextMessage implements Message, Serializable {
    int type = Constants.FIND_NEXT_MESSAGE_TYPE;
    boolean isFirst = true; // true if this the first visited node
    Node minBiggerNode = null;  // minimal node that has id > request node
    Node minNode = null; // minimal node in the ring
    Node requestNode; // node that request to find its next node

    public FindNextMessage(Node node) {
        requestNode = node;
    }

    @Override
    public int getType() {
        return type;
    }
}
