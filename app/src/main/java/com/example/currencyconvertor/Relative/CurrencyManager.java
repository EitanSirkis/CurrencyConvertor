package com.example.currencyconvertor.Relative;

import com.example.currencyconvertor.DataItems.PageBaseItem;
import com.example.currencyconvertor.Relative.CountryCurrencyInfo;

import java.util.List;


public class CurrencyManager {
    /*
    private WeakReference<Context> context;
    private WeakReference<FetchCurrencyResponseAsync> fetchCurrencyResponseCallBack;
    private CurrencyDb currencyDb;


    private static final String LAST_CHOOSEN_CURRENCY = "last choosen currency";

    public CurrencyManager(WeakReference<Context> context, WeakReference<FetchCurrencyResponseAsync> fetchCurrencyResponseCallBack)
    {
        this.context = context;
        this.fetchCurrencyResponseCallBack = fetchCurrencyResponseCallBack;
        this.currencyDb = new CurrencyDb(context);
    }

    public void fetchCurrencyBaseInfoAsync() {
        WebRequestHandler.getJsonObject(context, baseCurrencyInfoHandler, RelativeCurrencyAdministrativeData.CURRENCY_BASE_URL, null, null);
    }

    public void initializeRelativeCurrencyInfoAsync(String currency) {
        String url = createUrlForCurrencyWebRequest(currency);
        WebRequestHandler.getJsonObject(context, relativeCurrencyInfoInitialResponseHandler, url, null, null);
    }

    public void fetchPeriodicRelativeCurrencyInfoAsync(String currency) {
        String url = createUrlForCurrencyWebRequest(currency);
        WebRequestHandler.getJsonObject(context, relativeCurrencyInfoPeriodicResponseHandler, url, null, null);
    }

    public String createUrlForCurrencyWebRequest(String currency) {
        StringBuilder url = new StringBuilder(RelativeCurrencyAdministrativeData.CURRENCY_BASE_URL);
        url.append("?base=" + currency);
        return (url.toString());
    }

    private void createSupportedCurrenciesListsFromJson(List<CountryCurrencyInfo> countryCurrencyInfoList, List<String> currenciesNameList, JSONObject baseCurrencyInfoObject)
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

        }
        catch (JSONException ex)
        {

            ex.printStackTrace();

        }
    }


    private void createdCurrenciesRelativeRateListsFromJson(List<PageBaseItem> relativeCurrencyDataItemsList, List<RelativeCurrencyInfo> relativeCurrencyInfoList, JSONObject relativeCurrencyInfoObject)
    {
        try
        {
            String baseCurrency = relativeCurrencyInfoObject.getString("base");
            JSONObject ratesJsonObject = relativeCurrencyInfoObject.getJSONObject("rates");
            Iterator<String> currencies = ratesJsonObject.keys();
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

    // will be run on separate thread
    private void createSupportedCurrenciesListFromDb(final List<CountryCurrencyInfo> countryCurrencyInfoList)
    {
                 currencyDb.fetchCurrenciesNamesMT(countryCurrencyInfoList, new Runnable() {
                     @Override
                     public void run() {
                         if (context.get() != null) {
                             ((Activity) (context.get())).runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     // send currency prepared list via callback
                                     fetchCurrencyResponseCallBack.get().onFetchCurrencyBaseInfoFinished(countryCurrencyInfoList);
                                 }
                             });
                         }
                     }
                 });
    }

    // will be run on separate thread
    private void createCurrencyRelatedRateListFromDb (final List<RelativeCurrencyInfo> relativeCurrencyInfoList)
    {
      currencyDb.fetchRelativeRCurrenciesatesMT(relativeCurrencyInfoList, new Runnable() {
          @Override
          public void run()
          {
              if (context.get() != null)
              {
                  final List<PageBaseItem> relativeCurrencyDataItemsList = new ArrayList<PageBaseItem>();
                  for (RelativeCurrencyInfo relativeCurrencyInfo : relativeCurrencyInfoList)
                  {
                      relativeCurrencyDataItemsList.add(new RelativeCurrencyDataItem(relativeCurrencyInfo));
                  }
                  ((Activity) (context.get())).runOnUiThread(new Runnable() {

                      @Override
                      public void run()
                      {
                          fetchCurrencyResponseCallBack.get().onFetchRelativeCurrencyInfoFinished(relativeCurrencyDataItemsList);
                      }
                  });
              }
          }
        });
    }


    private void onSuccessfullWebCurrencyRatesResponse(JSONObject relativeCurrencyInfoObject)
    {

        List<PageBaseItem> relativeCurrencyDataItemsList = new ArrayList<PageBaseItem>();
        final List<RelativeCurrencyInfo> relativeCurrencyInfoList = new ArrayList<RelativeCurrencyInfo>();
        createdCurrenciesRelativeRateListsFromJson(relativeCurrencyDataItemsList, relativeCurrencyInfoList, relativeCurrencyInfoObject);
        //updateDB
        currencyDb.updateCurrenciesRelativeRateTableMT(relativeCurrencyInfoList,null);
        // send currency relative rates prepared list via callback
        fetchCurrencyResponseCallBack.get().onFetchRelativeCurrencyInfoFinished(relativeCurrencyDataItemsList);
    }

    public String getLastChoosenCurrency()
    {
        SharedPreferences sharedPreferences = ((Context)(context.get())).getSharedPreferences(MainActivity.Shared_Preferences, MODE_PRIVATE);
        String lastChoosenCurrency = sharedPreferences.getString(LAST_CHOOSEN_CURRENCY,"");
        return lastChoosenCurrency;
    }


    private WebRequestHandler.ResponseHandler baseCurrencyInfoHandler = new WebRequestHandler.ResponseHandler() {
        @Override
        public void onWebResponseFinished(JSONObject baseCurrencyInfoObject) {

            final List<CountryCurrencyInfo> countryCurrencyInfoList = new ArrayList<CountryCurrencyInfo>();
            final List<String> currencyNameList = new ArrayList<String>();

            if (baseCurrencyInfoObject != null)  //web request succeeded
            {
                createSupportedCurrenciesListsFromJson(countryCurrencyInfoList, currencyNameList, baseCurrencyInfoObject);
                //update DB
                currencyDb.updateSupportedCurrencisNamesTableMT(currencyNameList, null);

                // send currency prepared list via callback
                fetchCurrencyResponseCallBack.get().onFetchCurrencyBaseInfoFinished(countryCurrencyInfoList);
            }
            else //web request failed and data should be taken from sharedPreferences and DB
            {
                createSupportedCurrenciesListFromDb(countryCurrencyInfoList);
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
                onSuccessfullWebCurrencyRatesResponse(relativeCurrencyInfoObject);
            }

            else   // web request failed, bring Data From DB
            {
                final List<RelativeCurrencyInfo> relativeCurrencyInfoList = new ArrayList<RelativeCurrencyInfo>();
                createCurrencyRelatedRateListFromDb(relativeCurrencyInfoList);
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
                onSuccessfullWebCurrencyRatesResponse(relativeCurrencyInfoObject);
            }
            else // web request failed, bring Data From DB
            {
                fetchCurrencyResponseCallBack.get().onFetchRelativeCurrencyFinishedWithError();
            }

        }
    };
*/

    public interface FetchCurrencyResponseAsync
    {
        public void onFetchCurrencyBaseInfoFinished(List<? extends CountryCurrencyInfo> countryCurrencyInfoList);
        public void onFetchRelativeCurrencyInfoFinished(List<PageBaseItem> relativeCurrencyDataItemList);
        public void onFetchRelativeCurrencyFinishedWithError();
    }
}



