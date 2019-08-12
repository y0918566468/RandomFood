package com.example.randomfood;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RF.db";
    private static final int VERSION = 1;
    private Context context;

    public MyDBHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE food" + "(_id INTEGER PRIMARY KEY NOT NULL," +
                "foodWords TEXT UNIQUE NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public int deleteData(String words){
        SQLiteDatabase db = this.getWritableDatabase();

        //三種處理方式 1.SQLite指令並且使用佔位符引號 2.內建Delete單引號解決 3.利用佔位符引述解決引號問題
        //db.execSQL("DELETE FROM food WHERE foodWords = ?", new Object[]{words});
        //return db.delete("food", "foodWords = '" + words + "'", null );
        return db.delete("food", "foodWords = ?", new String[]{words} );

    }

    public List<String> getAllLabels(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + "food";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;

    }

    public List<FoodData> getFoddData(){
        List<FoodData> list = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + "food";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(new FoodData(cursor.getInt(0), cursor.getString(1)));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;

    }

    public int getDb_IsNull(){
        String selectQuery = "SELECT  * FROM " + "food";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor.getCount();
    }
}
