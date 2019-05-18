package com.airwallex.codechallenge;

import com.airwallex.codechallenge.input.Reader;

/**
 * 
 * @author chenyan
 * the entry point
 */
public class App {

    public static void main(String[] args) {
        Reader reader = new Reader();
        
        reader
                .read(args[0])
                //
                //TODO: use StreamHandler to handle each CurrencyConversionRate input
                //
                .flatMap(currencyConversionRate->StreamHandler.handle(currencyConversionRate))
                
                .forEach(currencyConversionRate -> System.out.println(currencyConversionRate.toString()));
    }

}
