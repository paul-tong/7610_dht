package constant;

public class Constants {
    public static final String CLIENT_NAME = "client";
    public static final int CLIENT_ID = 9980;
    public static final int FIND_NEXT_MESSAGE_TYPE = 1;
    public static final int SET_NEXT_MESSAGE_TYPE = 2;
    public static final int SET_PREV_MESSAGE_TYPE = 3;
    public static final int REQUEST_PREV_MESSAGE_TYPE = 4;
    public static final int RETURN_PREV_MESSAGE_TYPE = 5;
    public static final int NOTIFY_NEXT_MESSAGE_TYPE = 6;
    public static final int REQUEST_NODE_LEAVE_MESSAGE_TYPE = 7;
    public static final int RESPOND_CLIENT_MESSAGE_TYPE = 8;
    public static final int REQUEST_TRANSFER_MESSAGE_TYPE = 9;
    public static final int RESPOND_TRANSFER_MESSAGE_TYPE = 10;
    public static final int JOIN_MIN_TRANFER_TYPE = 1;
    public static final int JOIN_NOT_MIN_TRANFER_TYPE = 2;
    public static final int LEAVE_TRANSFER_TYPE = 3;
    public static final int PORT = 9989;
    public static final int BUFFER_SIZE = 65535;
    public static final int MODULE = (int)Math.pow(2, 6); // 2^m, used to mod hashed id
    public static final int INITIAL_WAIT_TIME = 15;
    public static final long STABLIZATION_INTERVAL = 5000L;
    public static final long STABLIZATION_DELAY = 2000L;
}
