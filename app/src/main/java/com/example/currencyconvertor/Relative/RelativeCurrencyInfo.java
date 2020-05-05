package com.example.currencyconvertor.Relative;

public class RelativeCurrencyInfo extends CountryCurrencyInfo
{
    double rate;

    public RelativeCurrencyInfo(String currency,String country,String flagUrl,double rate)
    {
        super(currency,country,flagUrl);
        this.rate = rate;
    }

    public double getRate()
    {
        return rate;
    }

    public void setRate(double rate)
    {
        this.rate = rate;
    }

}
