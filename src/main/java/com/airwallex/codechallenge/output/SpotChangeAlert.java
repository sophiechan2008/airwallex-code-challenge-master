package com.airwallex.codechallenge.output;

import java.time.Instant;

import com.airwallex.codechallenge.output.Alert.AlertType;

/**
 * @author chenyan
 * the alert servers when spot diff by 10% of average in last 5 minutes
 */
public class SpotChangeAlert extends Alert{
	public SpotChangeAlert(Instant t,String cp){
		super(t, cp);
		this.alert=AlertType.spotChange;
	}

	@Override
	public String toString() {
		try{
			return mapper.writeValueAsString(this);
		}catch(Exception e){
			e.printStackTrace();
			return "SpotChangeAlert [timestamp=" + timestamp + ", currencyPair=" + currencyPair + ", alert=" + alert + "]";
		}
	}
}
