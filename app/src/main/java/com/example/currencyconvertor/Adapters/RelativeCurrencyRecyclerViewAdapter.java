package com.example.currencyconvertor.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.currencyconvertor.Relative.RelativeCurrencyInfo;
import com.example.currencyconvertor.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.example.currencyconvertor.Utils.MathOperations;


import java.util.List;


public class RelativeCurrencyRecyclerViewAdapter<T extends RelativeCurrencyInfo> extends RecyclerView.Adapter<RelativeCurrencyRecyclerViewAdapter.ViewHolder>
{
    Context context;
    List <T> relativeCurrencyInfoList ;




    public RelativeCurrencyRecyclerViewAdapter(Context context, List<T> relativeCurrencyInfoList)
    {
        this.context = context;
        this.relativeCurrencyInfoList = relativeCurrencyInfoList;

    }

    public void setRelativeCureencyInfoList(List<T> relativeCurrencyInfoList)
    {
        this.relativeCurrencyInfoList = relativeCurrencyInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.relative_currency_rate_row, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RelativeCurrencyRecyclerViewAdapter.ViewHolder holder, int position) {




        //Picasso.get().load(currencyValueList.get(position).getFlagString()).resize(28, 28).into(holder.countryFlagImageView);
        Picasso.get().load(relativeCurrencyInfoList.get(position).getFlagUrl()).placeholder(android.R.drawable.ic_media_pause).
                error(android.R.drawable.stat_notify_error).into(holder.countryFlagImageView, new Callback() {

            @Override
            public void onSuccess() {
                int i;
            }

            @Override
            public void onError(Exception e) {
                int i;
            }
        });

        holder.countryTextView.setText(relativeCurrencyInfoList.get(position).getCountryName());
        holder.currencyTextView.setText(relativeCurrencyInfoList.get(position).getCurrency());

        double rate = relativeCurrencyInfoList.get(position).getRate();

        double roundedDouble = MathOperations.randDouble(rate);

        holder.rateTextView.setText(String.valueOf(roundedDouble));



    }

    @Override
    public int getItemCount() {

        return relativeCurrencyInfoList.size();
    }


    public List<? extends  RelativeCurrencyInfo> getRatesList()
    {

        return relativeCurrencyInfoList;
    }




    public class ViewHolder extends RecyclerView.ViewHolder
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
