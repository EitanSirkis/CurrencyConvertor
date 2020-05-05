package com.example.currencyconvertor.Utils;

public class MathOperations
{
    // round rate according to forth digit after the decimal part

    public static double randDouble(double num)
    {
        double roundedDouble = num;

        String numString = String.valueOf(num);
        int integerPlaces = numString.indexOf('.');

        int numOfDecimalDigits = numString.length() - integerPlaces-1;
        if (numOfDecimalDigits>=4)
        {
            int decimalNumber =(int) (num * Math.pow(10,4));
            if (decimalNumber%10>=4)
            {
                roundedDouble = (decimalNumber/10)+1;
            }
            else
            {
                roundedDouble = decimalNumber/10;
            }

            roundedDouble /= 1000;
        }
        return roundedDouble;

    }

    public static String createFormattedString (double num)
    {
        double param1 = num / Math.pow(10,12);
        if (param1 > 1)
            return "" + randDouble(param1) + " T";

        double param2 = num/Math.pow (10,9);
        if (param2 > 1)
            return "" + randDouble(param2) + " B";

        double param3 = num/Math.pow (10,6);
        return "" + randDouble(param3) + " M";

    }

}

