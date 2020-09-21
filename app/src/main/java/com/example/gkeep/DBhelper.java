package com.example.gkeep;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBhelper extends SQLiteOpenHelper {
   private  static final String TABLE_NAME = "items_table";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "items_title";
    private static final String COL_VALUE = "items_value";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_TITLE + " TEXT,"
            + COL_VALUE + " TEXT)";


    public DBhelper(@Nullable Context context) {
        super(context, "itemsDetails.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertDataToDatabase(SQLiteDatabase database, String itemTitle, String items){
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE,itemTitle);
        cv.put(COL_VALUE,items);

        database.insert(TABLE_NAME,null,cv);
    }
    public void updateDataToDatabase(SQLiteDatabase database,String itemTitle, String items, int id){
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE,itemTitle);
        cv.put(COL_VALUE,items);
        database.update(TABLE_NAME,cv,COL_ID + "=" + id,null);
    }

    public ArrayList<taskDetail> getDataFromDatabase(SQLiteDatabase database){
        ArrayList<taskDetail> taskDetails = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
                String value = cursor.getString(cursor.getColumnIndex(COL_VALUE));

                taskDetail tasks = new taskDetail();
                tasks.taskId = id;
                tasks.taskTitle = title;
                tasks.taskValue = value;
                taskDetails.add(tasks);

            }while (cursor.moveToNext());
        }
        return taskDetails;
    }
    public void deleteDataFromDatabase(SQLiteDatabase database,taskDetail taskdetail){
        database.delete(TABLE_NAME,COL_ID+" = "+ taskdetail.taskId,null);
    }


}
