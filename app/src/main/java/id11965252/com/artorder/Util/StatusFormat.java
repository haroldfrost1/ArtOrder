package id11965252.com.artorder.Util;

/**
 * Provides:
 * Mapping order status
 * Format status from int to String
 */
public class StatusFormat {

    public static String formatStatus(int statusCode){
        switch(statusCode){
            case 1:
            case 2:
            case 4:
            case 5:
                return "Paid";

            default:
                return "Unpaid";
        }
    }

}
