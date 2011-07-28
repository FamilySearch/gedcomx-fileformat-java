Introduction
------------

Welcome to the GEDCOM X File Format library for Java. This library is used to read and write GEDCOM X File Format as defined
by the [GEDCOM X File Format Specification](http://www.gedcomx.org/File-Format.html). It relies on the artifacts produced by
the [GEDCOM X project](https://github.com/FamilySearch/gedcomx).

Here's how you read a file:

```java
InputStream in = new FileInputStream("/path/to/gedcomx/file");
GedcomxFileReader reader = new new JerseyMultipartGedcomxFileReader(in, Record.class, Person.class);
Collection<GedcomxFilePart> parts = reader.getParts();
for (GedcomxFilePart part : parts) {
  Object content = part.getContent();
  if (content instanceof Record) {
    Record record = (Record) content;
    //handle the record as needed.
  }
  else if (content instanceof Person) {
    Person person = (Person) content;
    //handle the person as needed.
  }
  else if (content instanceof InputStream) {
    InputStream stream = (InputStream) content;
    if ("image/jpeg".equals(part.getMediaType())) {
      //handle the stream as a picture.
    }
    else {
      //...
    }
  }
}
```

Here's how you write a file:

```java
GedcomxFileWriter writer = new JerseyMultipartGedcomxFileWriter();
Record record = ...;
writer.addPart("application/x-gedcom-record-v1+xml", record);
Person person = ...;
writer.addPart("application/x-gedcom-conclusion-v1+xml", person);
InputStream image = new FileInputStream("/path/to/some/image.jpg");
writer.addPart("image/jpeg", image);

OutputStream out = new FileOutputStream("/path/to/file/to/write");
writer.writeTo(out);
```

Using the Library
-----------------

This library is a [Maven](http://maven.apache.org/)-based project with the artifacts being built at [Cloudbees](http://cloudbees.com)
by the [GEDCOM Jenkins Instance](https://gedcom.ci.cloudbees.com/job/gedcomx-fileformat-java-snapshot/). Snapshot artifacts are published
to the [snapshot repository](https://repository-gedcom.forge.cloudbees.com/snapshot/) and release artifacts to the
[release repository](https://repository-gedcom.forge.cloudbees.com/release/). Here are the Maven coordinates for the artifacts:

* groupId: `org.gedcomx`
* artifactId: `gedcomx-fileformat`
* version: [choose the latest](https://repository-gedcom.forge.cloudbees.com/release/org/gedcomx/gedcomx-fileformat/)

Of course, you can always clone the repo and build the library from source:

```
mvn install
```

