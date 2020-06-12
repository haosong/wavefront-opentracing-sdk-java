package com.wavefront.opentracing.reporting;

import com.wavefront.opentracing.WavefrontSpan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reporter that delegates to multiple other reporters for reporting.
 * Useful for debugging by reporting spans to console and to backend reporters.
 *
 * @author Vikram Raman (vikram@wavefront.com)
 */
public class CompositeReporter implements Reporter {

  private final List<Reporter> reporters = new ArrayList<>();

  public CompositeReporter(Reporter... reporters) {
    for (Reporter reporter : reporters) {
      this.reporters.add(reporter);
    }
  }

  public List<Reporter> getReporters() {
    // return a copy so that original list is not modified
    return new ArrayList<>(reporters);
  }

  @Override
  public void report(WavefrontSpan span) throws IOException {
    for (Reporter reporter : reporters) {
      reporter.report(span);
    }
  }

  @Override
  public int getFailureCount() {
    int result = 0;
    for (Reporter reporter : reporters) {
      result += reporter.getFailureCount();
    }
    return result;
  }

  @Override
  public void close() throws IOException {
    for (Reporter reporter : reporters) {
      reporter.close();
    }
  }

  @Override
  public void flush() {
    for (Reporter reporter : reporters) {
      reporter.flush();
    }
  }
}
