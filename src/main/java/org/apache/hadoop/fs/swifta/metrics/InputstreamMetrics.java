/*
 * Copyright (c) [2018]-present, Walmart Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.hadoop.fs.swifta.metrics;

import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.swifta.exceptions.SwiftMetricWrongParametersException;

public class InputstreamMetrics implements SwiftMetric {
  private static final Log LOG = LogFactory.getLog(InputstreamMetrics.class);
  private static final Map<InputStream, String> inputStreams =
      new WeakHashMap<InputStream, String>();
  private static final int MAX = 500;
  private String name; // Metric name.

  public InputstreamMetrics(String name) {
    this.name = name;
  }

  @Override
  public void report() {
    if (LOG.isDebugEnabled()) {
      LOG.debug(this.name() + inputStreams.size());
    }

  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public int increase(Object... objects) throws SwiftMetricWrongParametersException {
    if (objects.length != 2) {
      throw new SwiftMetricWrongParametersException(
          this.name() + ": Wrong parameters!<String> <InputStream>");
    }
    String path = (String) objects[0];
    InputStream input = (InputStream) objects[1];
    inputStreams.put(input, path);
    if (inputStreams.size() > MAX) {
      LOG.warn("You have too many connections!" + inputStreams.size());
    }
    return inputStreams.size();
  }

  @Override
  public int remove(Object... objects) throws SwiftMetricWrongParametersException {
    if (objects.length != 1) {
      throw new SwiftMetricWrongParametersException(
          this.name() + "[remove] Wrong parameters! <InputStream>");
    }
    inputStreams.remove(objects[0]);
    return inputStreams.size();
  }

  @Override
  public int count() {
    return inputStreams.size();
  }
}
