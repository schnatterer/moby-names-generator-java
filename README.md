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
        System.out.println(MobyNamesGenerator.getRandomName(0));
    }
}
```