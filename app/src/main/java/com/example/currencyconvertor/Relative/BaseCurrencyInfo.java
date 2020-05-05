package com.example.currencyconvertor.Relative;

import java.util.HashMap;
import java.util.Map;

public class BaseCurrencyInfo implements Comparable<Object> {

    private String currency;
    private String symbol;  // sign of currency;

    public BaseCurrencyInfo(String currency, String symbol)
    {
        this.currency = currency;
        this.symbol = symbol;
    }

    public String getCurrency()
    {
        return currency;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public String toString()
    {
        return  currency + " " +symbol;
    }

    public int compareTo(Object o)
    {
        if (o instanceof BaseCurrencyInfo)
        {
            BaseCurrencyInfo other = (BaseCurrencyInfo) o;
            return (this.currency.compareTo(((BaseCurrencyInfo) o).getCurrency()));
        }

        else return -1;
    }

    public static class RelativeCurrencyAdministrativeData
    {
        public final static String CURRENCY_BASE_URL = "https://api.exchangeratesapi.io/latest";
        public final static String BASE_FLAG_URL = "http://imagescache.365scores.com/image/upload/w_60,h_60,c_limit,f_webp,q_auto:eco/v1/Countries/Round/";


        private static final Map<String, String> currencyToCountry = createCurrencyToCountryMap();
        private static final Map<String, String> currencyToIdMap = createcurrencyToIdMap();

        private static Map<String, String> createCurrencyToCountryMap() {
            Map<String, String> currencyToCountry = new HashMap<String, String>();
            currencyToCountry.put("EUR", "Eur");
            currencyToCountry.put("CAD", "Canada");
            currencyToCountry.put("HKD", "HongKong");
            currencyToCountry.put("ISK", "Iceland");
            currencyToCountry.put("PHP", "Philippines");
            currencyToCountry.put("DKK", "Danemark");
            currencyToCountry.put("HUF", "Hungary");
            currencyToCountry.put("CZK", "Czech");
            currencyToCountry.put("AUD", "Australia");
            currencyToCountry.put("RON", "Romania");
            currencyToCountry.put("SEK", "Sweden");
            currencyToCountry.put("IDR", "Inodnesia");
            currencyToCountry.put("INR", "India");
            currencyToCountry.put("BRL", "Brazil");
            currencyToCountry.put("RUB", "Russia");
            currencyToCountry.put("HRK", "Croatia");
            currencyToCountry.put("JPY", "Japan");
            currencyToCountry.put("THB", "Thaibaht");
            currencyToCountry.put("CHF", "Switzerland");
            currencyToCountry.put("SGD", "Singapore");
            currencyToCountry.put("PLN", "Poland");
            currencyToCountry.put("MYR", "Malaysia");
            currencyToCountry.put("BGN", "Bulgaria");
            currencyToCountry.put("TRY", "Turkia");
            currencyToCountry.put("CNY", "China");
            currencyToCountry.put("NOK", "Norway");
            currencyToCountry.put("NZD", "New Zealand");
            currencyToCountry.put("ZAR", "South Africa");
            currencyToCountry.put("USD", "United States");
            currencyToCountry.put("MXN", "Mexico");
            currencyToCountry.put("ILS", "Israel");
            currencyToCountry.put("GBP", "Great Britain");
            currencyToCountry.put("KRW", "South Korean");
            return currencyToCountry;
        }

        private static Map<String, String> createcurrencyToIdMap() {
            Map<String, String> currencyToIdMap = new HashMap<String, String>();
            currencyToIdMap.put("EUR", "19");
            currencyToIdMap.put("CAD", "66");
            currencyToIdMap.put("HKD", "79");
            currencyToIdMap.put("ISK", "32");
            currencyToIdMap.put("PHP", "200");
            currencyToIdMap.put("DKK", "23");
            currencyToIdMap.put("HUF", "30");
            currencyToIdMap.put("CZK", "22");
            currencyToIdMap.put("AUD", "36");
            currencyToIdMap.put("RON", "29");
            currencyToIdMap.put("SEK", "24");
            currencyToIdMap.put("IDR", "75");
            currencyToIdMap.put("INR", "80");
            currencyToIdMap.put("BRL", "21");
            currencyToIdMap.put("RUB", "14");
            currencyToIdMap.put("HRK", "38");
            currencyToIdMap.put("JPY", "34");
            currencyToIdMap.put("THB", "118");
            currencyToIdMap.put("CHF", "15");
            currencyToIdMap.put("SGD", "50");
            currencyToIdMap.put("PLN", "37");
            currencyToIdMap.put("BGN", "40");
            currencyToIdMap.put("TRY", "12");
            currencyToIdMap.put("CNY", "35");
            currencyToIdMap.put("NOK", "27");
            currencyToIdMap.put("NZD", "159");
            currencyToIdMap.put("ZAR", "134");
            currencyToIdMap.put("USD", "18");
            currencyToIdMap.put("MXN", "31");
            currencyToIdMap.put("ILS", "6");
            currencyToIdMap.put("GBP", "166");
            currencyToIdMap.put("KRW", "86");
            currencyToIdMap.put("MYR", "82");
            return currencyToIdMap;
        }

        public static final String getFlagUrl(String currency)
        {
            return BASE_FLAG_URL + currencyToIdMap.get(currency);

        }

        public static final String getCountryNameFromCurrency (String currency)
        {
            return currencyToCountry.get(currency);
        }

    }
}
