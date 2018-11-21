package com.jeffryRaymond.rememberYou.data.database.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jeffryRaymond.rememberYou.data.database.models.Message;
import com.jeffryRaymond.rememberYou.data.database.models.TaskItem;
import com.jeffryRaymond.rememberYou.interfaces.Communicator;

import java.util.ArrayList;
import java.util.List;

/**
 * This class deals with the database, holding, setting, and retrieving data.
 */

public class DataBaseHelper implements Communicator {
    private DBHelper mDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public DataBaseHelper(Context context) {
        mDBHelper = new DBHelper(context);
    }

    //Function to insert a category
    public long insertTaskCategory(String categoryTitle) {
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_TASK_TITLE, categoryTitle);
        return mSQLiteDatabase.insert(DBHelper.TABLE_TASK_LABEL, null, contentValues);
    }

    //Function to insert username and pin
    public long insertUserNameAndPin(String userName, String userPin, int userGoal) {
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN__USERNAME, userName);
        contentValues.put(DBHelper.COLUMN_USERPIN, userPin);
        contentValues.put(DBHelper.COLUMN_USER_GOAL, userGoal);
        long id = mSQLiteDatabase.insert(DBHelper.TABLE_USER_INFO, null, contentValues);
        return id;
    }

    //Function to get the category number
    public int getCategoryNumber(String categoryName) {
        int ID = 0;
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String sql = "SELECT * FROM tasksLabel WHERE title = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{categoryName});
        while (cursor.moveToNext()) {
            ID = cursor.getInt(cursor.getColumnIndex("_id"));
        }
        cursor.close();
        return ID;

    }

    //Get all data and return in a list
    public List<TaskItem> getData() {
        List<TaskItem> data = new ArrayList<>();
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String sql = "SELECT * FROM taskTable";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex("taskID"));
            String title = cursor.getString(cursor.getColumnIndex("task_title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            long categoryId = cursor.getInt(cursor.getColumnIndex("_id"));
            TaskItem currentTask = new TaskItem();
            currentTask.setTaskID(id);
            currentTask.setTaskTitle(title);
            currentTask.setTaskDescription(description);
            currentTask.setTaskCategoryId(categoryId);
            data.add(currentTask);
        }
        cursor.close();
        return data;
    }

    //Function to get userName and Password
    public List<TaskItem> getUserNameAndPassWord() {
        List<TaskItem> data = new ArrayList<>();
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String sql = "SELECT * FROM tableUserInfo";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex("id"));
            String userName = cursor.getString(cursor.getColumnIndex("userName"));
            String passWord = cursor.getString(cursor.getColumnIndex("userPin"));
            TaskItem currentTask = new TaskItem();
            currentTask.setTaskID(id);
            currentTask.setUserName(userName);
            currentTask.setUserPinCode(passWord);
            data.add(currentTask);
        }
        cursor.close();
        return data;
    }

    //Function to get user name information
    public List<TaskItem> getUserNameInfo() {
        List<TaskItem> userNameList = new ArrayList<>();
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String sql = "SELECT * FROM tableUserInfo";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String userName = cursor.getString(cursor.getColumnIndex("userName"));
            TaskItem currentTask = new TaskItem();
            currentTask.setUserName(userName);
            userNameList.add(currentTask);
        }
        cursor.close();
        return userNameList;
    }


    //Function to get tasks from a specific category
    public List<TaskItem> getSpecificData(int ID) {
        List<TaskItem> data = new ArrayList<>();
        //System.out.print("title name: " + titleName);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String sql = "SELECT * FROM taskTable WHERE _id =?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(ID)});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("taskID"));
            String name = cursor.getString(cursor.getColumnIndex("task_title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            int categoryId = cursor.getInt(cursor.getColumnIndex("_id"));
            TaskItem currentTask = new TaskItem();
            currentTask.setTaskID(id);
            currentTask.setTaskTitle(name);
            currentTask.setTaskDescription(description);
            currentTask.setTaskCategoryId(categoryId);
            data.add(currentTask);
        }
        cursor.close();
        return data;
    }


    //Function to get deleted tasks from a specific category
    public List<TaskItem> getDeletedTasks(int ID) {
        List<TaskItem> data = new ArrayList<>();
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String sql = "SELECT * FROM tableDeletedTasks WHERE _id =?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(ID)});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("deletedTaskID"));
            String name = cursor.getString(cursor.getColumnIndex("deletedTitle"));
            String description = cursor.getString(cursor.getColumnIndex("deletedDescription"));
            int categoryId = cursor.getInt(cursor.getColumnIndex("_id"));
            TaskItem currentTask = new TaskItem();
            currentTask.setTaskID(id);
            currentTask.setTaskTitle(name);
            currentTask.setTaskDescription(description);
            currentTask.setTaskCategoryId(categoryId);
            data.add(currentTask);
        }
        cursor.close();
        return data;
    }

    //Function to get id from category name
    public int getSpinnerId(String spinnerTitle) {
        int id = 0;
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String sql = "SELECT _id FROM tasksLabel WHERE title =?";
        Cursor cursor = db.rawQuery(sql, new String[]{spinnerTitle});
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("_id"));
        }
        cursor.close();
        return id;
    }

    //Function to update the task
    public boolean updateData(String oldTitle, String newTitle, String newDescription) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mDBHelper.C_TASK_TITLE, newTitle);
        contentValues.put(mDBHelper.C_TASK_DESCRIPTION, newDescription);
        db.update(mDBHelper.TASK_TABLE, contentValues, "task_title = ?", new String[]{oldTitle});
        return true;
    }

    //Functiion to update the category name
    public boolean updateCategoryName(String oldCategoryName, String newCategoryName) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mDBHelper.COLUMN_TASK_TITLE, newCategoryName);
        db.update(mDBHelper.TABLE_TASK_LABEL, contentValues, "title =?", new String[]{oldCategoryName});
        return true;
    }

    //Function for deleting a task
    public boolean deleteData(String t_title) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        long id = db.delete(mDBHelper.TASK_TABLE, "task_title = ?", new String[]{t_title});
        return true;
    }

    //Function for deleting categories
    public boolean deleteCategory(String categoryTitle) {
        boolean deleted = false;
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        long id = db.delete(mDBHelper.TABLE_TASK_LABEL, "title =?", new String[]{categoryTitle});
        if (id > 0) {
            deleted = true;
        }
        return deleted;
    }

    //Function to get all data
    public String getAllData() {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String sql = "SELECT * FROM taskTable";
        Cursor cursor = db.rawQuery(sql, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("task_title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            buffer.append(title + " and " + description + "\n");
        }
        cursor.close();
        return buffer.toString();
    }

    public ArrayList<String> getSpinnerData() {
        ArrayList<String> spinnerList = new ArrayList<String>();
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String sql = "SELECT * FROM tasksLabel";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            spinnerList.add(title);
        }
        cursor.close();
        return spinnerList;
    }

    //Function for inserting task into a specific category
    public long insertTask(String title, String description, String titleCategory) {
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.C_TASK_TITLE, title);
        contentValues.put(DBHelper.C_TASK_DESCRIPTION, description);
        int categoryNumber = getCategoryNumber(titleCategory);
        contentValues.put(DBHelper.C_TASK_CATEGORY, categoryNumber);
        long id = mSQLiteDatabase.insert(DBHelper.TASK_TABLE, null, contentValues);
        return id;
    }

    //Function to add additional details
    public long addDetailedDescription(String detailedDescription, int taskId) {
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.C_DETAILED_DESCRIPTION, detailedDescription);
        long id = mSQLiteDatabase.update(DBHelper.TASK_TABLE, contentValues, "taskID = ?", new String[]{String.valueOf(taskId)});
        return id;
    }

    //Function for inserting task into a specific category
    public long insertDeletedTask(String title, String description, String titleCategory) {
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.C_DELETED_TITLE, title);
        contentValues.put(DBHelper.C_DELETED_DESCRIPTION, description);
        int categoryNumber = getCategoryNumber(titleCategory);
        contentValues.put(DBHelper.C_DELETED_CATEGORY_ID, categoryNumber);
        long id = mSQLiteDatabase.insert(DBHelper.TABLE_DELETED_TASKS, null, contentValues);
        return id;
    }

    @Override
    public void respond(int position) {
    }

    static class DBHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "tasks.db";
        private static final int DATABASE_VERSION = 75;
        private static final String TAG = "DBHelper";
        private Context mContext;

        //Columns of the user information table
        private static final String TABLE_USER_INFO = "tableUserInfo";
        private static final String USER_ID = "id";
        private static final String COLUMN__USERNAME = "userName";
        private static final String COLUMN_USERPIN = "userPin";
        private static final String COLUMN_USER_GOAL = "userGoal";

        //Columns of the categories table
        private static final String TABLE_TASK_LABEL = "tasksLabel";
        private static final String COLUMN_TASK_ID = "_id";
        private static final String COLUMN_TASK_TITLE = "title";
        private static final String COLUMN_TASK_USER_ID = "id";

        //Columns of the task table
        private static final String TASK_TABLE = "taskTable";
        private static final String C_TASK_ID = "taskID";
        private static final String C_TASK_TITLE = "task_title";
        private static final String C_TASK_DESCRIPTION = "description";
        private static final String C_DETAILED_DESCRIPTION = "detailedDescription";
        private static final String C_TASK_CATEGORY = "_id";
        private static final String COLUMN_COUNTER = "counter";
        private static final String C_TASK_RADIO_B_VAL = "radioButtonValue";

        //Columns of tasks deleted under category table
        private static final String TABLE_DELETED_TASKS = "tableDeletedTasks";
        private static final String DELETED_TASK_ID = "deletedTaskID";
        private static final String C_DELETED_TITLE = "deletedTitle";
        private static final String C_DELETED_DESCRIPTION = "deletedDescription";
        private static final String C_DELETED_CATEGORY_ID = "_id";

        //Table for holding user information
        private static final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + DBHelper.TABLE_USER_INFO + " (" +
                DBHelper.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DBHelper.COLUMN__USERNAME + " TEXT," + DBHelper.COLUMN_USERPIN + " TEXT,"
                + DBHelper.COLUMN_USER_GOAL + " INTEGER)";

        //Table for holding each category of task
        private static final String SQL_CREATE_TABLE_EACH_TASK = "CREATE TABLE " + DBHelper.TABLE_TASK_LABEL + " (" +
                DBHelper.COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DBHelper.COLUMN__USERNAME + " TEXT," +
                DBHelper.COLUMN_USERPIN + " TEXT," + DBHelper.COLUMN_TASK_TITLE + " TEXT," +
                DBHelper.COLUMN_TASK_USER_ID + " INTEGER, FOREIGN KEY (" + DBHelper.COLUMN_TASK_USER_ID + ")" + " REFERENCES " +
                DBHelper.TABLE_USER_INFO + "(" + DBHelper.USER_ID + ")" + ")";

        //Table for holding tasks
        private static final String CREATE_TASK_TABLE = "CREATE TABLE " + DBHelper.TASK_TABLE + "(" +
                DBHelper.C_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBHelper.C_TASK_TITLE + " TEXT," +
                DBHelper.C_TASK_DESCRIPTION + " TEXT," +
                DBHelper.C_DETAILED_DESCRIPTION + " TEXT," +
                DBHelper.COLUMN_COUNTER + " INTEGER," +
                DBHelper.C_TASK_RADIO_B_VAL + " INTEGER," +
                DBHelper.C_TASK_CATEGORY + " INTEGER, FOREIGN KEY (" + DBHelper.C_TASK_CATEGORY + ")" + " REFERENCES " +
                DBHelper.TABLE_TASK_LABEL + "(" + DBHelper.COLUMN_TASK_ID + ")" + ")";

        private static final String CREATE_HISTORY_TABLE = "CREATE TABLE " + DBHelper.TABLE_DELETED_TASKS + "(" +
                DBHelper.DELETED_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBHelper.C_DELETED_TITLE + " TEXT," +
                DBHelper.C_DELETED_DESCRIPTION + " TEXT," +
                DBHelper.C_DELETED_CATEGORY_ID + " INTEGER, FOREIGN KEY (" + DBHelper.C_DELETED_CATEGORY_ID + ")" + " REFERENCES " +
                DBHelper.TABLE_TASK_LABEL + "(" + DBHelper.COLUMN_TASK_ID + ")" + ")";

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
                sqLiteDatabase.execSQL(SQL_CREATE_TABLE_EACH_TASK);
                sqLiteDatabase.execSQL(CREATE_TASK_TABLE);
                sqLiteDatabase.execSQL(CREATE_HISTORY_TABLE);
            } catch (SQLException e) {
                Message.message(mContext, "" + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_INFO);
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_LABEL);
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DELETED_TASKS);
                onCreate(sqLiteDatabase);
            } catch (SQLException e) {
                Message.message(mContext, "" + e);
            }
        }
    }
}

