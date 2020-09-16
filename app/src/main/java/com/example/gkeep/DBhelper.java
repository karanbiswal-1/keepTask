package com.example.gkeep;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
}
