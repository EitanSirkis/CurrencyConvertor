package com.example.currencyconvertor.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.currencyconvertor.Crypto.CryptoCurrencyFragment;
import com.example.currencyconvertor.Relative.RelativeCurrencyFragment_new;


public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
{
    public final int NUM_OF_TABS = 2;

    public ScreenSlidePagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
            {
                return new RelativeCurrencyFragment_new();
            }

            // to be changed later
            case 1: {
                return new CryptoCurrencyFragment();
            }

            default:
                return  new RelativeCurrencyFragment_new();
        }
    }

    @Override
    public int getCount()
    {

        return NUM_OF_TABS;
    }

    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0: return "Relative Currency";
            case 1: return "Crypto Currency";
            default: return "Relative Currency";
        }
    }

}
