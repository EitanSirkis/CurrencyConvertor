package com.example.currencyconvertor.Relative;

public class CountryCurrencyInfo extends BaseCurrencyInfo
{
    private String countryName;
    private String flagUrl;


    public CountryCurrencyInfo(String currency, String symbol,String countryName, String flagUrl)
    {
        super(currency,symbol);
        this.countryName = countryName;
        this.flagUrl = flagUrl;
    }

    public CountryCurrencyInfo(String currency,String countryName, String flagUrl)
    {
        this (currency,null,countryName,flagUrl);
    }


    public String toString()

    {
        return countryName;
    }

    public String getCountryName()
    {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }



    public String getFlagUrl()
    {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl)
    {
        this.flagUrl = flagUrl;
    }





}


