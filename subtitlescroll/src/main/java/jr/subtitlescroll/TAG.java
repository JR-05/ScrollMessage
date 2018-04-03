package jr.subtitlescroll;

/**
 * Created by Administrator on 2017/10/11.
 */

public class TAG {
    /*
    Messenger发送Message标识符
     */
    public static final int CONNECT_SUCCESSFULL = 0;
    public static final int CONNECT_FAILED = 1;
    public static final int BIND_MAIN_AC = 2;
    public static final int BIND_SUB_AC = 3;
    public static final int SET_TITLE = 7;
    public static final int CLIENT_CONNECTION = 3;
    public static final int BIND_REFRESHIP_AC = 4;
    public static final int ONPEN_WIFI_CONNECTE_SERVICE = 5;
    public static final int DISCONNECT = 6;
    public static final int CONNECT = 7;
    public static final int SCOLLSUBTITLE = 5;
    public static final int CHANGE_TEXT_COLOR = 8;
    public static final int NEXT = 9;
    /*
    发送消息标识符
     */
    public static final String KEY_PHONE_MESSAGE = "phone_msg";
    public static final String KEY_POSITION = "position";
    public static final String KEY_SUBTITLE = "subtitl";
    public static final String KEY_START = "start";
    public static final String KEY_CHANG_TEXT_COLOR = "chang_text_color";
    public static final String KEY_DISCONNECT = "disconnect";
    public static final String KEY_NEXT_Client_SCROLL = "next_client_scroll";
    public static final String KEY_NEXT_SCROLL = "next_scroll";
}
