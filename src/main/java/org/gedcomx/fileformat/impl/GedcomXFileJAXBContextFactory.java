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
import org.gedcomx.metadata.dc.ObjectFactory;
import org.gedcomx.metadata.rdf.Description;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * A class for creating instances of <code>JAXBContext</code> appropriate for reading and writing GEDCOM X files.
 */
public class GedcomXFileJAXBContextFactory {

  /**
   * Factory method for creating a new instance of a <code>JAXBContext</code> appropriate for reading and/or writing a GEDCOM X file.
   *
   * The created <code>JAXBContext</code> references the following classes by default:
   *   org.gedcomx.conclusion.Person
   *   org.gedcomx.conclusion.Relationship
   *   org.gedcomx.metadata.dc.ObjectFactory
   *   org.gedcomx.metadata.foaf.Person
   *   org.gedcomx.metadata.rdf.Description
   * Any additional classes needed can be passed to this call to supplement (not override) these defaults
   *
   * @param classes Additional classes to supplement (not override) the provided defaults
   * @return A JAXBContext
   *
   * @throws JAXBException
   */
  public static JAXBContext newInstance(Class<?>... classes) throws JAXBException {
    Set<Class<?>> contextClasses = new HashSet<Class<?>>(Arrays.asList(
        Person.class
      , org.gedcomx.metadata.foaf.Person.class
      , Relationship.class
      , Description.class
      , ObjectFactory.class));
    contextClasses.addAll(Arrays.asList(classes));
    return JAXBContext.newInstance((Class<?>[]) contextClasses.toArray(new Class<?>[contextClasses.size()]));
  }
}
