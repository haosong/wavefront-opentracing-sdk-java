package com.wavefront.opentracing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import io.opentracing.SpanContext;

/**
 * Represents a Wavefront SpanContext based on OpenTracing's {@link SpanContext}.
 *
 * @author Vikram Raman
 */
public class WavefrontSpanContext implements SpanContext {

  private final UUID traceId;
  private final UUID spanId;
  private final Map<String, String> baggage;

  public WavefrontSpanContext(UUID traceId, UUID spanId) {
    this(traceId, spanId, null);
  }

  public WavefrontSpanContext(UUID traceId, UUID spanId, Map<String, String> baggage) {
    this.traceId = traceId;
    this.spanId = spanId;

    // expected that most contexts will have no bagagge items except when propagated
    this.baggage = (baggage == null) ? Collections.emptyMap() : baggage;
  }

  @Override
  public Iterable<Map.Entry<String, String>> baggageItems() {
    return Collections.unmodifiableMap(baggage).entrySet();
  }

  @Nullable
  public String getBaggageItem(String key) {
    return baggage.get(key);
  }

  public WavefrontSpanContext withBaggageItem(String key, String value) {
    Map<String, String> items = new HashMap<>(baggage);
    items.put(key, value);
    return new WavefrontSpanContext(traceId, spanId, items);
  }

  public UUID getTraceId() {
    return traceId;
  }

  public UUID getSpanId() {
    return spanId;
  }

  @Override
  public String toString() {
    return "WavefrontSpanContext{" +
        "traceId=" + traceId +
        ", spanId=" + spanId +
        '}';
  }
}