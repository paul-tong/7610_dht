package message;

import constant.Constants;
import node.Node;

import java.io.Serializable;

public class RequestDataOperationMessage implements Message, Serializable {
    private int type = Constants.REQUEST_DATA_OPERATION_MESSAGE_TYPE;
    private int operationType;
    private int dataKey;
    private int dataVal;

    public RequestDataOperationMessage(int operationType, int key, int val) {
        this.operationType = operationType;
        dataKey = key;
        dataVal = val;
    }

    @Override
    public int getType() {
        return type;
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