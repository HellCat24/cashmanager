package cashmanager.helo.com.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mazhukin Oleh on 16.11.2014.
 */
public class Utils {

    public static String getDateAndTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String getDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
        return dateFormat.format(date);
    }


}
