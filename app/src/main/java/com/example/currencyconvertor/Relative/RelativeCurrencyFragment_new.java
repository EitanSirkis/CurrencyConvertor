package com.example.currencyconvertor.Relative;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyconvertor.Adapters.NewGlobalRecyclerViewAdapter;
import com.example.currencyconvertor.DataItems.PageBaseItem;
import com.example.currencyconvertor.MultiButton;
import com.example.currencyconvertor.R;

import java.lang.ref.WeakReference;
import java.util.List;


public class RelativeCurrencyFragment_new extends Fragment implements AdapterView.OnItemSelectedListener,CM.FetchCurrencyResponseAsync
{
    private RecyclerView relativeCurrencyRecyclerView;
    private ProgressBar progressBar;
    private Spinner currencySpinner;
    private CM currencyManager;
    private NewGlobalRecyclerViewAdapter<PageBaseItem> relativeCurrencyRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.relative_crypto_currency_fragment, container, false);

        MultiButton multiButton = view.findViewById(R.id.multiButton);
        multiButton.setVisibility(View.GONE);

        ((TextView) (view.findViewById(R.id.textView1))).setText("Currency");
        ((TextView) (view.findViewById(R.id.textView2))).setText("Country");
        ((TextView) (view.findViewById(R.id.textView3))).setText("Rate");
        view.findViewById(R.id.textView4).setVisibility(View.GONE);

        relativeCurrencyRecyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        currencySpinner = view.findViewById(R.id.spinner);

        currencyManager = new CM(new WeakReference<Context>(getActivity()), (new WeakReference<CM.FetchCurrencyResponseAsync>(this)));
        currencyManager.init();
        return view;
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // checks if a new currency is requested
        CountrySpinnerAdapter<CountryCurrencyInfo> countrySpinnerAdapter = ((CountrySpinnerAdapter<CountryCurrencyInfo>) (parent.getAdapter()));
       // Log.e("OnItemSelected: ", "position " + position + ", " + "last spinner position" + countrySpinnerAdapter.getLastSpinnerPositionChoise());

        if (countrySpinnerAdapter.getLastSpinnerPositionChoise() != position)
        {
            relativeCurrencyRecyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            currencyManager.fetchPeriodicRelativeCurrencyInfoAsync(countrySpinnerAdapter.getCountryCurrencyInfoList().get(position).getCurrency());
            countrySpinnerAdapter.setLastSpinnerPositionChoise(position); //update spinner choice
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private int getChosenCurrencyPosition(List<CountryCurrencyInfo> countryCurrencyInfoList, String chosenCurrency) {

        int lastChosenIndex = -1;

        if (!chosenCurrency.equals("")) {
            for (int i = 0; i < countryCurrencyInfoList.size(); i++)  //  currency that is saved in shared preferebces existed
            {
                if (countryCurrencyInfoList.get(i).getCurrency().equals(chosenCurrency)) {
                    lastChosenIndex = i;
                    break;
                }
            }
        }
        return lastChosenIndex;
    }

    public void onInitializationFinished(List<CountryCurrencyInfo> countryCurrencyInfoList, List<PageBaseItem> relativeCurrencyDataItemsList, String chosenCurrency)
    {
        int currencyChosenPosition = getChosenCurrencyPosition(countryCurrencyInfoList, chosenCurrency);
        boolean connectionProblemsFlag = true;
        if (countryCurrencyInfoList.size() > 0 && currencyChosenPosition != -1) //check if there is data for spinner
        {
            //set spinner data
            CountrySpinnerAdapter<CountryCurrencyInfo> countrySpinnerAdapter = new CountrySpinnerAdapter(getActivity(), R.layout.currnecy_country_spinner, countryCurrencyInfoList);
            countrySpinnerAdapter.setDropDownViewResource(R.layout.currency_spinner_row);
            currencySpinner.setVisibility(View.VISIBLE);
            currencySpinner.setOnItemSelectedListener(this);
            currencySpinner.setAdapter(countrySpinnerAdapter);

            currencySpinner.setSelection(currencyChosenPosition);
            countrySpinnerAdapter.setLastSpinnerPositionChoise(currencyChosenPosition);

            if (relativeCurrencyDataItemsList.size() > 0)  //check if there is data for spinnr
            {
                progressBar.setVisibility(View.GONE);
                relativeCurrencyRecyclerView.setVisibility(View.VISIBLE);

                // set recycler view with data
                linearLayoutManager = new LinearLayoutManager(getActivity());
                relativeCurrencyRecyclerView.setLayoutManager(linearLayoutManager);
                relativeCurrencyRecyclerViewAdapter = new NewGlobalRecyclerViewAdapter<PageBaseItem>(getActivity(), relativeCurrencyDataItemsList);
                relativeCurrencyRecyclerView.setAdapter(relativeCurrencyRecyclerViewAdapter);
                connectionProblemsFlag = false;
            }
        }

        if (connectionProblemsFlag)
        {
            showConnectionErrorMessage();
        }
    }


    public void onPeriodicFetchRelativeCurrencyInfoFinished(List<PageBaseItem> relativeCurrencyDataItemList)
    {
        progressBar.setVisibility(View.GONE);
        relativeCurrencyRecyclerView.setVisibility(View.VISIBLE);

        if (relativeCurrencyRecyclerView.getAdapter() == null)  // check if recycler view adapter was set before
        {
            linearLayoutManager = new LinearLayoutManager(getActivity());
            relativeCurrencyRecyclerView.setLayoutManager(linearLayoutManager);

            relativeCurrencyRecyclerViewAdapter = new NewGlobalRecyclerViewAdapter<PageBaseItem>(getActivity(), relativeCurrencyDataItemList);
            relativeCurrencyRecyclerView.setAdapter(relativeCurrencyRecyclerViewAdapter);
        }
        else
        {
            relativeCurrencyRecyclerViewAdapter.setPageBaseItemList(relativeCurrencyDataItemList);
            relativeCurrencyRecyclerViewAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onPeriodicFetchRelativeCurrencyFinishedWithError(String lastChosenCurrency) {
        CountrySpinnerAdapter countrySpinnerAdapter = ((CountrySpinnerAdapter) currencySpinner.getAdapter());
        if (countrySpinnerAdapter != null)
        {
            List<CountryCurrencyInfo> countryCurrencyInfoList = countrySpinnerAdapter.getCountryCurrencyInfoList();
            int lastChosenCurrencyPosition = getChosenCurrencyPosition(countryCurrencyInfoList, lastChosenCurrency);

            if (lastChosenCurrencyPosition != -1)  //  currency that is saved in shared preferences is valid
            {
                progressBar.setVisibility(View.GONE);
                relativeCurrencyRecyclerView.setVisibility(View.VISIBLE);
                countrySpinnerAdapter.setLastSpinnerPositionChoise(lastChosenCurrencyPosition);
                currencySpinner.setSelection(lastChosenCurrencyPosition);
            }
        }
        showConnectionErrorMessage();

    }

    public void showConnectionErrorMessage() {
        Toast noConnectionToast = Toast.makeText(getContext(), R.string.no_connection, Toast.LENGTH_LONG);
        noConnectionToast.setGravity(Gravity.BOTTOM, 0, 0);
        noConnectionToast.show();
    }
}


