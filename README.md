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

Also available as [CLI](#democli).
![Screencast showcasing Docker Image](https://raw.githubusercontent.com/wiki/schnatterer/moby-names-generator-java/screencast.gif)

## Usage

Add the [latest stable version of moby-names-generator](https://search.maven.org/search?q=a:moby-names-generator%20AND%20g:info.schnatterer.moby-names-generator) 
to the dependency management tool of your choice.

```XML
<dependency>
  <groupId>info.schnatterer.moby-names-generator</groupId>
  <artifactId>moby-names-generator</artifactId>
  <version>20.10.0-r0</version>
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

## Demo/CLI

There are examples for [Java 8](example-java8) and [Java9+](example-java9) (using Jigsaw/JPMS). Both implement simple 
command line interfaces that return a random name.

The Java 9+ example is also available as compact Docker image, [built using GraalVM](Dockerfile).

[![moby-names-generator on DockerHub](https://img.shields.io/docker/image-size/schnatterer/moby-names-generator)](https://hub.docker.com/r/schnatterer/moby-names-generator)

```shell
$ docker run --rm schnatterer/moby-names-generator
nifty_noether
```

## Releasing

The version name looks like so, for example: `20.10.0-r0-SNAPSHOT`
The first part (before `-r0`)  corresponds to the [moby version](https://github.com/moby/moby/releases).
The second part `r0` is zero-based and can be increase when the moby version does not change.
When updating the moby version, use a snapshot first, to see if the builds still succeeds.

* Set the version using: `mvn versions:set`
* When the builds succeed, release either via 
  * `./mvnw release:prepare -Darguments=pgp.skip=true`
    Sets versions in pom.xml, commits, tags and pushes to SCM. Travis builds tag and pushes to Maven Central.
  * Or manually using `mvn versions:set` to set version without `SNAPSHOT`, then increase `r` and add `SNAPSHOT` again.
    Don't forget to tag. Either locally or via GitHub (signed tag!) 