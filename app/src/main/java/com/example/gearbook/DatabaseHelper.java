package com.example.gearbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG ="DatabaseHelper";

    private static final String TABLE_NAME ="gear_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "date";
    private static final String COL3 = "maker";
    private static final String COL4 = "description";
    private static final String COL5 = "price";
    private static final String COL6 = "comment";




    public DatabaseHelper( Context context) {
        super(context, TABLE_NAME,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL2 +" TEXT,"+ COL3 + "TEXT)";
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, maker TEXT, description TEXT, price TEXT, comment TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public boolean addData(String date,String maker,String description,String price, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, date);
        contentValues.put(COL3, maker);
        contentValues.put(COL4, description);
        contentValues.put(COL5, price);
        contentValues.put(COL6, comment);



        Log.d(TAG, "addData: Adding " + comment + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getItemID(ArrayList<String> selectedItem){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL4  + " = ?" + " AND " + COL5 + " = ?" + "";
        Cursor data = db.rawQuery(query, new String[]{selectedItem.get(1),selectedItem.get(2)});
        return data;
    }
    public Cursor getMakerCom(ArrayList<String> selectedItem){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + "*" + " FROM " + TABLE_NAME +
                " WHERE " + COL4  + " = ?" + " AND " + COL5 + " = ?" + "";
        Cursor data = db.rawQuery(query, new String[]{selectedItem.get(1),selectedItem.get(2)});
        return data;
    }


    public Cursor getTotalPrice() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT SUM(price)" + " FROM " + TABLE_NAME;
        Log.d(TAG, "get total price: query: " + query);
        Cursor data = db.rawQuery(query, null);

        return data;

    }

//    public Cursor getMakerCom(int id ){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String IdSting= Integer.toString(id);
//        String query = "SELECT " + COL3 +","+ COL6 +" FROM " + TABLE_NAME +
//                " WHERE " + COL1  + "=?"+ "";
//        Cursor makerComGot = db.rawQuery(query, new String[]{IdSting });
//        return makerComGot;
//    }
//    public Cursor getMakerCom(int id ){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String IdSting= Integer.toString(id);
//        String query = "SELECT " + COL3 +" FROM " + TABLE_NAME +
//                " WHERE " + COL1  + "=?"+ "";
//        Cursor makerComGot = db.rawQuery(query, new String[]{IdSting });
//        return makerComGot;
//    }

    /**
     * Updates the name field
     * @param
     * @param id
     * @param
     */
    public void updateName( String price, int id, String newDate,String newMaker,String newDescription,String newPrice, String newComment){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newDate +"'," + COL3 + "='"+ newMaker +"'," + COL4 + "='"+ newDescription +"'," + COL5 + "='"+ newPrice +"'," + COL6 + "='"+ newComment    + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL5 + " = '" + price + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting new to " + newDate+newDescription+newComment+newMaker+newPrice);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }


}
