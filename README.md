

# MvnRepo Indexer

[Java](https://www.java.com/en/) crawler to download all `pom.xml` files from a repository.

## Run

```bash
./gradlew run - run application
```



## Tests

Checkstyle report

```bash
./gradlew checkstyleMain - run checkstyle
```

FindBugs report

```bash
./gradlew findbugsMain - run findBugs
```

PMD report

```bash
./gradlew pmdMain - run pmd
```

Jacoco coverate report

```bash
./gradlew jacocoTestReport - run jacoco reports for jacoco/test.exec file
```

SonarQube report

```bash
./gradlew sonarqube -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=frido-bitbucket -Dsonar.login=abc13456789 - integration with SonarCloud
```

[Here](https://frido.github.io/mvnrepo-indexer/) you can find tests reports