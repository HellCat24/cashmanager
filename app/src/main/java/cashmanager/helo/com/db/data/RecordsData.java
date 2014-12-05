package cashmanager.helo.com.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cashmanager.helo.com.db.data.base.DataSource;
import cashmanager.helo.com.db.DB;
import cashmanager.helo.com.db.DBHelper;
import cashmanager.helo.com.model.bd.Record;

public class RecordsData extends DataSource {
    private static final String TAG = RecordsData.class.getSimpleName();

    public RecordsData(DBHelper dbHelper) {
        super(dbHelper);
    }

    public List<Record> getRecordList() {
        List<Record> additionDOList = new ArrayList<Record>();
        Cursor cursor;
        try {
            cursor = getRWDb().query(DB.RecordTableInfo.TBL_NAME, null, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Record additionDO = new Record(cursor);
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

        values.put(DB.RecordTableInfo.COL_DATE, record.date.getTime());
        values.put(DB.RecordTableInfo.COL_DESCRIPTION, record.description);
        values.put(DB.RecordTableInfo.COL_COST, record.cost);
        //values.put(DB.RecordTableInfo.COL_FILE_PATH, record.a);

        if (record.id > 0) {
            values.put(DB.RecordTableInfo.COL_ID, record.id);
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