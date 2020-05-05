package com.example.currencyconvertor.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper
{
    private static DBManager dbManager;
    private SQLiteDatabase db;

    public static final String DB_NAME = "CurreniesDB";
    public static final int DB_VERSION = 1;


    public static final String CURRENCY_NAMES_TABLE = "currency_names";
    public static final String ROW_ID_COLUMN = "row_id";


    public static final String RELATIVE_RATES_TABLE = "relative_rates";
    public static final String CURRENCY_NAMES_COLUMN = "currency";
    public static final String CURRENCY_RATES_COLUMN = "rate";
    public static final String CURRENCY_IS_CHOSEN_COLUMN = "is_chosen";


    private DBManager(WeakReference<Context> context)
    {
        super(context.get(),DB_NAME,null,DB_VERSION);
        db = getWritableDatabase();
    }

    public static synchronized DBManager getInstance(WeakReference<Context> context)
    {
        if (dbManager == null)
        {
            dbManager = new DBManager(context);
        }
        return dbManager;
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String currencyNamesTableQuery = "create table "
                + CURRENCY_NAMES_TABLE + " ("
                + ROW_ID_COLUMN
                + " integer primary key"
                + " autoincrement not null,"
                + CURRENCY_NAMES_COLUMN
                + " text not null);";

        db.execSQL(currencyNamesTableQuery);

        String currencyRatesTableQuery = "create table "
                + RELATIVE_RATES_TABLE + " ("
                + CURRENCY_NAMES_COLUMN
                + " text primary key not null,"
                + CURRENCY_RATES_COLUMN
                + " real not null,"
                + CURRENCY_IS_CHOSEN_COLUMN
                + " integer not null);";
        db.execSQL(currencyRatesTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    public void insert(String tableName, String nullColumnHack ,ContentValues contentValues)
    {
        db.insert(tableName, nullColumnHack, contentValues);
    }

    public void delete (String tableName, String whereClause, String[] whereArgs)
    {
        db.delete(tableName, whereClause, whereArgs);
    }

    public void deleteAll (String tableName)
    {
        db.delete(tableName,null,null);
    }

    public List<ContentValues> readRawsQuery(String tableName, String[] selectionArgs)
    {
        String selectAllQuery = "SELECT * FROM " +tableName;
        Cursor cursor = db.rawQuery(selectAllQuery,selectionArgs);
        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();
        while (cursor.moveToNext())
        {
            ContentValues contentValues = new ContentValues();
            switch (tableName)
            {
                case CURRENCY_NAMES_TABLE:
                {
                    contentValues.put(CURRENCY_NAMES_COLUMN, cursor.getString(cursor.getColumnIndex(CURRENCY_NAMES_COLUMN)));
                    break;
                }
                case RELATIVE_RATES_TABLE:
                {
                    contentValues.put(CURRENCY_NAMES_COLUMN, cursor.getString(cursor.getColumnIndex(CURRENCY_NAMES_COLUMN)));
                    contentValues.put(CURRENCY_RATES_COLUMN, cursor.getDouble(cursor.getColumnIndex(CURRENCY_RATES_COLUMN)));
                    contentValues.put(CURRENCY_IS_CHOSEN_COLUMN, cursor.getInt(cursor.getColumnIndex(CURRENCY_IS_CHOSEN_COLUMN)));
                }
            }
            contentValuesList.add(contentValues);
        }
        return contentValuesList;
    }

  //  String countryName = BaseCurrencyInfo.RelativeCurrencyAdministrativeData.getCountryNameFromCurrency(currency);
//    String flagUrl = BaseCurrencyInfo.RelativeCurrencyAdministrativeData.getFlagUrl(currency);

    public void replaceAll(String tableName, List<ContentValues> contentValueList )
    {
        db.beginTransaction();
        try
        {
            db.delete(tableName, null, null);
            for (ContentValues contentValues : contentValueList)
            {
                db.insert(tableName, null, contentValues);
            }
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
    }
    @Override
    protected void finalize() throws Throwable
    {
        Log.e("Finalize","Finalize");

        super.finalize();
        db.close();
    }
}
