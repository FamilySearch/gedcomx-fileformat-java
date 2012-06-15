/**
 * Copyright 2012 Intellectual Reserve, Inc.
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

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.gedcomx.rt.GedcomJsonProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * A class for creating instances of <code>JAXBContext</code> appropriate for reading and writing GEDCOM X files.
 */
public class JacksonJsonSerialization implements GedcomxEntrySerializer, GedcomxEntryDeserializer {

  private final ObjectMapper mapper;

  public JacksonJsonSerialization(Class<?>... classes) {
    this(true, classes);
  }

  public JacksonJsonSerialization(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public JacksonJsonSerialization(boolean pretty, Class<?>... classes) {
    ObjectMapper mapper = GedcomJsonProvider.createObjectMapper(classes);
    mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    if (pretty) {
      mapper.getSerializationConfig().enable(SerializationConfig.Feature.INDENT_OUTPUT);
    }
    this.mapper = mapper;
  }

  @Override
  public Object deserialize(InputStream in) throws IOException {
    return this.mapper.readValue(in, (JavaType) null);
  }

  @Override
  public void serialize(Object resource, OutputStream out) throws IOException {
    this.mapper.writeValue(out, resource);
  }

}
