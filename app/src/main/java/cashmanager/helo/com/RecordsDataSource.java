package cashmanager.helo.com;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cashmanager.helo.com.model.bd.Record;

public class RecordsDataSource extends PizzaDataSource {
    private static final String TAG = RecordsDataSource.class.getSimpleName();
    private String[] showColumns = {
            DB.RecordTableInfo.COL_ID,
            DB.RecordTableInfo.COL_TITLE,
            DB.RecordTableInfo.COL_DATE,
            DB.RecordTableInfo.COL_DESCRIPTION,
            DB.RecordTableInfo.COL_COST,
            DB.RecordTableInfo.COL_FILE_PATH
    };

    public RecordsDataSource(DBHelper dbHelper) {
        super(dbHelper);
    }

    private Record parseDbCursorToAddition(Cursor cursor) {
        Record record = new Record();

        record.id = cursor.getInt(0);
        record.title = cursor.getString(1);
        record.date = new Date(cursor.getLong(2));
        record.description = cursor.getString(3);
        record.cost = cursor.getInt(4);

        return record;
    }

    public List<Record> getRecordList() {
        List<Record> additionDOList = new ArrayList<Record>();
        Cursor cursor;
        try {
            cursor = getRWDb().query(DB.RecordTableInfo.TBL_NAME, showColumns, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Record additionDO = parseDbCursorToAddition(cursor);
                additionDOList.add(additionDO);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException getRecordList()", e);
        }
        return additionDOList;
    }

    public void addRecord(Record record) {
        ContentValues values = new ContentValues();


        values.put(DB.RecordTableInfo.COL_TITLE, record.title);
        values.put(DB.RecordTableInfo.COL_DATE, record.date.getTime());
        values.put(DB.RecordTableInfo.COL_DESCRIPTION, record.description);
        values.put(DB.RecordTableInfo.COL_COST, record.cost);
        //values.put(DB.RecordTableInfo.COL_FILE_PATH, record.);

        if (record.id > 0) {

        } else {
            try {
                getRWDb().insert(DB.RecordTableInfo.TBL_NAME, null, values);
            } catch (SQLException e) {
                Log.e(TAG, "SQLException updateRecord", e);
            }
        }
    }

    public void deleteRecord(int recordID) {
        try {
            getRWDb().delete(DB.RecordTableInfo.TBL_NAME, DB.RecordTableInfo.COL_ID, new String[]{String.valueOf(recordID)});
        } catch (SQLException e) {
            Log.e(TAG, "SQLException deleteRecord", e);
        }
    }

}