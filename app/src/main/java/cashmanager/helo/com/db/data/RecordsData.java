package cashmanager.helo.com.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    public int getMaxPrice() {
        int maxPrice = 0;
        Cursor cursor;
        try {
            cursor = getRWDb().query(DB.RecordTableInfo.TBL_NAME, new String[]{"MAX(cost)"}, null, null, null, null, null);
            cursor.moveToFirst();
            maxPrice = cursor.getInt(0);
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException getMaxPrice()", e);
        }
        return maxPrice;
    }

    public int getMinPrice() {
        int minPrice = 0;
        Cursor cursor;
        try {
            cursor = getRWDb().query(DB.RecordTableInfo.TBL_NAME, new String[]{"MIN(cost)"}, null, null, null, null, null);
            cursor.moveToFirst();
            minPrice = cursor.getInt(0);
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException getMinPrice()", e);
        }
        return minPrice;
    }

    public int getWeekPrice() {
        int weekPrice = 0;
        Cursor cursor;
        Date[] dates = getWeekDays();
        try {
            cursor = getRWDb().rawQuery("select sum(cost) from " + DB.RecordTableInfo.TBL_NAME + " where date BETWEEN " + dates[0].getTime() + " AND " + dates[1].getTime(), null);
            cursor.moveToFirst();
            weekPrice = cursor.getInt(0);
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException getWeekPrice()", e);
        }
        return weekPrice;
    }

    public int getMonthPrice() {
        int monthPrice = 0;
        Cursor cursor;
        Date[] dates = getMonthDays();
        try {
            cursor = getRWDb().rawQuery("select sum(cost) from " + DB.RecordTableInfo.TBL_NAME + " where date BETWEEN " + dates[0].getTime() + " AND " + dates[1].getTime(), null);
            cursor.moveToFirst();
            monthPrice = cursor.getInt(0);
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException getMonthPrice()", e);
        }
        return monthPrice;
    }

    private Date[] getWeekDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        Date[] dates = new Date[2];
        for (int i = 0; i < 2; i++) {
            dates[i] = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 6);
        }
        return dates;
    }

    private Date[] getMonthDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        Date[] dates = new Date[2];
        for (int i = 0; i < 2; i++) {
            dates[i] = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
        }
        return dates;
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