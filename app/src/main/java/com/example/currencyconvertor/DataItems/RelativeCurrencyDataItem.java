package com.example.currencyconvertor.DataItems;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyconvertor.Relative.RelativeCurrencyInfo;
import com.example.currencyconvertor.R;
import com.example.currencyconvertor.Utils.MathOperations;
import com.example.currencyconvertor.Utils.RecyclerViewTypes;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class RelativeCurrencyDataItem extends PageBaseItem
{
    RelativeCurrencyInfo relativeCurrencyInfo;

    public RelativeCurrencyDataItem(RelativeCurrencyInfo relativeCurrencyInfo)
    {
        this.relativeCurrencyInfo = relativeCurrencyInfo;
    }

    @Override
    public int getBaseTypeId()
    {
        return RecyclerViewTypes.RELATIVE_CURRENCY_VIEW;

    }


    public static RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.relative_currency_rate_row, (ViewGroup) parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ViewHolder hold = (ViewHolder)holder;
        Picasso.get().load(relativeCurrencyInfo.getFlagUrl()).placeholder(android.R.drawable.ic_media_pause).
                error(android.R.drawable.stat_notify_error).into(hold.countryFlagImageView, new Callback() {

            @Override
            public void onSuccess() {
                int i;
            }

            @Override
            public void onError(Exception e) {
                int i;
            }
        });

        hold.countryTextView.setText(relativeCurrencyInfo.getCountryName());
        hold.currencyTextView.setText(relativeCurrencyInfo.getCurrency());

        double rate = relativeCurrencyInfo.getRate();

        double roundedDouble = MathOperations.randDouble(rate);

        hold.rateTextView.setText(String.valueOf(roundedDouble));

    }


    private static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView countryFlagImageView;
        TextView countryTextView;
        TextView currencyTextView;
        TextView rateTextView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            countryFlagImageView = itemView.findViewById(R.id.countryFlagImageView);
            countryTextView = itemView.findViewById(R.id.targetCountryTextView);
            currencyTextView = itemView.findViewById(R.id.currencyTextView);
            rateTextView = itemView.findViewById(R.id.rateTextView);
        }
    }
}
