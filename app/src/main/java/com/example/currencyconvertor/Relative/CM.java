package com.example.currencyconvertor.Relative;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.currencyconvertor.Controllers.WebRequestHandler;
import com.example.currencyconvertor.DataItems.PageBaseItem;
import com.example.currencyconvertor.DataItems.RelativeCurrencyDataItem;
import com.example.currencyconvertor.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.currencyconvertor.Utils.MathOperations;

import static android.content.Context.MODE_PRIVATE;


public class CM
{
    private WeakReference<Context> context;
    private WeakReference<CM.FetchCurrencyResponseAsync> fetchCurrencyResponseCallBack;
    private CurrencyDb currencyDb;

    List<CountryCurrencyInfo> countryCurrencyInfoList;
    List<PageBaseItem> relativeCurrencyDataItemsList;


    String lastChosenCurrency;
    private static final String LAST_CHOSEN_CURRENCY = "last chosen currency";

    public CM(WeakReference<Context> context, WeakReference<CM.FetchCurrencyResponseAsync> fetchCurrencyResponseCallBack)
    {
        this.context = context;
        this.fetchCurrencyResponseCallBack = fetchCurrencyResponseCallBack;
        this.currencyDb = new CurrencyDb(context);

        this.countryCurrencyInfoList = new ArrayList<CountryCurrencyInfo>();
        this.relativeCurrencyDataItemsList = new ArrayList<PageBaseItem>();
    }


    public void init()
    {
        initializeCurrencyInfoAsync();
    }

    private void initializeCurrencyInfoAsync()
    {
        if (isWeakContextReferenceValid(context))
        {
            WebRequestHandler.getJsonObject(context, initCurrencyInfoHandler, RelativeCurrencyAdministrativeData.CURRENCY_BASE_URL, null, null);
        }
    }

    private void initializeRelativeCurrencyInfoAsync(String currency)
    {
        if (isWeakContextReferenceValid(context))
        {
            String url = createUrlForCurrencyWebRequest(currency);
            WebRequestHandler.getJsonObject(context, relativeCurrencyInfoInitialResponseHandler, url, null, null);
        }
    }

    public void fetchPeriodicRelativeCurrencyInfoAsync(String currency)
    {
        if (isWeakContextReferenceValid(context))
        {
            lastChosenCurrency = currency;
            String url = createUrlForCurrencyWebRequest(currency);
            WebRequestHandler.getJsonObject(context, relativeCurrencyInfoPeriodicResponseHandler, url, null, null);
        }
    }

    public void finishCurrencyInfoHandling()
    {

        if (countryCurrencyInfoList.size() <= 0 || !isWeakContextReferenceValid(fetchCurrencyResponseCallBack))    // country currency supported names list can not be taken both from Web and DB . Stop initialization process
        {
            fetchCurrencyResponseCallBack.get().onInitializationFinished(countryCurrencyInfoList, relativeCurrencyDataItemsList, "");
        }
        else
        {
            boolean isCurrencyExist = false;
            lastChosenCurrency = getLastChosenCurrencyFromSP();


            // check if last chosen currency in SP exist in supported list
            for (CountryCurrencyInfo countryCurrencyInfo : countryCurrencyInfoList)
            {
                String currency = countryCurrencyInfo.getCurrency();
                countryCurrencyInfo.setCountryName(RelativeCurrencyAdministrativeData.getCountryNameFromCurrency(currency));
                countryCurrencyInfo.setFlagUrl(RelativeCurrencyAdministrativeData.getFlagUrl(currency));
                if (lastChosenCurrency.equals(countryCurrencyInfo.getCurrency()))
                {
                    isCurrencyExist = true;
                    break;
                }
            }

            if (!isCurrencyExist)
            {
                lastChosenCurrency = countryCurrencyInfoList.get(0).getCurrency(); // choose a default currency
            }

            initializeRelativeCurrencyInfoAsync(lastChosenCurrency);
        }
    }

    private void onSuccessfulWebCurrencyRatesResponse(JSONObject relativeCurrencyInfoObject, boolean onInit) {
        List<RelativeCurrencyInfo> relativeCurrencyInfoList = new ArrayList<RelativeCurrencyInfo>();
        relativeCurrencyDataItemsList.clear();
        createdCurrenciesRelativeRateListsFromJson(relativeCurrencyInfoObject, relativeCurrencyInfoList);
        //updateDB
        currencyDb.updateCurrenciesRelativeRateTableMT(relativeCurrencyInfoList, null);

        //update last chosen currency in SP
        setLastChosenCurrencyInSharedPreferences(lastChosenCurrency);

        if (isWeakContextReferenceValid(fetchCurrencyResponseCallBack)) {
            if (onInit)  // check if function runs on initialization phase
            {
                // send all prepared data lists via callback
                fetchCurrencyResponseCallBack.get().onInitializationFinished(countryCurrencyInfoList, relativeCurrencyDataItemsList, lastChosenCurrency);
            }
            else
            {
                fetchCurrencyResponseCallBack.get().onPeriodicFetchRelativeCurrencyInfoFinished(relativeCurrencyDataItemsList);
            }
        }
    }

    private void createSupportedCurrenciesListsFromJson(List<String> currenciesNameList, JSONObject baseCurrencyInfoObject)
    {
        try
        {
            //adding first country
            String firstBaseCurrency = baseCurrencyInfoObject.getString("base");
            String firstCountryName = RelativeCurrencyAdministrativeData.getCountryNameFromCurrency(firstBaseCurrency);
            String firstFlagUrl = RelativeCurrencyAdministrativeData.getFlagUrl(firstBaseCurrency);
            countryCurrencyInfoList.add(new CountryCurrencyInfo(firstBaseCurrency, firstCountryName, firstFlagUrl));
            currenciesNameList.add(firstBaseCurrency);

            // add all countries beside the first one that was already added
            JSONObject jsonObject = baseCurrencyInfoObject.getJSONObject("rates");
            Iterator<String> currencyIterator = jsonObject.keys();
            while (currencyIterator.hasNext()) {
                String currency = currencyIterator.next();
                String countryName = RelativeCurrencyAdministrativeData.getCountryNameFromCurrency(currency);
                String flagUrl = RelativeCurrencyAdministrativeData.getFlagUrl(currency);
                countryCurrencyInfoList.add(new CountryCurrencyInfo(currency, countryName, flagUrl));
                currenciesNameList.add(currency);
            }

        } catch (JSONException ex) {

            ex.printStackTrace();

        }
    }


    private void createdCurrenciesRelativeRateListsFromJson(JSONObject relativeCurrencyInfoObject, List<RelativeCurrencyInfo> relativeCurrencyInfoList)
    {
        try
        {
            String baseCurrency = relativeCurrencyInfoObject.getString("base");
            JSONObject ratesJsonObject = relativeCurrencyInfoObject.getJSONObject("rates");
            Iterator<String> currencies = ratesJsonObject.keys();
            relativeCurrencyInfoList.add(new RelativeCurrencyInfo(baseCurrency, RelativeCurrencyAdministrativeData.getCountryNameFromCurrency(baseCurrency), RelativeCurrencyAdministrativeData.getFlagUrl(baseCurrency),1.0));

            while (currencies.hasNext())
            {
                String currencyName = currencies.next();
                if (!currencyName.equals(baseCurrency))
                {
                    String countryName = RelativeCurrencyAdministrativeData.getCountryNameFromCurrency(currencyName);
                    double currencyValue = ratesJsonObject.getDouble(currencyName);
                    currencyValue = MathOperations.randDouble(currencyValue);

                    RelativeCurrencyInfo relativeCurrencyInfo = new RelativeCurrencyInfo(currencyName, countryName, RelativeCurrencyAdministrativeData.getFlagUrl(currencyName), currencyValue);
                    relativeCurrencyInfoList.add(relativeCurrencyInfo);
                    relativeCurrencyDataItemsList.add(new RelativeCurrencyDataItem(relativeCurrencyInfo));
                }
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }



    private void createSupportedCurrenciesListFromDb()
    {
        currencyDb.fetchCurrenciesNamesMT(countryCurrencyInfoList, new Runnable()
        {
            @Override
            public void run()
            {
                if (context.get() != null)
                {
                    ((Activity)context.get()).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            finishCurrencyInfoHandling();
                        }
                    });
                }
            }
        });
    }

    private void createCurrencyRelatedRateListFromDb ()
    {
        final List <RelativeCurrencyInfo> relativeCurrencyInfoList = new ArrayList<RelativeCurrencyInfo>();
        final LastChosenCurrency lastChosenCurrencyInDb = new LastChosenCurrency("");

        currencyDb.fetchRelativeRCurrenciesRatesMT(relativeCurrencyInfoList, lastChosenCurrencyInDb, new Runnable()
        {
            @Override
            public void run()
            {
                if (isWeakContextReferenceValid(context)  && lastChosenCurrencyInDb.currency.equals(lastChosenCurrency)) // checks if data in DB is consistent with data in last chosen currency
                {
                    setLastChosenCurrencyInSharedPreferences(lastChosenCurrencyInDb.currency);
                    for (RelativeCurrencyInfo relativeCurrencyInfo : relativeCurrencyInfoList)
                    {
                        String currency = relativeCurrencyInfo.getCurrency();
                        relativeCurrencyInfo.setCountryName(RelativeCurrencyAdministrativeData.getCountryNameFromCurrency(currency));
                        relativeCurrencyInfo.setFlagUrl(RelativeCurrencyAdministrativeData.getFlagUrl(currency));
                        relativeCurrencyDataItemsList.add(new RelativeCurrencyDataItem(relativeCurrencyInfo));
                    }
                }
                else
                {
                    setLastChosenCurrencyInSharedPreferences(countryCurrencyInfoList.get(0).getCurrency());
                }

                ((Activity) (context.get())).runOnUiThread(new Runnable()
            {
                @Override
                public void run() {
                    fetchCurrencyResponseCallBack.get().onInitializationFinished(countryCurrencyInfoList, relativeCurrencyDataItemsList, lastChosenCurrencyInDb.currency);
                }
            });
            }
        });
    }

    private String createUrlForCurrencyWebRequest(String currency)
    {
        StringBuilder url = new StringBuilder(RelativeCurrencyAdministrativeData.CURRENCY_BASE_URL);
        url.append("?base=" + currency);
        return (url.toString());
    }

    private String getLastChosenCurrencyFromSP()
    {
        SharedPreferences sharedPreferences = ((Context)(context.get())).getSharedPreferences(MainActivity.Shared_Preferences, MODE_PRIVATE);
        String localLastChosenCurrency = sharedPreferences.getString(LAST_CHOSEN_CURRENCY,"");
        return localLastChosenCurrency;
    }

    private void setLastChosenCurrencyInSharedPreferences (String lastChoosenCurrency)
    {
        SharedPreferences sharedPreferences = ((Context)(context.get())).getSharedPreferences(MainActivity.Shared_Preferences, MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString(LAST_CHOSEN_CURRENCY,lastChoosenCurrency);
        sharedPreferencesEditor.commit();
    }

    private boolean isLastChosenCurrencyInDbExistInSupportedList(String lastChosenCurrencyInDb)
    {
        if (countryCurrencyInfoList.size() < 0)
        {
            return false;
        }
        for (CountryCurrencyInfo countryCurrencyInfo: countryCurrencyInfoList)
        {
            if (countryCurrencyInfo.getCurrency().equals(lastChosenCurrencyInDb))
            {
                return true;
            }
        }
        return false;
    }

    private WebRequestHandler.ResponseHandler initCurrencyInfoHandler = new WebRequestHandler.ResponseHandler()
    {
        @Override
        public void onWebResponseFinished(JSONObject baseCurrencyInfoObject) {

            final List<String> currencyNameList = new ArrayList<String>();

            if (baseCurrencyInfoObject != null)  //web request succeeded
            {
                createSupportedCurrenciesListsFromJson(currencyNameList, baseCurrencyInfoObject);
                //update DB
                currencyDb.updateSupportedCurrenciesNamesTableMT(currencyNameList, null);

                // send currency prepared list via callback*/
                finishCurrencyInfoHandling();
            }
            else //web request failed and data should be taken from sharedPreferences and DB
            {
                createSupportedCurrenciesListFromDb();
            }
        }
    };

    private WebRequestHandler.ResponseHandler relativeCurrencyInfoInitialResponseHandler = new WebRequestHandler.ResponseHandler()
    {
        @Override
        public void onWebResponseFinished(JSONObject relativeCurrencyInfoObject)
        {

            if (relativeCurrencyInfoObject != null)   // web request succeeded
            {
                onSuccessfulWebCurrencyRatesResponse(relativeCurrencyInfoObject, true);
            }
            else   // web request failed, bring Data From DB
            {
                createCurrencyRelatedRateListFromDb();
            }
        }
    };

    private WebRequestHandler.ResponseHandler relativeCurrencyInfoPeriodicResponseHandler = new WebRequestHandler.ResponseHandler()
    {
        @Override
        public void onWebResponseFinished(JSONObject relativeCurrencyInfoObject)
        {
            if (relativeCurrencyInfoObject != null)  // web request succeeded
            {
                onSuccessfulWebCurrencyRatesResponse(relativeCurrencyInfoObject,false);
            }
            else
            {
                fetchCurrencyResponseCallBack.get().onPeriodicFetchRelativeCurrencyFinishedWithError(getLastChosenCurrencyFromSP());
            }
        }
    };

    private boolean isWeakContextReferenceValid(WeakReference<? extends  Object> weakReference)
    {
        if (weakReference.get() != null)
      {
            return  true;
        }

        return  false;
    }

    public static class LastChosenCurrency
    {
        public String currency;
        private LastChosenCurrency (String currency)
        {
            this.currency = currency;
        }

    }

    public interface FetchCurrencyResponseAsync
    {
        public void onInitializationFinished (List <CountryCurrencyInfo> countryCurrencyInfoList, List<PageBaseItem> relativeCurrencyDataItemsList, String chosenCurrency) ;

        public void onPeriodicFetchRelativeCurrencyInfoFinished(List<PageBaseItem> relativeCurrencyDataItemList);

        public void onPeriodicFetchRelativeCurrencyFinishedWithError(String lastChosenCurrency);
    }
}
