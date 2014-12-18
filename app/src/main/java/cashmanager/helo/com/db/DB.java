package cashmanager.helo.com.db;

public class DB {

    public static final String RECORD_TABLE = " CREATE TABLE "
            + RecordTableInfo.TBL_NAME + " ( "
            + RecordTableInfo.COL_ID + " INTEGER PRIMARY KEY NOT NULL, "
            + RecordTableInfo.COL_DATE + " INTEGER, "
            + RecordTableInfo.COL_DESCRIPTION + " VARCHAR(255), "
            + RecordTableInfo.COL_COST + " INTEGER, "
            + RecordTableInfo.COL_CATEGORY_ID + " INTEGER, "
            + RecordTableInfo.COL_IS_PRIVATE + " INTEGER, "
            + RecordTableInfo.COL_PARENT_ID + " INTEGER, "
            + RecordTableInfo.IS_SUB_RECORD + " INTEGER, "
            + RecordTableInfo.COL_FILE_PATH + " VARCHAR(255) "
            + " );";

    public static final String CATEGORY_TABLE = " CREATE TABLE "
            + CategoryTableInfo.TBL_NAME + " ( "
            + CategoryTableInfo.COL_ID + " INTEGER PRIMARY KEY NOT NULL, "
            + CategoryTableInfo.COL_TITLE + " VARCHAR(255), "
            + CategoryTableInfo.COL_DESCRIPTION + " VARCHAR(255) "
            + " );";

    public static final String ATTACHMENT_TABLE = " CREATE TABLE "
            + AttachmentTableInfo.TBL_NAME + " ( "
            + AttachmentTableInfo.COL_ID + " INTEGER PRIMARY KEY NOT NULL, "
            + AttachmentTableInfo.COL_RECORD_ID + " INTEGER, "
            + AttachmentTableInfo.COL_TITLE + " VARCHAR(255), "
            + AttachmentTableInfo.COL_FILE_PATH + " VARCHAR(255) "
            + " );";

    public static final String RECORD_CATEGORY_TABLE = " CREATE TABLE "
            + RecordCategoryTableInfo.TBL_NAME + " ( "
            + RecordCategoryTableInfo.COL_ID + " INTEGER PRIMARY KEY NOT NULL, "
            + RecordCategoryTableInfo.COL_RECORD_ID + " INTEGER, "
            + RecordCategoryTableInfo.COL_CATEGORY_ID + " VARCHAR(255) "
            + " );";

    public static final String BUDGET_TABLE = " CREATE TABLE "
            + BudgetTableInfo.TBL_NAME + " ( "
            + BudgetTableInfo.COL_ID + " INTEGER PRIMARY KEY NOT NULL, "
            + BudgetTableInfo.COL_DATE + " INTEGER, "
            + BudgetTableInfo.COL_VALUE + " INTEGER "
            + " );";

    public static final class RecordTableInfo {
        public static final String TBL_NAME = "records";
        public static final String COL_ID = "id";
        public static final String COL_DATE = "date";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_COST = "cost";
        public static final String COL_CATEGORY_ID = "category_id";
        public static final String COL_IS_PRIVATE = "is_public";
        public static final String COL_FILE_PATH = "file_path";

        public static final String COL_PARENT_ID = "parent_id";
        public static final String IS_SUB_RECORD = "is_sub_record";
    }

    public static final class CategoryTableInfo {
        public static final String TBL_NAME = "category";
        public static final String COL_ID = "id";
        public static final String COL_TITLE = "title";
        public static final String COL_DESCRIPTION = "description";
    }

    public static final class RecordCategoryTableInfo {
        public static final String TBL_NAME = "record_category";
        public static final String COL_ID = "id";
        public static final String COL_RECORD_ID = "record_id";
        public static final String COL_CATEGORY_ID = "category_id";
    }

    public static final class AttachmentTableInfo {
        public static final String TBL_NAME = "attachment";
        public static final String COL_ID = "id";
        public static final String COL_RECORD_ID = "record_id";
        public static final String COL_TITLE = "title";
        public static final String COL_FILE_PATH = "file_path";
    }

    public static final class BudgetTableInfo {
        public static final String TBL_NAME = "budget";
        public static final String COL_ID = "id";
        public static final String COL_DATE = "date";
        public static final String COL_VALUE = "value";
    }
}