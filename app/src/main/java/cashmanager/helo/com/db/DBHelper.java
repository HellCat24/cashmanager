package cashmanager.helo.com.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cashmanager.helo.com.Consts;
import cashmanager.helo.com.db.data.BudgetData;
import cashmanager.helo.com.db.data.CategoryData;
import cashmanager.helo.com.db.data.RecordsData;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();

    private static DBHelper mInstance = null;

    private SQLiteDatabase mDatabase;

    private static final int DB_VERSION = 1;

    private RecordsData mRecordsDataSource;
    private BudgetData mBudgetDataSource;
    private CategoryData mCategoryData;

    private DBHelper(Context context) {
        super(context, Consts.DB_PATH, null, DB_VERSION);

        mRecordsDataSource = new RecordsData(this);
        mBudgetDataSource = new BudgetData(this);
        mCategoryData = new CategoryData(this);
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
        mDatabase.execSQL(DB.BUDGET_TABLE);

        onUpgrade(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public RecordsData getRecordsDataSource() {
        return mRecordsDataSource;
    }

    public BudgetData getBudgetDataSource() {
        return mBudgetDataSource;
    }

    public CategoryData getCategoryData() {
        return mCategoryData;
    }
}
