/**
 * Copyright 2011 Intellectual Reserve, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gedcomx.fileformat;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Standard interface for writing a GEDCOM X file.
 *
 * @author Ryan Heaton
 */
public interface GedcomxFileWriter {

  /**
   * Add an attribute to the GEDCOM X file.
   *
   * @param name The name of the attribute.
   * @param value The value of the attribute.
   */
  void addAttribute(String name, String value);

  /**
   * Add a resource to the GEDCOM X file.
   *
   * @param contentType the content type of the resource.
   * @param resource The resource.
   */
  void addResource(String contentType, Object resource);

  /**
   * Add a resource to the GEDCOM X file.
   *
   * @param contentType the content type of the resource.
   * @param resource The resource.
   * @param attributes The attributes of the resource.
   */
  void addResource(String contentType, Object resource, Map<String, List<String>> attributes);

  /**
   * Close the writer.
   */
  void close() throws IOException;

}
