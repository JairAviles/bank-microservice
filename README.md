# Micro service case study

- Employee: Jair Israel Aviles Eusebio
- Id: 20087460
- ADID: JA40013952
- Email: [jair.eusebio@wipro.com](mailto:jair.eusebio@wipro.com)

### Dependencies

- Maven
- Java jdk 8
- Lombok
- Spring boot
- jUnit 5
- Mockito
- Swagger2
- Inline H2 DB memory integrated (No need to define datasource, unless using another RDBMS)

### Instructions commands

- Running tests `mvn clean verify`
- Generating jar `mvn clean install`
- Running spring boot app `mvn clean install spring-boot:run`

### Acknowledge
- DB DDL definition in **application/resources**, inserting 4 customer by default
- Jacoco test report coverage available in path **target/site/jacoco/index.html**
- Opening application in [localhost:8080/](localhost:8080/) will redirect to swagger ui page
- H2 console available in [default page](localhost:8080/h2-console)