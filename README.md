moby-names-generator-java
====
[![Build Status](https://travis-ci.org/schnatterer/moby-names-generator-java.svg?branch=master)](https://travis-ci.org/schnatterer/moby-names-generator-java)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=info.schnatterer.moby-names-generator%3Amoby-names-generator-parent&metric=alert_status)](https://sonarcloud.io/dashboard?id=info.schnatterer.moby-names-generator%3Amoby-names-generator-parent)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=info.schnatterer.moby-names-generator%3Amoby-names-generator-parent&metric=coverage)](https://sonarcloud.io/dashboard?id=info.schnatterer.moby-names-generator%3Amoby-names-generator-parent)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=info.schnatterer.moby-names-generator%3Amoby-names-generator-parent&metric=sqale_index)](https://sonarcloud.io/dashboard?id=info.schnatterer.moby-names-generator%3Amoby-names-generator-parent)

Java Version of the famous [Moby (Docker) Names Generator](https://github.com/moby/moby/blob/master/pkg/namesgenerator/names-generator.go).

```java
import info.schnatterer.mobynamesgenerator.MobyNamesGenerator;

class GenerateMobyName {
    public static void main(String[] args) {
        System.out.println(MobyNamesGenerator.getRandomName());
    }
}
```


## Usage

Add the [latest stable version of moby-names-generator](https://search.maven.org/search?q=a:moby-names-generator%20AND%20g:info.schnatterer.moby-names-generator) 
to the dependency management tool of your choice.

```XML
<dependency>
  <groupId>info.schnatterer.moby-names-generator</groupId>
  <artifactId>moby-names-generator</artifactId>
  <version>19.03.6-r0</version>
</dependency>
```

[![Maven Central](https://img.shields.io/maven-central/v/info.schnatterer.moby-names-generator/moby-names-generator.svg)](https://search.maven.org/search?q=a:moby-names-generator%20AND%20g:info.schnatterer.moby-names-generator)

You can also get snapshot versions from our [snapshot repository](https://oss.sonatype.org/content/repositories/snapshots/info/schnatterer/moby-names-generator/) 
(for the most recent commit).
To do so, add the following repo to your `pom.xml` or `settings.xml`:
```xml
<repository>
    <id>snapshots-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
</repository>
```

