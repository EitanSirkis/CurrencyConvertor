package com.example.currencyconvertor.DataItems;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyconvertor.Adapters.NewGlobalRecyclerViewAdapter;

public abstract class PageBaseItem {

    public abstract void onBindViewHolder(RecyclerView.ViewHolder absHolder, int position);

    public abstract int getBaseTypeId();
}




