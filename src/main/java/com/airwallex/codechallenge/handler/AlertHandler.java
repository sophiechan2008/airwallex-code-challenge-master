package com.airwallex.codechallenge.handler;

import java.util.Optional;

import com.airwallex.codechallenge.output.Alert;
import com.airwallex.codechallenge.input.CurrencyConversionRate;

/**
 * 
 * @author chenyan
 * the interface that works as different alert strategy
 */
public interface AlertHandler {
	/**
	 * 
	 * @param rate
	 * @return an Optional of alert, 
	 * null if there isn't alert, not-null alert for cases that should trigger alert
	 */
	Optional<Alert> handle(CurrencyConversionRate rate);	
}
