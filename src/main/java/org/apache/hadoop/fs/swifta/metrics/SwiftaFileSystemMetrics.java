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

import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.swifta.exceptions.SwiftMetricWrongParametersException;
import org.apache.hadoop.fs.swifta.snative.SwiftNativeFileSystem;

public class SwiftaFileSystemMetrics implements SwiftMetric {

  private static final Log LOG = LogFactory.getLog(SwiftaFileSystemMetrics.class);
  private static final Map<SwiftNativeFileSystem, String> fileSystem =
      new WeakHashMap<SwiftNativeFileSystem, String>();
  private static final int MAX = 500;
  private String name; // Metric name.

  public SwiftaFileSystemMetrics(String name) {
    this.name = name;
  }

  @Override
  public void report() {
    if (LOG.isDebugEnabled()) {
      LOG.debug(this.name() + fileSystem.size());
    }

  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public int increase(Object... objects) throws SwiftMetricWrongParametersException {
    if (objects.length != 1) {
      throw new SwiftMetricWrongParametersException("Wrong parameters!<SwiftNativeFileSystem>");
    }
    SwiftNativeFileSystem file = (SwiftNativeFileSystem) objects[0];
    fileSystem.put(file, "test");
    if (fileSystem.size() > MAX) {
      LOG.warn("You have too many SwiftNativeFileSystem instances!" + fileSystem.size());
    }
    return fileSystem.size();
  }

  @Override
  public int remove(Object... objects) throws SwiftMetricWrongParametersException {
    if (objects.length != 1) {
      throw new SwiftMetricWrongParametersException(
          this.name() + "[remove] Wrong parameters! <SwiftNativeFileSystem>");
    }
    fileSystem.remove(objects[0]);
    return fileSystem.size();
  }

  @Override
  public int count() {
    return fileSystem.size();
  }

}
