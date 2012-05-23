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

import org.gedcomx.rt.GedcomNamespaceManager;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.jar.*;


/**
 * Class to help in writing a GEDCOM X file.
 */
public class GedcomxOutputStream {

  private final Marshaller jaxbMarshaller;
  private final JarOutputStream gedxOutputStream;
  private final Manifest mf;

  /**
   * Constructs a GEDCOM X output stream.
   *
   * NOTE: This class uses the GedcomXFileJAXBContextFactory to create a JAXB context from which to derive the marshaller that is used to marshal resources into the output stream.
   * GedcomXFileJAXBContextFactory creates a context that includes some default resource classes.  The classes passed via this constructor will supplement these defaults; they will
   * not overwrite or replace these defaults.  Please see the documentation for GedcomXFileJAXBContextFactory to review the list of default classes.
   *
   * @param gedxOutputStream an output stream to which the GEDCOM X resources will appended
   * @param classes classes representing resources that will be marshaled (via JAXB) into the GEDCOM X output stream
   *
   * @throws IOException
   */
  public GedcomxOutputStream(OutputStream gedxOutputStream, Class<?>... classes) throws IOException {
    try {
      this.jaxbMarshaller = GedcomXFileJAXBContextFactory.newInstance(classes).createMarshaller();
      this.jaxbMarshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
      this.jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new GedcomNamespaceManager());
    }
    catch (JAXBException ex) {
      throw new IOException(ex);
    }

    this.gedxOutputStream = new JarOutputStream(gedxOutputStream);

    this.mf = new Manifest();
    this.mf.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
  }

  /**
   * Add an attribute to the GEDCOM X output stream.
   *
   * @param name The name of the attribute.
   * @param value The value of the attribute.
   */
  public void addAttribute(String name, String value) {
    this.mf.getMainAttributes().putValue(name, value);
  }

  /**
   * Add a resource to the GEDCOM X output stream.
   *
   *
   * @param contentType The content type of the resource.
   * @param entryName The name by which this resource shall be known within the GEDCOM X file.
   * @param resource The resource.
   * @param lastModified timestamp when the resource was last modified (can be null)
   * @throws IOException
   */
  public JarEntry addResource(String contentType, String entryName, Object resource, Date lastModified) throws IOException {
    return addResource(contentType, entryName, resource, null, lastModified);
  }

  /**
   * Add a resource to the GEDCOM X output stream.
   *
   * @param contentType The content type of the resource.
   * @param entryName The name by which this resource shall be known within the GEDCOM X file.
   * @param resource The resource.
   * @param attributes The attributes of the resource.
   * @param lastModified timestamp when the resource was last modified (can be null)
   *
   * @throws IOException
   */
  public JarEntry addResource(String contentType, String entryName, Object resource, Map<String, String> attributes, Date lastModified) throws IOException {
    if (contentType.trim().length() == 0) {
      throw new IllegalArgumentException("contentType must not be null or empty.");
    }

    entryName = entryName.replaceAll("\\\\", "/");
    entryName = entryName.charAt(0) == '/' ? entryName.substring(1) : entryName;

    JarEntry gedxEntry = new JarEntry(entryName); // will throw a runtime exception if entryName is not okay
    this.mf.getEntries().put(entryName, new Attributes());
    if (lastModified != null)
      gedxEntry.setTime(lastModified.getTime());

    this.mf.getAttributes(entryName).put(Attributes.Name.CONTENT_TYPE, contentType);
    if (attributes != null) {
      for (Map.Entry<String, String> entry : attributes.entrySet()) {
        this.mf.getAttributes(entryName).putValue(entry.getKey(), entry.getValue());
      }
    }

    this.gedxOutputStream.putNextEntry(gedxEntry);

    try {
      jaxbMarshaller.marshal(resource, this.gedxOutputStream);
    }
    catch (JAXBException ex) {
      throw new IOException(ex);
    }
    return gedxEntry;
  }

  /**
   * Closes the GEDCOM X output stream as well as the stream being filtered.
   *
   * @throws IOException
   */
  public void close() throws IOException {
    this.gedxOutputStream.putNextEntry(new JarEntry(JarFile.MANIFEST_NAME));
    this.mf.write(this.gedxOutputStream);
    this.gedxOutputStream.close();
  }
}
