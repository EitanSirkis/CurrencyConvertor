package com.example.currencyconvertor.Crypto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.currencyconvertor.Relative.BaseCurrencyInfo;
import com.example.currencyconvertor.R;

import java.util.List;

public class CryptoCurrencySpinnerAdapter <T extends BaseCurrencyInfo> extends ArrayAdapter<T>

{

    List<T> supportedCurrencyList;

    public void setSupportedCurrencyList(List<T> supportedCurrencyList) {
        this.supportedCurrencyList = supportedCurrencyList;
    }

    private int lastSpinnerPositionChoise = -1;

    public CryptoCurrencySpinnerAdapter(@NonNull Context context, int resource, List<T> supportedCurrencyList)
    {
        super(context, resource,supportedCurrencyList);
        this.supportedCurrencyList = supportedCurrencyList;
    }

    public View getView(int position, View view, ViewGroup viewGroup)
    {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.crypto_currency_spinner,viewGroup,false);
        TextView currencyTextView = v.findViewById(R.id.currencyTextView);
        currencyTextView.setText(getItem(position).toString());
        return v;
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


        viewHolder.currencyTextView.setText(supportedCurrencyList.get(position).toString());
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

    public List<T> getSupportedCurrencyList()
    {
        return supportedCurrencyList;
    }

    private static class ViewHolder
    {
        private TextView currencyTextView;
    }

}
