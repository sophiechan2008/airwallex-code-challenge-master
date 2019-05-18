package com.airwallex.codechallenge.handler;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.logging.StreamHandler;
import java.util.stream.Stream;
import com.airwallex.codechallenge.input.CurrencyConversionRate;

import com.airwallex.codechallenge.output.Alert;
import com.airwallex.codechallenge.output.SpotChangeAlert;

/**
 * 
 * @author chenyan
 * 
 * Alert case1: when the spot rate for a currency pair changes by more than 10% 
 * from the 5 minute average for that currency pair
 * 
 * How:
 * For each currency pair, I maintained a Queue of Currency of given length(5 minutes),
 * and also maintained sum for this window.
 * for new record, enqueue the Rate, and increase the sum
 * for record out of the window, dequeue Rate, and decrease the sum 
 * to keep the Queue and Sum updated
 */
public class SpotChangeHandler implements AlertHandler{
	
	//the window for average calculation
	private final static int WINDOW_SIZE = 5*60;// 5 minutes
	
	//for each pair, maintain the Currency Window to calculate the average
	private HashMap<String, LinkedList<Double>> currencyRateInWindow;
	
	//for each pair, cache the sum of this window
	private HashMap<String, Double> currencySumInWindow;
	
	public SpotChangeHandler(){
		currencyRateInWindow = new HashMap<String, LinkedList<Double>>();
		currencySumInWindow = new HashMap<String, Double>();
	}
	
	@Override
	public Optional<Alert> handle(CurrencyConversionRate currencyConversionRate){
		//parse the input
		Instant currentTimestamp = currencyConversionRate.getTimestamp();
		String currencyPair = currencyConversionRate.getCurrencyPair();
		Double currentRate = currencyConversionRate.getRate();
		
		//inital the currency window
		if(!currencyRateInWindow.containsKey(currencyPair)){
			currencyRateInWindow.put(currencyPair, new LinkedList());
		}
		
		//initial the sum
		if(!currencySumInWindow.containsKey(currencyPair)){
			currencySumInWindow.put(currencyPair, 0.0);
		}
		
		Double sumInWindow = currencySumInWindow.get(currencyPair);
		LinkedList<Double> queue = currencyRateInWindow.get(currencyPair);
		Double preAverage = queue.isEmpty()?currentRate: sumInWindow/queue.size();
		
		boolean meetAlertThreshold = (Math.abs(preAverage-currentRate)/currentRate>0.1);
		
		//enqueue the current Rate, and increase the sum
		queue.addFirst(currentRate);
		sumInWindow+=currentRate;
		
		//if there are records out of window, remove it, and decrease the sum
		if(queue.size()>WINDOW_SIZE){
			Double outOfScope = queue.removeLast();
			sumInWindow-=outOfScope;
		}
		currencySumInWindow.put(currencyPair, sumInWindow);
		
		//generate the alert if necessary
		Alert alert =null;
		if(meetAlertThreshold){
			alert = new SpotChangeAlert(currentTimestamp, currencyPair);
		}
		return Optional.ofNullable(alert);
	}
	
	
}
