package com.example.currencyconvertor.Relative;

import androidx.fragment.app.Fragment;

public class RelativeCurrencyFragment extends Fragment
{
    /*
    private RecyclerView relativeCurrencyRecyclerView;
    private ProgressBar progressBar;
    private Spinner currencySpinner;

    private CurrencyManager currencyManager;
    private NewGlobalRecyclerViewAdapter<PageBaseItem> relativeCurrencyRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private static final String LAST_CHOOSEN_CURRENCY = "last choosen currency";
    private boolean isFirstRun = true;


    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.relative_crypto_currency_fragment, container, false);

        MultiButton multiButton = view.findViewById(R.id.multiButton);
        multiButton.setVisibility(View.GONE);

        ((TextView)(view.findViewById(R.id.textView1))).setText("Currency");
        ((TextView)(view.findViewById(R.id.textView2))).setText("Country");
        ((TextView)(view.findViewById(R.id.textView3))).setText("Rate");
         view.findViewById(R.id.textView4).setVisibility(View.GONE);

        relativeCurrencyRecyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        currencySpinner = view.findViewById(R.id.spinner);

        currencyManager = new CurrencyManager(new WeakReference<Context>(getActivity()),(new WeakReference<CurrencyManager.FetchCurrencyResponseAsync>(this)));

        currencyManager.fetchCurrencyBaseInfoAsync();

        return view;
    }

    //ask Manager for last successfull chossen spinner position and checks if
    private int getLastChooenCurrencyPosition(List<? extends CountryCurrencyInfo> countryCurrencyInfoList)
    {
        int lastChoosenIndex = -1;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MainActivity.Shared_Preferences, MODE_PRIVATE);
        String lastChosenCurrency = sharedPreferences.getString(LAST_CHOOSEN_CURRENCY,"");

        if (!lastChosenCurrency.equals(""))
        {
            for (int i = 0; i < countryCurrencyInfoList.size(); i++)  //  currency that save in shared preferebces existed
            {
                if (countryCurrencyInfoList.get(i).getCurrency().equals(lastChosenCurrency))
                {
                    lastChoosenIndex = i;
                    break;
                }
            }
        }
      return lastChoosenIndex;
    }

    private void setLastChosenCurrencyInSharedPreferences (String lastChoosenCurrency)
    {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MainActivity.Shared_Preferences, MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString(LAST_CHOOSEN_CURRENCY,lastChoosenCurrency);
        sharedPreferencesEditor.commit();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // checks if a new currency is requested
        CountrySpinnerAdapter<CountryCurrencyInfo> countrySpinnerAdapter = ((CountrySpinnerAdapter<CountryCurrencyInfo>) (parent.getAdapter()));
        Log.e("OnItemSelected: ", "position " + position + ", " + "last spinner position" + countrySpinnerAdapter.getLastSpinnerPositionChoise());

        if (isFirstRun == true || countrySpinnerAdapter.getLastSpinnerPositionChoise()  != position)
        {
            relativeCurrencyRecyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            if (isFirstRun == true)
            {
                currencyManager.initializeRelativeCurrencyInfoAsync(countrySpinnerAdapter.getCountryCurrencyInfoList().get(position).getCurrency());
                isFirstRun = false;
            }
            else if (countrySpinnerAdapter.getLastSpinnerPositionChoise() != position)
            {
                currencyManager.fetchPeriodicRelativeCurrencyInfoAsync(countrySpinnerAdapter.getCountryCurrencyInfoList().get(position).getCurrency());
            }
            countrySpinnerAdapter.setLastSpinnerPositionChoise(position); //update spinner choice
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }



    public void onStartResponse(List<? extends CountryCurrencyInfo> countryCurrencyInfoList, String chosenCurrency, List<? extends RelativeCurrencyInfo> relativeCurrencyInfoList)
    {
        if (countryCurrencyInfoList != null)
        {
            //set spinner data
            CountrySpinnerAdapter<CountryCurrencyInfo> countrySpinnerAdapter = new CountrySpinnerAdapter(getActivity(), R.layout.currnecy_country_spinner, countryCurrencyInfoList);
            countrySpinnerAdapter.setDropDownViewResource(R.layout.currency_spinner_row);
            currencySpinner.setVisibility(View.VISIBLE);
            currencySpinner.setOnItemSelectedListener(this);



            currencySpinner.setAdapter(countrySpinnerAdapter);
        }

    }


    //handle data that feeds the spinner and set last spinner selection
    public void onFetchCurrencyBaseInfoFinished(List<? extends CountryCurrencyInfo> countryCurrencyInfoList)
    {
        CountrySpinnerAdapter<CountryCurrencyInfo> countrySpinnerAdapter = new CountrySpinnerAdapter(getActivity(), R.layout.currnecy_country_spinner, countryCurrencyInfoList);
        countrySpinnerAdapter.setDropDownViewResource(R.layout.currency_spinner_row);
        currencySpinner.setVisibility(View.VISIBLE);
        currencySpinner.setOnItemSelectedListener(this);
        currencySpinner.setAdapter(countrySpinnerAdapter);

        int lastChoosenCurrencyPosition = getLastChooenCurrencyPosition(countryCurrencyInfoList);

        if (lastChoosenCurrencyPosition != -1)      // last choosen currency found
        {
            currencySpinner.setSelection(lastChoosenCurrencyPosition);
        }

        else   // last choosen currency doesn't find
        {
            final String currencyInfirstPosition = countrySpinnerAdapter.getCountryCurrencyInfoList().get(0).getCurrency();
            setLastChosenCurrencyInSharedPreferences(currencyInfirstPosition);
        }
    }

    public void onFetchRelativeCurrencyInfoFinished(List<PageBaseItem> relativeCurrencyDataItemList)
    {
        progressBar.setVisibility(View.GONE);
        relativeCurrencyRecyclerView.setVisibility(View.VISIBLE);

        if (relativeCurrencyRecyclerView.getAdapter() == null)    // if first run of this function
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

        CountrySpinnerAdapter countrySpinnerAdapter = ((CountrySpinnerAdapter)(currencySpinner.getAdapter()));
        int lastSpinnerPositionChoise = countrySpinnerAdapter.getLastSpinnerPositionChoise();
        String currentCurrency = ((CountryCurrencyInfo)(countrySpinnerAdapter.getCountryCurrencyInfoList().get(lastSpinnerPositionChoise))).getCurrency();
        setLastChosenCurrencyInSharedPreferences(currentCurrency);
    }

    @Override
    public void onFetchRelativeCurrencyFinishedWithError()
    {
        CountrySpinnerAdapter countrySpinnerAdapter = ((CountrySpinnerAdapter) currencySpinner.getAdapter());
        List<CountryCurrencyInfo> countryCurrencyInfoList = countrySpinnerAdapter.getCountryCurrencyInfoList();

        if (getLastChooenCurrencyPosition(countryCurrencyInfoList) != -1)  //  currency that save in shared preferences is valid
        {
            progressBar.setVisibility(View.GONE);
            relativeCurrencyRecyclerView.setVisibility(View.VISIBLE);

            SharedPreferences sharedPreferences = getContext().getSharedPreferences(MainActivity.Shared_Preferences, MODE_PRIVATE);
            String currency = sharedPreferences.getString(LAST_CHOOSEN_CURRENCY,"");

            int position = 0;
            for (; position < countryCurrencyInfoList.size(); position++)     // find index of last spinner choise
            {
                if (currency.equals(countryCurrencyInfoList.get(position).getCurrency()))
                {
                    break;
                }
            }
            countrySpinnerAdapter.setLastSpinnerPositionChoise(position);
            currencySpinner.setSelection(position);
        }

        Toast noConnectionToast = Toast.makeText(getContext(),R.string.no_connection  ,Toast.LENGTH_LONG);
        noConnectionToast.setGravity(Gravity.BOTTOM,0,0);
        noConnectionToast.show();
    }

     */
}
