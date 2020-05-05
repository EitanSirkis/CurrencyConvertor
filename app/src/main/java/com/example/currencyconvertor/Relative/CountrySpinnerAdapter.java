package com.example.currencyconvertor.Relative;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.currencyconvertor.Relative.CountryCurrencyInfo;
import com.example.currencyconvertor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CountrySpinnerAdapter<T extends CountryCurrencyInfo>  extends ArrayAdapter<T>
{


    private List<T> countryCurrencyInfoList;
    private int lastSpinnerPositionChoise = -1;

    public  CountrySpinnerAdapter(Context context, int resource, List<T> countryCurrencyInfoList)
    {

        super(context,resource,countryCurrencyInfoList);
        this.countryCurrencyInfoList = countryCurrencyInfoList;
    }

    public View getView(int position, View view, ViewGroup viewGroup)
    {

        view = LayoutInflater.from(getContext()).inflate(R.layout.currnecy_country_spinner,viewGroup,false);

        ImageView flag = (ImageView) view.findViewById(R.id.flagImageView);

        TextView country = (TextView) view.findViewById(R.id.countryTextView);
        TextView currency = (TextView) view.findViewById(R.id.currencyTextView);


        Picasso.get().load(countryCurrencyInfoList.get(position).getFlagUrl()).placeholder(android.R.drawable.ic_media_pause).
                error(android.R.drawable.stat_notify_error).into(flag);

        country.setText(countryCurrencyInfoList.get(position).getCountryName()+" : ");
        currency.setText(countryCurrencyInfoList.get(position).getCurrency());
        return view;
    }



    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        ViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.currency_spinner_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.currencyTextView = convertView.findViewById(R.id.currencyTextView);
            convertView.setTag(viewHolder);
        }

       else
       {
           viewHolder = (ViewHolder) (convertView.getTag());
       }


        viewHolder.currencyTextView.setText(countryCurrencyInfoList.get(position).getCurrency());
        viewHolder.currencyTextView.setTextColor(convertView.getContext().getResources().getColor(R.color.currencyRowTextColor));



        if (lastSpinnerPositionChoise ==position)
        {


            convertView.setBackgroundResource(R.color.backgroundColor);
        }

        else
        {
            convertView.setBackgroundResource(R.color.rowColor);
        }

        return convertView;
    }

    public int getLastSpinnerPositionChoise()
    {
        return lastSpinnerPositionChoise;
    }

    public void setLastSpinnerPositionChoise(int lastSpinnerPositionChoise)
    {
        this.lastSpinnerPositionChoise = lastSpinnerPositionChoise;
    }

    public List<T> getCountryCurrencyInfoList()
    {
        return countryCurrencyInfoList;
    }

    // inner class that is used for recycling convertView parameter in 'getView' method of outer class
    private static class ViewHolder
    {
        private TextView currencyTextView;
    }


}