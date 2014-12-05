package cashmanager.helo.com.model.bd;

import android.database.Cursor;

import java.util.Date;

/**
 * Created by tac on 11/25/14.
 */
public class Budget {

    public int id;

    public Date date;

    public int value;

    public Budget() {

    }

    public Budget(Date date, int value) {
        this.date = date;
        this.value = value;
    }

    public Budget(Cursor cursor) {
        id = cursor.getInt(0);
        date = new Date(cursor.getLong(1));
        value = cursor.getInt(2);
    }
}
