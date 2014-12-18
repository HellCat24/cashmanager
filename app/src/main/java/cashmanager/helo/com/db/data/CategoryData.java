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
import cashmanager.helo.com.model.bd.Category;

/**
 * Created by Oleg on 19.12.2014.
 */
public class CategoryData extends DataSource {

    public static final int EMPTY_BUDGET = 0;

    private static final int CURRENT_BUDGET = 1;

    private static final String TAG = CategoryData.class.getSimpleName();

    public CategoryData(DBHelper dbHelper) {
        super(dbHelper);
    }

    public int getCurrentBudget() {
        Budget budget = null;
        Cursor cursor;
        try {
            cursor = getRWDb().query(DB.BudgetTableInfo.TBL_NAME, null, DB.BudgetTableInfo.COL_ID + " = " + CURRENT_BUDGET, null, null, null, null);
            if (cursor.moveToFirst()) {
                budget = new Budget(cursor);
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e(TAG, "SQLException getBudgetList()", e);
        }
        if (budget != null) {
            return budget.value;
        } else {
            return EMPTY_BUDGET;
        }
    }

    public List<Category> getCategoryList() {
        List<Category> categories = new ArrayList<Category>();
        Cursor cursor;
        try {
            cursor = getRWDb().query(DB.CategoryTableInfo.TBL_NAME, null,null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Category category = new Category(cursor);
                categories.add(category);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException getCategoryList()", e);
        }
        return categories;
    }

    public String[] getCategoryTitleList() {
        String[] categories = null;
        Cursor cursor;
        try {
            cursor = getRWDb().query(DB.CategoryTableInfo.TBL_NAME, null,null, null, null, null, null);
            cursor.moveToFirst();
            int i = 0;
            categories = new String[cursor.getCount()];
            while (!cursor.isAfterLast()) {
                categories[i] = cursor.getString(1);
                cursor.moveToNext();
                i++;
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException getCategoryTitleList()", e);
        }
        return categories;
    }

    public void addCategory(String title) {
        ContentValues values = new ContentValues();

        values.put(DB.CategoryTableInfo.COL_TITLE, title);

        try {
            getRWDb().insert(DB.CategoryTableInfo.TBL_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "SQLException addCategory()", e);
        }
    }

    public void deleteCategory(int categoryId) {
        try {
            getRWDb().delete(DB.CategoryTableInfo.TBL_NAME, DB.CategoryTableInfo.COL_ID, new String[]{String.valueOf(categoryId)});
        } catch (SQLException e) {
            Log.e(TAG, "SQLException deleteCategory()", e);
        }
    }

}
