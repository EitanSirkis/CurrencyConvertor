package com.example.currencyconvertor;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyconvertor.Adapters.NewGlobalRecyclerViewAdapter;
import com.example.currencyconvertor.Crypto.CryptoCurrencyFragment;
import com.example.currencyconvertor.DataItems.PageBaseItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PaginationScrollingListener <T extends PageBaseItem>  extends RecyclerView.OnScrollListener
{

    private CryptoCurrencyFragment cryptoCurrencyFragment;
    private PaginationScrollingListener.LoadItemsOnScrollAsync loadItemsOnScrollAsync;
    private NewGlobalRecyclerViewAdapter<T> recyclerViewAdapter;

    private T progressBarItem;

    private boolean isLoading = false;

    public PaginationScrollingListener  (CryptoCurrencyFragment cryptoCurrencyFragment,PaginationScrollingListener.LoadItemsOnScrollAsync loadItemsOnScrollAsync,NewGlobalRecyclerViewAdapter<T> recyclerViewAdapter,T progressBarItem)
    {
        this.cryptoCurrencyFragment = cryptoCurrencyFragment;
        this.loadItemsOnScrollAsync = loadItemsOnScrollAsync;
        this.recyclerViewAdapter = recyclerViewAdapter;
        this.progressBarItem = progressBarItem;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
    {
        super.onScrolled(recyclerView,dx,dy);
        if (((LinearLayoutManager)(recyclerView.getLayoutManager())).findLastCompletelyVisibleItemPosition() == recyclerViewAdapter.getItemCount() -1 && cryptoCurrencyFragment.areMoreItemsToLoad(recyclerViewAdapter.getItemCount()) && isLoading == false)
        {
            addProgressBar(progressBarItem);
            loadItemsOnScrollAsync.loadItemOnScroll(recyclerViewAdapter.getItemCount());
        }
    }

    public void addItems(List<T> itemsList, Comparator<T> sortBy)
    {
        removeProgressBar();

        // check if items should be sorted according to any comparator
        if (sortBy == null)
        {
            recyclerViewAdapter.getPageBaseItemList().addAll(itemsList);
        }
        else
        {
            for (T item : itemsList)
            {
                int position = Collections.binarySearch(recyclerViewAdapter.getPageBaseItemList(),item,sortBy);
                if (position<0)
                {
                    position = Math.abs(position) -1;
                    recyclerViewAdapter.getPageBaseItemList().add(position,item);
                }
                else
                {
                    recyclerViewAdapter.getPageBaseItemList().add(position+1,item);
                }
            }
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void addProgressBar(T progressBarItem)
    {
            isLoading = true;
            recyclerViewAdapter.getPageBaseItemList().add(recyclerViewAdapter.getItemCount(),progressBarItem);
            recyclerViewAdapter.notifyItemInserted(recyclerViewAdapter.getItemCount()-1);
    }

    private void removeProgressBar()
    {
        int position = recyclerViewAdapter.getPageBaseItemList().size()-1;
        recyclerViewAdapter.getPageBaseItemList().remove(progressBarItem);
        isLoading = false;
        recyclerViewAdapter.notifyItemRemoved(position);
    }

    public interface LoadItemsOnScrollAsync
    {
         void loadItemOnScroll(int startItemIndex);
    }


}

