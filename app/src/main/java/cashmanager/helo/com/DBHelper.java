package cashmanager.helo.com;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();

    private static DBHelper mInstance = null;

    private SQLiteDatabase mDatabase;

    private static final int DB_VERSION = 1;

    private RecordsDataSource mRecordsDataSource;

    private DBHelper(Context context) {
        super(context, Consts.DB_PATH, null, DB_VERSION);

        mRecordsDataSource = new RecordsDataSource(this);
    }

    /**
     * @return can be null
     */
    public static DBHelper get() {
        return mInstance;
    }

    public static void createInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHelper(context);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mDatabase = db;
        mDatabase.execSQL(DB.RECORD_TABLE);
        mDatabase.execSQL(DB.CATEGORY_TABLE);
        mDatabase.execSQL(DB.ATTACHMENT_TABLE);
        mDatabase.execSQL(DB.RECORD_CATEGORY_TABLE);

        onUpgrade(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public RecordsDataSource getRecordsDataSource() {
        return mRecordsDataSource;
    }
}
