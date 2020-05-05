package com.example.currencyconvertor.DataItems;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.currencyconvertor.Crypto.CryptoCurrency;
import com.example.currencyconvertor.R;
import com.example.currencyconvertor.Utils.MathOperations;
import com.example.currencyconvertor.Utils.RecyclerViewTypes;

import java.util.Comparator;


public class CryptoCurrencyDataItem extends PageBaseItem {



    CryptoCurrency cryptoCurrency;

    public CryptoCurrencyDataItem(CryptoCurrency cryptoCurrency)
    {
        this.cryptoCurrency = cryptoCurrency;
    }

    @Override
    public int getBaseTypeId()
    {
        return  RecyclerViewTypes.CRYPTO_CURRENCY_VIEW;
    }


    public static RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crypto_currency_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        ViewHolder hold = (ViewHolder) holder;
        hold.symbolTextView. setText(cryptoCurrency.getSymbol());
        hold.nameTextView.setText(cryptoCurrency.getName());

        double price = cryptoCurrency.getPrice();
        double roundedPricec = MathOperations.randDouble(price);
        hold.priceTextView.setText(String.valueOf(roundedPricec));

        double marketPrice = cryptoCurrency.getMarketCup();
        hold.marketPriceTextView.setText(MathOperations.createFormattedString(marketPrice));


    }

    public CryptoCurrency getCryptoCurrency()
    {
        return cryptoCurrency;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        private TextView symbolTextView;
        private TextView nameTextView;
        private TextView priceTextView;
        private TextView marketPriceTextView;

        public ViewHolder(@NonNull View view)
        {
            super(view);

            symbolTextView = view.findViewById(R.id.symbolTextView);
            nameTextView= view.findViewById(R.id.nameTextView);
            priceTextView = view.findViewById(R.id.priceTextView);
            marketPriceTextView = view.findViewById(R.id.marketCupTextView);


        }
    }

    public static class SortBySymbol implements Comparator <PageBaseItem>
    {

        @Override
        public int compare(PageBaseItem o1, PageBaseItem o2)
        {
            if (o1 instanceof ProgressBarItem)
            {
                return 1;
            }

            else if (o2 instanceof ProgressBarItem)
            {
                return -1;
            }
            else
            {
                return (((CryptoCurrencyDataItem)(o1)).cryptoCurrency.getSymbol().compareTo(((CryptoCurrencyDataItem)(o2)).cryptoCurrency.getSymbol()));
            }



        }
    }

    public static class SortByPrice implements Comparator <PageBaseItem>
    {
        @Override
        public int compare(PageBaseItem o1, PageBaseItem o2)
        {

            if (o1 instanceof ProgressBarItem)
            {
                return 1;
            }

            else if (o2 instanceof ProgressBarItem)
            {
                return -1;
            }

            else
            {
                if ((((CryptoCurrencyDataItem)(o1)).cryptoCurrency.getPrice()) < (((CryptoCurrencyDataItem)(o2)).cryptoCurrency.getPrice()))
                {
                    return -1;
                }

                else if ((((CryptoCurrencyDataItem)(o1)).cryptoCurrency.getPrice()) > (((CryptoCurrencyDataItem)(o2)).cryptoCurrency.getPrice()))
                {
                    return 1;
                }

                else
                {
                    return 0;
                }
            }
        }
    }

}
