package com.airwallex.codechallenge;

import java.util.Optional;
import java.util.stream.Stream;

import com.airwallex.codechallenge.output.Alert;
import com.airwallex.codechallenge.handler.TrendencyHandler;
import com.airwallex.codechallenge.handler.SpotChangeHandler;
import com.airwallex.codechallenge.input.CurrencyConversionRate;

/**
 *  @author chenyan
 * StreamHandler uses TrendencyHandler(alert continuous falling/rising for 15 minutes)
 * and SpotChangeHandler(alert if there is 10% diff from the 5 minutes average)
 * to generate alerts for each condition
 *
 */
public class StreamHandler {
	private final static TrendencyHandler fallingHandler =  new TrendencyHandler();
	private final static SpotChangeHandler spotChangeHandler  = new SpotChangeHandler();
	
	/**
	 * 
	 * @param rate
	 * @return the stream of alerts that are generated in different strategy(Trendency & SpotChange)
	 */
	public  static Stream<Alert> handle(final CurrencyConversionRate rate){
		return 	Stream.of(spotChangeHandler.handle(rate), fallingHandler.handle(rate))
				.filter(opt->opt.isPresent())
				.map(opt->opt.get());
	}
}
