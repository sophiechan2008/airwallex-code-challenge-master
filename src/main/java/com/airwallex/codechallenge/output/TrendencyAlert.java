package com.airwallex.codechallenge.output;import java.time.Instant;

import com.airwallex.codechallenge.handler.TrendencyHandler.Direction;
import com.airwallex.codechallenge.output.Alert.AlertType;

/**
 * @author chenyan
 * the alert servers for there is falling/rising trend lasts for 15 minutes
 */
public class TrendencyAlert extends Alert{
	private int seconds;
	
	public TrendencyAlert(Instant t,String cp, int s, Direction dir){
		super(t, cp);
		if(dir.equals(Direction.Falling)){
			this.alert = AlertType.falling;
		}else if(dir.equals(Direction.Rising)){
			this.alert = AlertType.rising;
		}
		this.seconds =s;
	}

	@Override
	public String toString() {
		try{
			return mapper.writeValueAsString(this);
		}catch(Exception e){
			e.printStackTrace();
			return "TrendencyAlert [seconds=" + seconds + ", timestamp=" + timestamp + ", currencyPair=" + currencyPair
				+ ", alert=" + alert + "]";
		}
	}

	public int getSeconds() {
		return seconds;
	}

	
}
