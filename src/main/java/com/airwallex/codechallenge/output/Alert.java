package com.airwallex.codechallenge.output;

import java.time.Instant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author chenyan
 * the base class of Alert, it includes common information 
 * like: timestamp, currencypair, and alert type
 * and there is ObjectMapper to searize it to JSON format
 */
public class Alert {
	protected Instant timestamp;
	protected String currencyPair;
	protected AlertType alert;
	
	protected ObjectMapper mapper;
	
	public Alert(Instant t,String cp){
		this.timestamp = t;
		this.currencyPair = cp;
		mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); 
		
		JavaTimeModule module=new JavaTimeModule();
		module.addSerializer(Instant.class, new InstantEpochSerializer());
		mapper.registerModule(module);
	}
	public static enum AlertType{
		spotChange,
		falling,
		rising;
	}
	
	@Override
	public String toString() {
		try {
			return mapper.writeValueAsString(this);
		} catch (Exception e) {
			e.printStackTrace();
			return "Excetion: Alert [timestamp=" + timestamp + ", currencyPair=" + currencyPair + ", alert=" + alert
					+ "]";
		}
	}

	public Instant getTimestamp() {
		return timestamp;
	}
	
	public String getCurrencyPair() {
		return currencyPair;
	}
	
	public AlertType getAlert() {
		return alert;
	}
	
}
