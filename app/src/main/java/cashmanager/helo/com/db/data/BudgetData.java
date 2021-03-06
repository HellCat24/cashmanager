package cashmanager.helo.com.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cashmanager.helo.com.db.DB;
import cashmanager.helo.com.db.DBHelper;
import cashmanager.helo.com.db.data.base.DataSource;
import cashmanager.helo.com.model.bd.Budget;

/**
 * Created by tac on 11/25/14.
 */
public class BudgetData extends DataSource {

    private static final int CURRENT_BUDGET = 0;

    private static final String TAG = RecordsData.class.getSimpleName();

    public BudgetData(DBHelper dbHelper) {
        super(dbHelper);
    }

    public int getCurrentBudget() {
        Budget budget = null;
        Cursor cursor;
        try {
            cursor = getRWDb().query(DB.BudgetTableInfo.TBL_NAME, null, DB.BudgetTableInfo.COL_ID, new String[]{String.valueOf(CURRENT_BUDGET)}, null, null, null);
            cursor.moveToFirst();
            budget = new Budget(cursor);
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException getBudgetList()", e);
        }
        if (budget != null) {
            return budget.value;
        } else {
            return 0;
        }
    }

    public List<Budget> getBudgetList() {
        List<Budget> budgets = new ArrayList<Budget>();
        Cursor cursor;
        try {
            cursor = getRWDb().query(DB.BudgetTableInfo.TBL_NAME, null, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Budget budget = new Budget(cursor);
                budgets.add(budget);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException getBudgetList()", e);
        }
        return budgets;
    }

    public void addBudgetRecord(Budget budget) {
        ContentValues values = new ContentValues();

        values.put(DB.BudgetTableInfo.COL_DATE, budget.date.getTime());
        values.put(DB.BudgetTableInfo.COL_VALUE, budget.value);

        try {
            getRWDb().insert(DB.RecordTableInfo.TBL_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "SQLException addBudgetRecord()", e);
        }
    }

    public void deleteBudgetRecord(int recordID) {
        try {
            getRWDb().delete(DB.BudgetTableInfo.TBL_NAME, DB.BudgetTableInfo.COL_ID, new String[]{String.valueOf(recordID)});
        } catch (SQLException e) {
            Log.e(TAG, "SQLException deleteBudgetRecord()", e);
        }
    }

}
