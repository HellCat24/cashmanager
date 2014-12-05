package cashmanager.helo.com.db.data.base;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

import cashmanager.helo.com.db.DBHelper;

public class DataSource {
    private static final String TAG = DataSource.class.getSimpleName();
    private DBHelper mDBHelper;

    public DataSource() {
    }

    public DataSource(DBHelper dbHelper) {
        mDBHelper = dbHelper;
    }

    public SQLiteDatabase getRWDb() throws SQLException {
            return mDBHelper.getWritableDatabase();
    }

    public SQLiteDatabase getRDb() throws SQLException {
        return mDBHelper.getReadableDatabase();
    }

    public void closeDB() throws SQLException {
        mDBHelper.getWritableDatabase().close();
    }

    protected Cursor getRow(String tableName, String column, String order) throws SQLException {
        return getRDb().rawQuery("select * from " + tableName + "  where " + column + " = ?",
                new String[]{String.valueOf(order)});
    }

    protected Cursor getMaxFieldValue(String tableName, String column) throws SQLException {
        return getRWDb().query(tableName, new String[]{"MAX(" + column + ")"}, null, null, null, null, null);
    }

    protected void deleteAllRecordsInTable(String tableName) {
        try {
            getRWDb().delete(tableName, null, null);
        } catch (SQLException e) {
            Log.e(TAG, "SQLException  deleteAll records "+ tableName);
        }
    }
}