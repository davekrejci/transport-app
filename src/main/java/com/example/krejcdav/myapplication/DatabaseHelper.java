package com.example.krejcdav.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import java.util.concurrent.ThreadLocalRandom;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "record_database";
    private static final String DB_TABLE_NAME = "record";
    private static final String DB_COLUMN_ID = "id";
    private static final String DB_COLUMN_DATE = "date";
    private static final String DB_COLUMN_TYPE = "type";
    private static final String DB_COLUMN_PRICE = "price";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + DB_TABLE_NAME + " (" +
                DB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DB_COLUMN_DATE + " INTEGER, " +
                DB_COLUMN_TYPE + " INTEGER, " +
                DB_COLUMN_PRICE + " INTEGER" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
        onCreate(database);
    }

    public boolean saveToDatabase(Context context,TransportType transportType) {
        SQLiteDatabase database = this.getWritableDatabase();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.DB_COLUMN_DATE, System.currentTimeMillis());
        values.put(DatabaseHelper.DB_COLUMN_TYPE, transportType.getValue());
        if (transportType == TransportType.TRAM){
            values.put(
                    DatabaseHelper.DB_COLUMN_PRICE,
                    Integer.parseInt(sharedPreferences.getString(context.getString(R.string.pref_tram_price),"0")));
            }
        else if (transportType == TransportType.BUS){
            values.put(
                    DatabaseHelper.DB_COLUMN_PRICE,
                    Integer.parseInt(sharedPreferences.getString(context.getString(R.string.pref_bus_price),"0")));
        }
        else return false;

        long newRowId = database.insert(DatabaseHelper.DB_TABLE_NAME, null, values);
        if(newRowId == -1)
            return false;
        else
            return true;

    }
    public Cursor getAllRows(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM " + DB_TABLE_NAME,null);
        return data;
    }
    public Cursor getMonthlySums(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor data = database.rawQuery("SELECT strftime('%m %Y', datetime(date/1000, 'unixepoch')) as Month, SUM(price) as TotalPerMonth FROM record GROUP BY Month",null);
        return data;
    }
    public Cursor getDayOfWeekSums(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor data = database.rawQuery("SELECT strftime('%w', datetime(date/1000, 'unixepoch')) as Day, SUM(price) as TotalPerDay FROM record GROUP BY Day",null);
        return data;
    }
    public void removeLastRow() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + DB_TABLE_NAME + " WHERE "+ DB_COLUMN_ID + " = (SELECT MAX("+ DB_COLUMN_ID + ") FROM " + DB_TABLE_NAME + ")");
    }
    public void removeAllRows(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + DB_TABLE_NAME);
    }
    public void insertRandomRows(int rowCount){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 0; i < rowCount; i++) {
            values.put(
                    DatabaseHelper.DB_COLUMN_DATE, 
                    randomNumberInRange(System.currentTimeMillis(), System.currentTimeMillis()+Long.parseLong("9999999999"))
            );
            values.put(
                    DatabaseHelper.DB_COLUMN_TYPE, 
                    randomNumberInRange(0,1)
            );
            values.put(
                    DatabaseHelper.DB_COLUMN_PRICE,
                    randomNumberInRange(15,50)
            );
            database.insert(DatabaseHelper.DB_TABLE_NAME, null, values);
            values.clear();
        }
    }
    private int randomNumberInRange(int lowerBound, int upperBound){
        return ThreadLocalRandom.current().nextInt(lowerBound, upperBound);
    }
    private long randomNumberInRange(long lowerBound, long upperBound){
        return ThreadLocalRandom.current().nextLong(lowerBound, upperBound);
    }

}
