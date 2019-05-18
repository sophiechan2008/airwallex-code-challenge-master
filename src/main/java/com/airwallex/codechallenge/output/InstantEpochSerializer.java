package com.airwallex.codechallenge.output;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;

/**
 * 
 * @author chenyan
 * transfer the Instance to format of (seconds to Epoch)
 */
public class InstantEpochSerializer extends JsonSerializer<Instant> {
    @Override
    public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (instant == null) {
            return;
        }
        double jsonValue = instant.toEpochMilli()*1.0/1000;
        
        jsonGenerator.writeString(String.format("%.3f", jsonValue));
    }
}