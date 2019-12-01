package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;

/**
 * client send message to request node leave
 */
public class RequestNodeLeaveMessage implements Message, Serializable {

    private int type = Constants.REQUEST_NODE_LEAVE_MESSAGE_TYPE;

    public RequestNodeLeaveMessage() {}

    @Override
    public int getType() {
        return type;
    }
}
