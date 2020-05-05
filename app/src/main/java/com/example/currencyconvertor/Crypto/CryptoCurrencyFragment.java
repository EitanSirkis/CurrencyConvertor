package com.example.currencyconvertor.Crypto;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyconvertor.Adapters.NewGlobalRecyclerViewAdapter;
import com.example.currencyconvertor.DataItems.CryptoCurrencyDataItem;
import com.example.currencyconvertor.DataItems.PageBaseItem;
import com.example.currencyconvertor.MultiButton;
import com.example.currencyconvertor.PaginationScrollingListener;
import com.example.currencyconvertor.R;
import com.example.currencyconvertor.Relative.BaseCurrencyInfo;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CryptoCurrencyFragment extends Fragment implements AdapterView.OnItemSelectedListener,CryptoCurrencyManager.FetchCryptoCurrencyResponse, PaginationScrollingListener.LoadItemsOnScrollAsync
{

    private CryptoCurrencySpinnerAdapter<BaseCurrencyInfo> cryptoCurrencySpinnerAdapter;
    private RecyclerView cryptoCurrencyRecyclerView;
    private ProgressBar progressBar;
    private Spinner currencySpinner;
    private MultiButton multiButton;

    private CryptoCurrencyManager cryptoCurrencyManager;

    private NewGlobalRecyclerViewAdapter<PageBaseItem> cryptoCurrencyRecyclerViewAdapter;
    private PaginationScrollingListener<PageBaseItem> pageBaseItemPaginationScrollingListener;

    private Comparator<PageBaseItem> sortBy = new CryptoCurrencyDataItem.SortBySymbol(); // Change!!!
    private LinearLayoutManager linearLayoutManager;

    private boolean isCryptoCurrencyAlreadyFetched;


    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.relative_crypto_currency_fragment, container, false);
       // setRetainInstance(true);


        ((TextView)(view.findViewById(R.id.textView1))).setText("Symbol");
        ((TextView)(view.findViewById(R.id.textView2))).setText("Name");
        ((TextView)(view.findViewById(R.id.textView3))).setText("Price");
        ((TextView)(view.findViewById(R.id.textView4))).setText("Market Price");

         multiButton = view.findViewById(R.id.multiButton);

        cryptoCurrencyRecyclerView= view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        currencySpinner = view.findViewById(R.id.spinner);

        currencySpinner.setVisibility(View.GONE);
        multiButton.setVisibility(View.GONE);
        cryptoCurrencyRecyclerView.setVisibility(View.GONE);

        for (int i=0; i<multiButton.getNumOfButtons(); i++)
        {
            OnClickListener listener = createListenerForButton(i);
            multiButton.addSingleButtonListener(listener,i);

            String buttonText = createTextForButton(i);
            multiButton.setSingleButtonText(buttonText,i);

        }

        cryptoCurrencyManager = new CryptoCurrencyManager(new WeakReference<Context>(getActivity()), (new WeakReference<CryptoCurrencyManager.FetchCryptoCurrencyResponse>(this)));
        cryptoCurrencyManager.fetchSupportedCurrenciesAsync();
        return view;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



        if (cryptoCurrencySpinnerAdapter.getLastSpinnerPositionChoise() != position)
        {
            isCryptoCurrencyAlreadyFetched = false;

            cryptoCurrencyRecyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            cryptoCurrencyManager.fetchCryptoCurrency(cryptoCurrencySpinnerAdapter.getSupportedCurrencyList().get(position).getCurrency(),1);
            cryptoCurrencySpinnerAdapter.setLastSpinnerPositionChoise(position);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onFetchSupportedCurrenciesFinished(List<BaseCurrencyInfo> supportedCurrenciesList)
    {

        cryptoCurrencySpinnerAdapter = new CryptoCurrencySpinnerAdapter<BaseCurrencyInfo>(getActivity(), R.layout.crypto_currency_spinner, supportedCurrenciesList);
        cryptoCurrencySpinnerAdapter.setDropDownViewResource(R.layout.relative_currency_rate_row);

        currencySpinner.setVisibility(View.VISIBLE);
        currencySpinner.setOnItemSelectedListener(this);
        currencySpinner.setAdapter(cryptoCurrencySpinnerAdapter);


    }

    public void onFetchCryptoCurrencyFinished(List<PageBaseItem> cryptoCurrencyDataItemList)
    {

        if (isCryptoCurrencyAlreadyFetched == false)  // if a countious data or a new from item 1
        {
            isCryptoCurrencyAlreadyFetched = true;
            progressBar.setVisibility(View.GONE);
            multiButton.setVisibility(View.VISIBLE);
            cryptoCurrencyRecyclerView.setVisibility(View.VISIBLE);


            if (cryptoCurrencyRecyclerView.getAdapter() == null)    // checks if adapter has already set before
            {
                linearLayoutManager = new LinearLayoutManager(getActivity());

                cryptoCurrencyRecyclerView.setLayoutManager(linearLayoutManager);

                sortBy = getInitialComparator(multiButton.getActiveButtonIndex());
                Collections.sort(cryptoCurrencyDataItemList, sortBy);

                cryptoCurrencyRecyclerViewAdapter = new NewGlobalRecyclerViewAdapter<PageBaseItem>(getActivity(), cryptoCurrencyDataItemList);

                pageBaseItemPaginationScrollingListener = new PaginationScrollingListener<PageBaseItem>(this,this, cryptoCurrencyRecyclerViewAdapter, cryptoCurrencyManager.getCustomProgressBarItem());

                cryptoCurrencyRecyclerView.addOnScrollListener(pageBaseItemPaginationScrollingListener);

                cryptoCurrencyRecyclerView.setAdapter(cryptoCurrencyRecyclerViewAdapter);
            }
            else  // if adapter was never set
            {
                cryptoCurrencyRecyclerViewAdapter.getPageBaseItemList().clear();
                pageBaseItemPaginationScrollingListener.addItems(cryptoCurrencyDataItemList, sortBy);
            }
        }
        else // a continous chunk of data
        {
            pageBaseItemPaginationScrollingListener.addItems(cryptoCurrencyDataItemList, sortBy);
        }

    }

    public boolean areMoreItemsToLoad(int currentListSize)
    {
        if (currentListSize <= cryptoCurrencyManager.getMaxNumOfItems())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    @Override
    public void loadItemOnScroll(int startItemIndex)
    {
        String currency = cryptoCurrencySpinnerAdapter.getSupportedCurrencyList().get(cryptoCurrencySpinnerAdapter.getLastSpinnerPositionChoise()).getCurrency();
        cryptoCurrencyManager.fetchCryptoCurrency(currency,startItemIndex);

    }

    private void sortCryptoCurrencyList()
    {
        Collections.sort(cryptoCurrencyRecyclerViewAdapter.getPageBaseItemList(),sortBy);
    }

    protected Comparator <PageBaseItem> getInitialComparator (int activeButtonIndex)
    {
        switch (activeButtonIndex)
        {
            case 0:
            {
                return new CryptoCurrencyDataItem.SortBySymbol();
            }
            case 1:
            {
                return new CryptoCurrencyDataItem.SortByPrice();
            }

            default:
                return null;


        }

    }

    protected String createTextForButton(int index)
    {
        switch (index)
        {
            case 0:
            {
                return "By Symbol";
            }

            case 1:
            {
                return "By Price";
            }

            default:
                return "";

        }

    }

    public OnClickListener createListenerForButton(int indexOfListenertoSet)
    {
        switch (indexOfListenertoSet)
        {
            case 0:
            {
                return new OnSortBySymbolClick();
            }
            case 1:
            {
                return new OnSortByPriceClick();
            }

            default:
                return null;

        }

    }


    private class OnSortBySymbolClick implements View.OnClickListener
    {


        @Override
        public void onClick(View v)
        {
            sortBy = new CryptoCurrencyDataItem.SortBySymbol();
            sortCryptoCurrencyList();
            cryptoCurrencyRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private class OnSortByPriceClick implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            sortBy = new CryptoCurrencyDataItem.SortByPrice();
            sortCryptoCurrencyList();
            cryptoCurrencyRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

}



