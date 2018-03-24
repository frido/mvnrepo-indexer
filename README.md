# Development

~~`IntelliJ`~~ ~~`Eclipse Oxygen`~~ `Visual Studio Code` was used as IDE, written in `Java8`, build with `Gradle`.

## IDE

### Eclipse Oxygen

```java
c:\Users\peter\.gradle\
org.gradle.java.home=C:\\dev\\tools\\jdk1.8.0_121
```

*Note: Gradle to use correct jdk set:*

#### List of plugins

* Buildship Gradle Integration 2.0
* EGit 4.6.0
* logback-beagle 1.1.3
* UML Designer 7.1

### Visual Studio Code

I like it more then Eclipse. VS Code is super fast, ultra lightweight compared with Eclipse.
VS Cdde isn't effective for code refactoring and handling big projects. But for the project like this is the best option.

## Libraries

* *logging*: `logback` vs ~~`log4j`~~
* *http clients*: `jersey-client` vs ...
* *json*: TODO
* *mongo*: `Mongo` vs ~~`Morphia`~~ vs ~~`Mongoose`~~
* *testing*: ~~`jUnit4`~~ vs `jUnit5`

`code-workspace.code-workspace` is setting file for VS Code workspace.
There is defined `checkstyle` integration and recommended plugins.

## Integrations

### SonarCloud

[![SonarCloud](https://sonarcloud.io/api/project_badges/measure?project=mvnrepo-indexer&metric=alert_status)](https://sonarcloud.io/dashboard?id=mvnrepo-indexer)

### CircleCI

[![CircleCI](https://circleci.com/bb/frido/mvnrepo-indexer.svg?style=svg)](https://circleci.com/bb/frido/mvnrepo-indexer)

### StackShare

[![StackShare](https://img.shields.io/badge/tech-stack-0690fa.svg?style=flat)](https://stackshare.io/frido/mvnrepo)

### mLab

[mlab](https://mlab.com/databases/frido)

### C9

[Cloud9](https://ide.c9.io/frido/common)

*Setup project:*

* install oracle java8
* install sdk (for gradle)
* install gradle (via sdk)
* git is already installed as part of Ubuntu template clone from bitbucket
* set env `MONGO_URL` for c9 (just set it as in linux to .profile file)

## Usage

<!-- markdownlint-disable MD034 -->

* ./gradlew run - run application
* ./gradlew test - run tests
* ./gradlew checkstyleMain - run checkstyle
* ./gradlew findbugsMain - run findBugs
* ./gradlew pmdMain - run pmd
* ./gradlew jacocoTestReport - run jacoco reports for jacoco/test.exec file
* ./gradlew sonarqube -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=frido-bitbucket -Dsonar.login=abc13456789 - integration with SonarCloud
