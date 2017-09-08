package com.sweettreat.ar.tabsscroller;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "images.db";
    private static final String TABLE_NAME = "table_imgs";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_IMG = "img";
    private static final String[] ALL_COL = {"img"};

    private static final String PRINTTAG = "TESTDBHandler";


    public DBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    String query = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_IMG + " BLOB" +
            ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public List<Bitmap> getImgEntries(){
        List<Bitmap> imgList = new ArrayList<Bitmap>();
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1;";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getBlob(c.getColumnIndex(COLUMN_IMG)) != null){

                byte[] b = c.getBlob(c.getColumnIndex(COLUMN_IMG));
                Bitmap bm = getByteArrayAsBitmap(b);
                imgList.add(bm);
            }
        c.moveToNext();
        }

        c.close();
        return imgList;
    }

    public Cursor getAllRows(){
        String where = null;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(true,TABLE_NAME,ALL_COL,where,null,null,null,null,null);

        if(c != null) {
            c.moveToFirst();
        }
        db.close();
        return c;
    }


    public void addEntry(Bitmap bitmap) throws SQLiteException{

        //convert bitmap to byte array
        byte[] data = getBitmapAsByteArray(bitmap);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IMG, data);
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    //method to delete entry
    public void deleteEntry(Bitmap bitmap) {
        //convert bitmap to byte array
        byte[] data = getBitmapAsByteArray(bitmap);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_IMG + " =\"" + data + "\";");
    }


    public void deleteTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);

    }

    public String databaseToString(){

        String dbString = "";
//        SQLiteDatabase db = getWritableDatabase();
//        //* means select every column, 1 means select every row
//        String query = "SELECT * FROM " + TABLE_
//        Cursor c = db.rawQuery(queNAME + " WHERE 1;";
//ry, null);
////
////        //moves cursor to first row
//        c.moveToFirst();
//
//        while(!c.isAfterLast()){
//
//            String print = Integer.toString(c.getColumnIndex(COLUMN_IMG));
//
//            Log.d(TAG,print);
//
////            if(c.getString(c.getColumnIndex(COLUMN_IMG))!= null){
////                dbString += c.getString(c.getColumnIndex(COLUMN_IMG));
////                dbString += "\n";
////           }
//            c.moveToNext();
//        }
//
//        db.close();


        return dbString;

    }


    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
    private static Bitmap getByteArrayAsBitmap(byte[] data){
        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
        return bitmap;
    }
}
