package com.example.currencyconvertor.Relative;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.currencyconvertor.Controllers.DBManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDb
{
    private DBManager dbManager;
    private SQLiteDatabase sqLiteDatabase;
    private WeakReference<Context> context;

    public CurrencyDb(WeakReference<Context> context)
    {
        dbManager = DBManager.getInstance(context);
    }


    public void updateSupportedCurrenciesNamesTable(List<String> currencyNameList)
    {
        List <ContentValues> contentValuesList = new ArrayList<ContentValues>();
        for (String currencyName : currencyNameList)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBManager.CURRENCY_NAMES_COLUMN, currencyName);
            contentValuesList.add(contentValues);
        }
        dbManager.replaceAll(DBManager.CURRENCY_NAMES_TABLE, contentValuesList);
    }

    public void updateSupportedCurrenciesNamesTableMT(final List<String> currencyNameList, final Runnable runnable)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                updateSupportedCurrenciesNamesTable(currencyNameList);
                if (runnable != null)
                {
                    runnable.run();
                }

            }
        }).start();
    }

    public void updateCurrenciesRelativeRateTable(List<RelativeCurrencyInfo> relativeCurrencyInfoList)
    {
        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();
        for (RelativeCurrencyInfo relativeCurrencyInfo : relativeCurrencyInfoList)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBManager.CURRENCY_NAMES_COLUMN, relativeCurrencyInfo.getCurrency());
            contentValues.put(DBManager.CURRENCY_RATES_COLUMN, relativeCurrencyInfo.getRate());
            if (relativeCurrencyInfo.getRate() == 1)
            {
                contentValues.put(DBManager.CURRENCY_IS_CHOSEN_COLUMN, 1);
            }
            else
            {
                contentValues.put(DBManager.CURRENCY_IS_CHOSEN_COLUMN, 0);
            }
            contentValuesList.add(contentValues);
        }
        dbManager.replaceAll(DBManager.RELATIVE_RATES_TABLE, contentValuesList );
    }

    public void updateCurrenciesRelativeRateTableMT(final List<RelativeCurrencyInfo> relativeCurrencyInfoList, final Runnable runnable)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                updateCurrenciesRelativeRateTable(relativeCurrencyInfoList);
                if (runnable != null)
                {
                    runnable.run();
                }

            }
        }).start();

    }


    public void fetchCurrenciesNames(List<CountryCurrencyInfo> countryCurrencyInfoList)
    {
        List<ContentValues> contentValuesList = dbManager.readRawsQuery(DBManager.CURRENCY_NAMES_TABLE, null);
        for (ContentValues contentValues: contentValuesList )
        {
            String currency = (String) contentValues.get(DBManager.CURRENCY_NAMES_COLUMN);
            countryCurrencyInfoList.add(new CountryCurrencyInfo(currency, null, null));
        }
    }

    public void fetchCurrenciesNamesMT(final List<CountryCurrencyInfo> countryCurrencyInfoList, final Runnable runnable)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                fetchCurrenciesNames(countryCurrencyInfoList);

                 if (runnable != null)
        {
            runnable.run();
        }
            }
        }).start();
    }

    public void fetchRelativeCurrenciesRates (List<RelativeCurrencyInfo> relativeCurrencyInfoList, CM.LastChosenCurrency lastChosenCurrencyInDb)
    {
        List<ContentValues> contentValuesList = dbManager.readRawsQuery(DBManager.RELATIVE_RATES_TABLE, null);
        for (ContentValues contentValues: contentValuesList )
        {
            String currency = (String) contentValues.get(DBManager.CURRENCY_NAMES_COLUMN);
            double rate = (double) contentValues.get(DBManager.CURRENCY_RATES_COLUMN);
            int isChosenCurrency = (int) contentValues.get(DBManager.CURRENCY_IS_CHOSEN_COLUMN);

            if (isChosenCurrency == 1)
            {
                lastChosenCurrencyInDb.currency = currency;
            }
            else
            {
                RelativeCurrencyInfo relativeCurrencyInfo = new RelativeCurrencyInfo(currency,null ,null, rate);
                relativeCurrencyInfoList.add(relativeCurrencyInfo);
            }
        }
    }


    public void fetchRelativeRCurrenciesRatesMT (final List<RelativeCurrencyInfo> relativeCurrencyInfoList, final CM.LastChosenCurrency lastChosenCurrencyInDb, final Runnable runnable)
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                fetchRelativeCurrenciesRates(relativeCurrencyInfoList,lastChosenCurrencyInDb);
                if (runnable != null)
                {
                    runnable.run();
                }
            }
        }).start();
    }
}
