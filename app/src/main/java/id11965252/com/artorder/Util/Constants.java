package id11965252.com.artorder.Util;

/**
 * Constants provides mapping of:
 * Server Configuration
 */
public final class Constants {

    // Server Configurations
    public static final String HTTP = "http://";
    public static final String SERVER_IP = "192.168.1.107";
    public static final String PORT = ":8080";
    public static final String API_ADDRESS = "/hotels/rest/orderService/";
    public static final String ORDER_LIST_URI = "list";
    public static final String ADD_ORDER_URI = "add";
    public static final String ORDER_DETAIL_URI = "detail?id=";

    // Add Order Response Constants
    public static final int ADDORDER_SUCCESS = 200;

    /**
     The caller references the constants using <tt>Constants.EMPTY_STRING</tt>,
     and so on. Thus, the caller should be prevented from constructing objects of
     this class, by declaring this private constructor.
     */
    private Constants(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}
