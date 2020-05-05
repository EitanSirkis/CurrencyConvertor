package com.example.currencyconvertor.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyconvertor.DataItems.CryptoCurrencyDataItem;
import com.example.currencyconvertor.DataItems.PageBaseItem;
import com.example.currencyconvertor.DataItems.ProgressBarItem;
import com.example.currencyconvertor.DataItems.RelativeCurrencyDataItem;
import com.example.currencyconvertor.Utils.RecyclerViewTypes;

import java.util.List;

public class NewGlobalRecyclerViewAdapter <T extends PageBaseItem> extends RecyclerView.Adapter <RecyclerView.ViewHolder>
{
    Context context;
    List<T> pageBaseItemList;

    public NewGlobalRecyclerViewAdapter(Context context, List<T> pageBaseItemList)
    {
        this.context = context;
        this.pageBaseItemList = pageBaseItemList;
    }

    public void setPageBaseItemList (List<T> pageBaseItemList)
    {
        this.pageBaseItemList = pageBaseItemList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        switch(viewType)
        {
            case RecyclerViewTypes.RELATIVE_CURRENCY_VIEW:
            {
                return RelativeCurrencyDataItem.onCreateViewHolder(parent);
            }

            case RecyclerViewTypes.CRYPTO_CURRENCY_VIEW:
            {
                return CryptoCurrencyDataItem.onCreateViewHolder(parent);
            }

            case RecyclerViewTypes.PROGRESS_BAR_VIEW:
            {
                return ProgressBarItem.onCreateViewHolder(parent);
            }

            default:
                return ProgressBarItem.onCreateViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        pageBaseItemList.get(position).onBindViewHolder(holder,position);

    }


    @Override
    public int getItemViewType(int position)
    {
        return pageBaseItemList.get(position).getBaseTypeId();
    }

    public List<T> getPageBaseItemList()
    {
        return pageBaseItemList;
    }

    @Override
    public int getItemCount()
    {
        return pageBaseItemList.size();
    }


}
