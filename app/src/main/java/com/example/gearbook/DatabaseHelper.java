package com.example.gearbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jason Zhao on Sept/20/2020.
 * Copyright is reserved
 */

//In class does not link to any activity, rather, it handles all the data exchanges with a SQL Database.

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
        //Create a Table with SQL
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, maker TEXT, description TEXT, price TEXT, comment TEXT)";
        db.execSQL(createTable);
    }

    //don't need this function, but still need to initialize it
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
    //add of the columns into the table, and return whether it is successful,
    //reference to https://github.com/mitchtabian/SaveReadWriteDeleteSQLite/blob/master/SaveAndDisplaySQL/app/src/main/java/com/tabian/saveanddisplaysql/EditDataActivity.java
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
    /**
     * get all the data  of the item
     * @param
     * @return data
     *
     */

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * get ID of the item
     * @param  selectedItem
     * @return data(ID)
     *
     */
    public Cursor getItemID(ArrayList<String> selectedItem){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL4  + " = ?" + " AND " + COL5 + " = ?" + "";
        Cursor data = db.rawQuery(query, new String[]{selectedItem.get(1),selectedItem.get(2)});
        return data;
    }

    /**
     * get maker and comment of the item
     * @param  selectedItem
     * @return data(maker and comment)
     *
     */
    public Cursor getMakerCom(ArrayList<String> selectedItem){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + "*" + " FROM " + TABLE_NAME +
                " WHERE " + COL4  + " = ?" + " AND " + COL5 + " = ?" + "";
        Cursor data = db.rawQuery(query, new String[]{selectedItem.get(1),selectedItem.get(2)});
        return data;
    }

    /**
     * Calculate total price
     * @param
     * @return cursor data
     *
     */

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
     * Updates the all fields
     * @param price, id, newDate,newMaker,newDescription, newPrice, newComment
     *
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
     *
     */
    public void deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }


}
