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
package org.gedcomx.fileformat.impl;

import org.gedcomx.conclusion.Person;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.fileformat.GedcomxFileWriter;
import org.gedcomx.metadata.dc.ObjectFactory;
import org.gedcomx.metadata.rdf.Description;
import org.gedcomx.rt.GedcomNamespaceManager;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.jar.*;


public class GedcomxFileWriterImpl implements GedcomxFileWriter {

  private final Marshaller jaxbMarshaller;
  private final JarOutputStream gedxOutputStream;
  private final Manifest mf;

  public GedcomxFileWriterImpl(OutputStream gedxOutputStream, Class<?>... classes) throws IOException, JAXBException {
    Set<Class<?>> contextClasses = new HashSet<Class<?>>(Arrays.asList(
          Person.class
        , org.gedcomx.metadata.foaf.Person.class
        , Relationship.class
        , Description.class
        , ObjectFactory.class));
    contextClasses.addAll(Arrays.asList(classes));
    this.jaxbMarshaller = JAXBContext.newInstance((Class<?>[])contextClasses.toArray(new Class<?>[contextClasses.size()])).createMarshaller();
    this.jaxbMarshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
    this.jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new GedcomNamespaceManager());

    this.gedxOutputStream = new JarOutputStream(gedxOutputStream);

    this.mf = new Manifest();
    this.mf.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
  }

  public void addAttribute(String name, String value) {
    this.mf.getMainAttributes().putValue(name, value);
  }

  public void addResource(String contentType, String entryName, Object resource) throws IOException {
    addResource(contentType, entryName, resource, null);
  }

  public void addResource(String contentType, String entryName, Object resource, Map<String, String> attributes) throws IOException {
    if (contentType.trim().length() == 0) {
      throw new IllegalArgumentException("contentType must not be null or empty.");
    }

    entryName = entryName.replaceAll("\\\\", "/");
    JarEntry gedxEntry = new JarEntry(entryName.charAt(0) == '/' ? entryName.substring(1) : entryName); // will throw a runtime exception if entryName is not okay
    this.mf.getEntries().put(entryName, new Attributes());

    this.mf.getAttributes(entryName).put(Attributes.Name.CONTENT_TYPE, contentType);
    if (attributes != null) {
      this.mf.getAttributes(entryName).putAll(attributes);
    }

    this.gedxOutputStream.putNextEntry(gedxEntry);

    try {
      jaxbMarshaller.marshal(resource, this.gedxOutputStream);
    }
    catch (JAXBException ex) {
      throw new IOException(ex);
    }
  }

  public void close() throws IOException {
    this.gedxOutputStream.putNextEntry(new JarEntry(JarFile.MANIFEST_NAME));
    this.mf.write(this.gedxOutputStream);
    this.gedxOutputStream.close();
  }
}
