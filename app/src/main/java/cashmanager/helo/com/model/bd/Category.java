package cashmanager.helo.com.model.bd;

import android.database.Cursor;

import com.orm.SugarRecord;

/**
 * Created by Mazhukin Oleh on 10.11.2014.
 */
public class Category {

    public int id;

    public String title;

    public Category(Cursor cursor) {
        this.id = cursor.getInt(0);
        this.title = cursor.getString(1);
    }

    public Category(String title) {
        this.title = title;
    }
}
