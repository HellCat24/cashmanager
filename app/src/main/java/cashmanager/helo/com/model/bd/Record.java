package cashmanager.helo.com.model.bd;

import android.content.Context;
import android.database.Cursor;

import java.util.Date;

/**
 * Created by Mazhukin Oleh on 10.11.2014.
 */
public class Record {

    public int id;

    public Date date;

    public String description;

    public Integer cost;

    public boolean isPrivate;

    public Category category;

    //public Attachment attachment;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Record)) return false;

        Record record = (Record) o;

        if (id != record.id) return false;
        if (!cost.equals(record.cost)) return false;
        if (!date.equals(record.date)) return false;
        if (!description.equals(record.description)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + date.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + cost.hashCode();
        return result;
    }

    public Record(){}

    public Record(Cursor cursor){
        id = cursor.getInt(0);
        date = new Date(cursor.getLong(1));
        description = cursor.getString(2);
        cost = cursor.getInt(3);
    }
}
