package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;
import java.util.HashMap;

public class RespondTransferMessage implements Message, Serializable {
    private int type = Constants.RESPOND_TRANSFER_MESSAGE_TYPE;
    private HashMap<Integer, Integer> transferData = new HashMap<>();

    private String originalNodeName;

    public RespondTransferMessage(HashMap<Integer, Integer> map, String name)
    {
        transferData.putAll(map);
        originalNodeName = name;
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


    public String getOriginalNodeName() {
        return originalNodeName;
    }

    public void setOriginalNodeName(String originalNodeName) {
        this.originalNodeName = originalNodeName;
    }
}
