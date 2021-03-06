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


package org.apache.hadoop.fs.swifta;

import static org.apache.hadoop.fs.swifta.util.SwiftTestUtils.readBytesToString;
import static org.apache.hadoop.fs.swifta.util.SwiftTestUtils.writeTextFile;

import java.io.EOFException;
import java.io.IOException;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

/**
 * Test filesystem read operations
 */
public class TestSwiftFileSystemRead extends SwiftFileSystemBaseTest {


  /**
   * Read past the end of a file: expect the operation to fail
   * 
   * @throws IOException
   */
  @Test(timeout = SWIFT_TEST_TIMEOUT)
  public void testOverRead() throws IOException {
    final String message = "message";
    final Path filePath = new Path("/test/file.txt");

    writeTextFile(fs, filePath, message, false);

    try {
      readBytesToString(fs, filePath, 20);
      fail("expected an exception");
    } catch (EOFException e) {
      // expected
    }
  }

  /**
   * Read and write some JSON
   * 
   * @throws IOException
   */
  @Test(timeout = SWIFT_TEST_TIMEOUT)
  public void testRWJson() throws IOException {
    final String message = "{" + " 'json': { 'i':43, 'b':true}," + " 's':'string'" + "}";
    final Path filePath = new Path("/test/file.json");

    writeTextFile(fs, filePath, message, false);
    String readJson = readBytesToString(fs, filePath, message.length());
    assertEquals(message, readJson);
    // now find out where it is
    FileStatus status = fs.getFileStatus(filePath);
    fs.getFileBlockLocations(status, 0, 10);
  }

  /**
   * Read and write some XML
   * 
   * @throws IOException
   */
  @Test(timeout = SWIFT_TEST_TIMEOUT)
  public void testRWXML() throws IOException {
    final String message = "<x>" + " <json i='43' 'b'=true/>" + " string" + "</x>";
    final Path filePath = new Path("/test/file.xml");

    writeTextFile(fs, filePath, message, false);
    String read = readBytesToString(fs, filePath, message.length());
    assertEquals(message, read);
  }

}
