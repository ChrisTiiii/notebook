package com.example.juicekaaa.notebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.juicekaaa.notebook.mode.IndexMode;

import java.util.List;

/**
 * Created by Juicekaaa on 2017/10/26.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASENAME = "notebook.db";//数据库名
    private static final String USER = "user";//表名
    private static final int version = 1;//版本号
    private SQLiteDatabase sqLiteDatabase = null;
    private Cursor cursor;

    public DatabaseHelper(Context context) {
        super(context, DATABASENAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USER + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT, time TEXT, address Text, type Text);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(IndexMode indexMode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", indexMode.getTitle());
        contentValues.put("content", indexMode.getContent());
        contentValues.put("time", indexMode.getTime());
        contentValues.put("address", indexMode.getAddress());
        contentValues.put("type", indexMode.getType());
        getWritableDatabase().insert(USER, indexMode.getTitle(), contentValues);

    }


    public void updateData(IndexMode indexMode) {
        sqLiteDatabase = getWritableDatabase();
        String sql = "update " + USER + " set title = '" + indexMode.getTitle() + "',content = '" + indexMode.getContent() + "' where _id = " +
                indexMode.get_id();
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
    }


    public List<IndexMode> searchIndex(List<IndexMode> list, String type) {
        sqLiteDatabase = getWritableDatabase();
        if (type == "默认")
            cursor = sqLiteDatabase.rawQuery("select title , time ,_id from " + USER +" order by _id desc", null);
        else
            cursor = sqLiteDatabase.rawQuery("select title , time ,_id from " + USER +" where type ='" + type + "'"+" order by _id desc", null);
        while (cursor.moveToNext()) {
            IndexMode mIndexMode = new IndexMode();
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            mIndexMode.set_id(_id);
            mIndexMode.setTitle(title);
            mIndexMode.setTime(time);
            list.add(mIndexMode);
        }
        sqLiteDatabase.close();
        return list;
    }


    public void deleteIndex(int _id) {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("delete from " + USER + " where _id =" + _id);
        sqLiteDatabase.close();

    }

    public IndexMode oneData(int _id) {
        IndexMode indexMode = new IndexMode();
        sqLiteDatabase = getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("select title,content,address from " + USER + " where _id = " + _id, null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            Log.e("content", content);
            String address = cursor.getString(cursor.getColumnIndex("address"));
            indexMode.setTitle(title);
            indexMode.setContent(content);
            indexMode.setAddress(address);
        }
        sqLiteDatabase.close();
        return indexMode;
    }


}
