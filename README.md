* ./gradlew run - run application
* ./gradlew test - run tests
* ./gradlew test --tests frido.mvnrepo.indexer.ArtifactProcessorTest
* ./gradlew checkstyleMain - run checkstyle
* ./gradlew findbugsMain - run findBugs
* ./gradlew pmdMain - run pmd
* ./gradlew jacocoTestReport - run jacoco reports for jacoco/test.exec file
* ./gradlew sonarqube -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=frido-bitbucket -Dsonar.login=abc13456789 - integration with SonarCloud
