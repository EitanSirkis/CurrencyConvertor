package com.example.currencyconvertor.Crypto;

import android.content.Context;

import com.example.currencyconvertor.Controllers.WebRequestHandler;
import com.example.currencyconvertor.DataItems.CryptoCurrencyDataItem;
import com.example.currencyconvertor.DataItems.PageBaseItem;
import com.example.currencyconvertor.DataItems.ProgressBarItem;
import com.example.currencyconvertor.Relative.BaseCurrencyInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CryptoCurrencyManager
{
    private WeakReference <Context> context;
    private WeakReference<CryptoCurrencyManager.FetchCryptoCurrencyResponse> fetchCryptoCurrencyResponse;
    public static final int MAX_NUM_OF_ITEMS = 500;
    private final int LIMIT = 20;

    private PageBaseItem customProgressBarItem;


    private static  final String KEY = "439b4ad4-aa76-40b2-ad8c-f8eb61f89d84";
    private static final String SUPPORTED_CURRENCIES__URL = "https://pro-api.coinmarketcap.com/v1/fiat/map";

    private static final String CRYPTOCURRENCY_BASE_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";

    public CryptoCurrencyManager(WeakReference<Context> context,WeakReference<FetchCryptoCurrencyResponse> fetchCryptoCurrencyResponse)
    {
        this.context = context;
        this.fetchCryptoCurrencyResponse = fetchCryptoCurrencyResponse;

        customProgressBarItem = new ProgressBarItem();
    }

    public void fetchSupportedCurrenciesAsync()
    {
        HashMap<String,String> headers = new HashMap<String, String>();
            headers.put("X-CMC_PRO_API_KEY",KEY);
            WebRequestHandler.getJsonObject(context,currencyResponseHandler,SUPPORTED_CURRENCIES__URL,headers,null);
    }

    public void fetchCryptoCurrency(String currency,int startItemIndex)
    {
        HashMap<String,String> headers = new HashMap<String, String>();
        headers.put("X-CMC_PRO_API_KEY",KEY);
        StringBuilder url = new StringBuilder(CRYPTOCURRENCY_BASE_URL);
        url.append("?limit="+LIMIT +"&start=" + startItemIndex +"&convert=" + currency);
        WebRequestHandler.getJsonObject(context,new CryptoCurrencyResponseHandler(this,currency),url.toString(),headers,null);
    }

    WebRequestHandler.ResponseHandler  currencyResponseHandler = new WebRequestHandler.ResponseHandler ()
    {
        @Override
        public void onWebResponseFinished(JSONObject supportedCurrenciesResponse)
        {
            List<BaseCurrencyInfo> baseCurrencyInfoList = new ArrayList<BaseCurrencyInfo>();

            if (supportedCurrenciesResponse != null) //web request succeeded
            {
                try {

                    JSONArray jsonArray = supportedCurrenciesResponse.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        baseCurrencyInfoList.add(new BaseCurrencyInfo(dataObject.getString("symbol"), dataObject.getString("sign")));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Collections.sort(baseCurrencyInfoList);
                fetchCryptoCurrencyResponse.get().onFetchSupportedCurrenciesFinished(baseCurrencyInfoList);
            }
            else //web request failed and data should be taken from sharedPreferences and DB
            {


            }
        }
    };


    private static class CryptoCurrencyResponseHandler implements WebRequestHandler.ResponseHandler
    {
        private String currency;
        private CryptoCurrencyManager cryptoCurrencyManager;

        private CryptoCurrencyResponseHandler(CryptoCurrencyManager cryptoCurrencyManager,String currency)
        {
            this.cryptoCurrencyManager = cryptoCurrencyManager;
            this.currency = currency;
        }

        @Override
        public void onWebResponseFinished(JSONObject cryptoCurrencyResponse)
        {

            List<PageBaseItem> cryptoCurrencyDataItemList = new ArrayList<PageBaseItem>();

            try
            {
                JSONArray jsonArray = cryptoCurrencyResponse.getJSONArray("data");
                for(int i=0; i<jsonArray.length();i++)
                {
                    JSONObject dataObject = jsonArray.getJSONObject(i);
                    JSONObject quoteObject = dataObject.getJSONObject("quote");
                    JSONObject currencyObject = quoteObject.getJSONObject(currency);
                    CryptoCurrency cryptoCurrency = new CryptoCurrency(dataObject.getString("symbol"),dataObject.getString("name"),currencyObject.getDouble("price"),currencyObject.getDouble("market_cap"));

                    cryptoCurrencyDataItemList.add(new CryptoCurrencyDataItem(cryptoCurrency));
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cryptoCurrencyManager.fetchCryptoCurrencyResponse.get().onFetchCryptoCurrencyFinished(cryptoCurrencyDataItemList);
        }
    };

    public PageBaseItem getCustomProgressBarItem()
    {
        return customProgressBarItem;
    }

    public int getMaxNumOfItems()
    {
        return MAX_NUM_OF_ITEMS;
    }


    public interface  FetchCryptoCurrencyResponse
    {
        public void onFetchSupportedCurrenciesFinished(List<BaseCurrencyInfo> supportedCurrenciesList);

        public void onFetchCryptoCurrencyFinished(List<PageBaseItem> cryptoCurrencyDataItemList);
    }



}
