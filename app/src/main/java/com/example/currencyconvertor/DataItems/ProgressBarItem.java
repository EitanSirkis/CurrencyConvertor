package com.example.currencyconvertor.DataItems;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.currencyconvertor.R;
import com.example.currencyconvertor.Utils.RecyclerViewTypes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ProgressBarItem extends PageBaseItem
{

    public static RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate (R.layout.progress_bar_raw,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder absHolder, int position)
    {
    }

    @Override
    public int getBaseTypeId()
    {
        return RecyclerViewTypes.PROGRESS_BAR_VIEW;
    }


    private static class ViewHolder extends RecyclerView.ViewHolder
    {

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }
    }


}

