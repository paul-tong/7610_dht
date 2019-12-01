package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;
import java.util.HashMap;

public class RespondTransferMessage implements Message, Serializable {
    private int type = Constants.RESPOND_TRANSFER_MESSAGE_TYPE;
    private HashMap<Integer, Integer> transferData = new HashMap<>();

    public RespondTransferMessage(HashMap<Integer, Integer> map)
    {
        transferData.putAll(map);
    }

    @Override
    public int getType() {
        return type;
    }

    public HashMap<Integer, Integer> getTransferData() {
        return transferData;
    }

    public void setTransferData(HashMap<Integer, Integer> transferData) {
        this.transferData = transferData;
    }

}
