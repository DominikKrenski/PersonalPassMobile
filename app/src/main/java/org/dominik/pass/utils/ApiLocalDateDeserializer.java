package org.dominik.pass.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public final class ApiLocalDateDeserializer extends StdDeserializer<LocalDate> {
  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public ApiLocalDateDeserializer() {
    this(null);
  }

  public ApiLocalDateDeserializer(Class<?> clazz) {
    super(clazz);
  }

  @Override
  public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    String timestamp = p.getText();

    TemporalAccessor temporalAccessor = dtf.parse(timestamp);
    return LocalDate.from(temporalAccessor);
  }
}
