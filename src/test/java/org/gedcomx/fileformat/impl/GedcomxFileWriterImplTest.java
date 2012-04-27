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

import org.gedcomx.conclusion.ConclusionModel;
import org.gedcomx.conclusion.Person;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.fileformat.GedcomxFileWriter;
import org.gedcomx.metadata.rdf.Description;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class GedcomxFileWriterImplTest {
  @Test
  public void testWriter() throws Exception {
    File gedxFile = File.createTempFile("FsTestTmp", ".gedx");
    try {
      List<Object> resources = ExampleGedcomxFileData.create();

      GedcomxFileWriter gedxFileWriter = new GedcomxFileWriterImpl(new FileOutputStream(gedxFile));
      try {
        gedxFileWriter.addAttribute("GX-Root", "/persons/98765");
        for (Object resource : resources) {
          if (resource instanceof Person) {
            Person person = (Person)resource;
            gedxFileWriter.addResource(ConclusionModel.GEDCOMX_CONCLUSION_V1_XML_MEDIA_TYPE
              , "/persons/" + person.getId()
              , person);
          } else if (resource instanceof Relationship) {
            Relationship relationship = (Relationship)resource;
            gedxFileWriter.addResource(ConclusionModel.GEDCOMX_CONCLUSION_V1_XML_MEDIA_TYPE
              , "\\relationships\\" + relationship.getId()
              , relationship);
          } else if (resource instanceof org.gedcomx.metadata.foaf.Person) {
            org.gedcomx.metadata.foaf.Person person = (org.gedcomx.metadata.foaf.Person)resource;
            gedxFileWriter.addResource(ConclusionModel.GEDCOMX_CONCLUSION_V1_XML_MEDIA_TYPE
              , "/contributors/" + person.getId()
              , person);
          } else if (resource instanceof Description) {
            Description description = (Description)resource;
            gedxFileWriter.addResource(ConclusionModel.GEDCOMX_CONCLUSION_V1_XML_MEDIA_TYPE
              , "/descriptions/" + description.getId()
              , description);
          } else {
            // TODO: Dublin Core ObjectFactory Types?
          }
        }
      } finally {
        gedxFileWriter.close();
      }
      System.currentTimeMillis();
    } finally {
      gedxFile.delete();
    }
  }
}
