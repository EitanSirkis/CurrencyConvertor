package com.example.currencyconvertor.Crypto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyconvertor.R;
import com.example.currencyconvertor.Utils.MathOperations;

import java.util.List;


public class CryptoCurrencyRecyclerViewAdapter <T extends CryptoCurrency> extends RecyclerView.Adapter<CryptoCurrencyRecyclerViewAdapter.ViewHolder>
{

    private List<T> cryptoCurrencyList;
    private  Context context;

    public CryptoCurrencyRecyclerViewAdapter(Context context, List<T> cryptoCurrencyList)
    {
        this.context = context;
        this.cryptoCurrencyList = cryptoCurrencyList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.crypto_currency_row, parent, false);
        return new CryptoCurrencyRecyclerViewAdapter.ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull CryptoCurrencyRecyclerViewAdapter.ViewHolder holder, int position)
    {
        holder.symbolTextView.setText(cryptoCurrencyList.get(position).getSymbol());
        holder.nameTextView.setText(cryptoCurrencyList.get(position).getName());
        double price = cryptoCurrencyList.get(position).getPrice();
        double roundedPricec = MathOperations.randDouble(price);
        holder.priceTextView.setText(String.valueOf(roundedPricec));

        holder.marketPriceTextView.setText(String.valueOf(cryptoCurrencyList.get(position).getMarketCup()));

    }


    @Override
    public int getItemCount()
    {
        return cryptoCurrencyList.size();
    }

    public void setCryptoCurrencyList(List<T> cryptoCurrencyList)
    {
        this.cryptoCurrencyList = cryptoCurrencyList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView symbolTextView;
        private TextView nameTextView;
        private TextView priceTextView;
        private TextView marketPriceTextView;

        public ViewHolder(View view)
        {
            super(view);
            symbolTextView = view.findViewById(R.id.symbolTextView);
            nameTextView= view.findViewById(R.id.nameTextView);
            priceTextView = view.findViewById(R.id.priceTextView);
            marketPriceTextView = view.findViewById(R.id.marketCupTextView);
        }




    }


}
