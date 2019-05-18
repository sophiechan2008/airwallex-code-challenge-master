package com.airwallex.codechallenge.handler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.airwallex.codechallenge.output.Alert;
import com.airwallex.codechallenge.output.TrendencyAlert;
import com.airwallex.codechallenge.input.CurrencyConversionRate;

/**
 * 
 * @author chenyan
 * 
 * alert case2: when the spot rate has been rising/falling for 15 minutes. This alert should be
  throttled to only output once per minute and should report the length of time
  of the rise/fall in seconds.
  
 * How:
 * for each pair, I maintain the two Rates before the current one to check the trend
 * and I also keep the start point of the trend to calculate the duration of this trend.
 * For a given Rate, if the trend no longer keeps, update the start point;
 * else update the latest two rates, and generate alerts when needed
 */
public class TrendencyHandler implements AlertHandler{
	private final static int WINDOW_SIZE = 10;//15* 60; //check the trendency in 15 minutes window
	private final static int OUT_FREQUENCY = 5;//60; //alert every minute
	private HashMap<String, List<Double>> preCurrencies; //for each pair, maintain two Rates before current one
	private HashMap<String, Instant> startTimes; //for each pair, maintain the timestamp before current Rate, to keep track of the start point of falling/rising
	
	
	public static enum Direction{
		Rising,
		Falling,
		Hold;
	}

	public TrendencyHandler(){
		preCurrencies = new HashMap();
		startTimes = new HashMap();
	}
	
	
	@Override
	public Optional<Alert> handle(CurrencyConversionRate currencyConversionRate) {
		Instant currentTimestamp = currencyConversionRate.getTimestamp();
		String currencyPair = currencyConversionRate.getCurrencyPair();
		Double currentRate = currencyConversionRate.getRate();
		
		
		boolean firstRecord = !preCurrencies.containsKey(currencyPair);
		Alert alert=null;
		
		if(firstRecord){
			//the first record of this pair, just record the start point and first currency
			startTimes.put(currencyPair, currentTimestamp);
			preCurrencies.put(currencyPair, new ArrayList());
			preCurrencies.get(currencyPair).add( currentRate);
		
		}else if(preCurrencies.get(currencyPair).size()<2){
			//the second record of this pair, just record the second Rate
			preCurrencies.get(currencyPair).add(currentRate);
			
		}else{
			
			Double preRateByTwo = preCurrencies.get(currencyPair).get(0);
			Double preRateByOne= preCurrencies.get(currencyPair).get(1);
			long startTimestamp = startTimes.get(currencyPair).getEpochSecond();
			int timeGap =(int) (currentTimestamp.getEpochSecond() -startTimestamp);
			
			//there is rising/falling trend in the latest 3 Rates
			if((preRateByTwo.compareTo(preRateByOne)<0 && preRateByOne.compareTo(currentRate)<0) 
					||(preRateByTwo.compareTo(preRateByOne)>0 && preRateByOne.compareTo(currentRate)>0)){
				//generate alert every 1 minutes if the trend has been kept for 15 minutes
				if(timeGap>=WINDOW_SIZE  && timeGap%OUT_FREQUENCY==0){
					Direction direction = preRateByTwo.compareTo(preRateByOne)<0?Direction.Rising: Direction.Falling;
					alert = new TrendencyAlert(currentTimestamp, currencyPair, timeGap, direction);
				}
				
			}else{
				//there isn't trend at all, the mark the previous one as the start point
				startTimes.put(currencyPair, currentTimestamp.minusSeconds(1)); //previous one
			}
			
			//update the latest two Rates
			preCurrencies.get(currencyPair).set(0, preRateByOne);
			preCurrencies.get(currencyPair).set(1, currentRate);
		}
		
		return Optional.ofNullable(alert);
	}
	
	
}
