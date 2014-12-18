package cashmanager.helo.com.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.sql.SQLException;
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

    public enum TimeSearchType {DAY, WEEK, MONTH}

    public RecordsData(DBHelper dbHelper) {
        super(dbHelper);
    }

    public List<Record> getRecordList(boolean isPrivate) {
        List<Record> additionDOList = new ArrayList<Record>();
        Cursor cursor;
        try {
            if(isPrivate){
                cursor = getRWDb().query(DB.RecordTableInfo.TBL_NAME, null, null, null, null, null, null);
            } else {
                cursor = getRWDb().query(DB.RecordTableInfo.TBL_NAME, null, DB.RecordTableInfo.COL_IS_PRIVATE + "=" + (isPrivate ? 1: 0), null, null, null, null);
            }
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

    public List<Record> getRecordListWithinDates(TimeSearchType searchType, boolean isPrivate) {
        List<Record> additionDOList = new ArrayList<Record>();
        Date[] dates = null;
        switch (searchType) {
            case DAY:
                dates = getDayDates();
                break;
            case WEEK:
                dates = getWeekDates();
                break;
            case MONTH:
                dates = getMonthDates();
                break;
        }
        Cursor cursor;
        try {
            cursor = getRWDb().rawQuery("select * from " + DB.RecordTableInfo.TBL_NAME + " where date BETWEEN " + dates[0].getTime() + " AND " + dates[1].getTime() +", " + DB.RecordTableInfo.COL_IS_PRIVATE + "=" + "=" + (isPrivate ? 1: 0), null);
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

    public int getRecordsPrice(TimeSearchType timeSearchType) {
        int dayPrice = 0;
        Cursor cursor;
        Date[] dates = null;

        switch (timeSearchType) {
            case DAY:
                dates = getDayDates();
                break;
            case WEEK:
                dates = getWeekDates();
                break;
            case MONTH:
                dates = getMonthDates();
                break;
        }

        try {
            cursor = getRWDb().rawQuery("select sum(cost) from " + DB.RecordTableInfo.TBL_NAME + " where date BETWEEN " + dates[0].getTime() + " AND " + dates[1].getTime(), null);
            cursor.moveToFirst();
            dayPrice = cursor.getInt(0);
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException getRecordsPrice()", e);
        }
        return dayPrice;
    }

    private Date[] getDayDates() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);

        Date[] dates = new Date[2];
        for (int i = 0; i < 2; i++) {
            dates[i] = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dates;
    }

    private Date[] getWeekDates() {
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

    private Date[] getMonthDates() {
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
        values.put(DB.RecordTableInfo.COL_IS_PRIVATE, record.isPrivate);
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
            getRWDb().delete(DB.RecordTableInfo.TBL_NAME, DB.RecordTableInfo.COL_ID + "=" + recordID, null);
        } catch (SQLException e) {
            Log.e(TAG, "SQLException deleteRecord", e);
        }
    }

}