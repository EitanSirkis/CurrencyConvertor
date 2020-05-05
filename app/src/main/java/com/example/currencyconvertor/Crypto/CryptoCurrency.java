package com.example.currencyconvertor.Crypto;

import androidx.annotation.NonNull;

import java.util.Comparator;

public class CryptoCurrency
{
    private String symbol;
    private String name;
    private double price;
    private double marketCup;

    public CryptoCurrency(String symbol, String name, double price, double marketCup) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.marketCup = marketCup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMarketCup() {
        return marketCup;
    }

    public void setMarketCup(double marketCup)
    {
        this.marketCup = marketCup;
    }

    @NonNull
    @Override
    public String toString()
    {
      //  return "name: "+ name + " " + "symbol: " + symbol + " " + "price: " + price + " " + "marketCup: "  +marketCup;
        return symbol;
    }



    public static class SortBySymbol implements Comparator <CryptoCurrency>
    {

        @Override
        public int compare(CryptoCurrency o1, CryptoCurrency o2)
        {
            return (o1.getSymbol().compareTo(o2.getSymbol()));
        }
    }

    public static class SortByPrice implements Comparator <CryptoCurrency>
    {

        @Override
        public int compare(CryptoCurrency o1, CryptoCurrency o2)
        {
            return (int)(o1.getPrice() - o2.getPrice());
        }
    }



}
