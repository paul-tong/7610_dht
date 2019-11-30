package constant;

public class Constants {
    public static final int FIND_NEXT_MESSAGE_TYPE = 1;
    public static final int SET_NEXT_MESSAGE_TYPE = 2;
    public static final int SET_PREV_MESSAGE_TYPE = 3;
    public static final int PORT = 9989;
    public static final int BUFFER_SIZE = 65535;
    public static final int MODULE = (int)Math.pow(2, 6); // 2^m, used to mod hashed id
    public static final int INITIAL_WAIT_TIME = 15;
    public static final long STABLIZATION_INTERVAL = 1000L;
    public static final int TOTAL_ID_SPACE = 1024;
}
