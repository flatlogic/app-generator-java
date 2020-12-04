###requirements:
+ jdk 11
+ maven 3.6.3

How to create database schema and run a local app, run following commands:
* mvn clean compile -U
* mvn -Pdev jpa-schema:generate
* mvn -Pdev sql:execute
* mvn spring-boot:run

How to (target folder):
* build - mvn clean package
* run local - java -jar <jarname>.jar

Api Documentation (Swagger)
* http://localhost:8080/api/swagger-ui/index.html (local host)
* http://host/context-path/api/swagger-ui/index.html

Change db parameters
* open file src/main/resources/application.properties
* change following properties
```clojure
  spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
  spring.datasource.username=postgres
  spring.datasource.password=
```
* rebuild and run an app
  
Change backend server port
* open file src/main/resources/application.properties
* change following property
```clojure
  server.port=8080
```
* rebuild and run an app

For changing database driver open pom.xml and change following properties
```xml
<driverGroupId>org.postgresql</driverGroupId>
<driverArtifactId>postgresql</driverArtifactId>
<driverVersion>42.2.18</driverVersion>
```
* go to https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client/2.7.1
```xml
<dependency>
    <groupId>org.mariadb.jdbc</groupId>
    <artifactId>mariadb-java-client</artifactId>
    <version>2.7.1</version>
</dependency>
```
* set org.mariadb.jdbc in driverGroupId tag
* mariadb-java-client in driverArtifactId tag
* 2.7.1 in driverVersion tag
* change db parameters
* change following parameters in pom.xml for maven plugins as in for src/main/resources/application.properties
```xml
<jdbcUrl>jdbc:postgresql://localhost:5432/postgres</jdbcUrl>
<jdbcUser>postgres</jdbcUser>
<jdbcPassword></jdbcPassword>
```
* change db parameters in file src/main/resources/application.properties
```clojure
  spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
  spring.datasource.username=postgres
  spring.datasource.password=
```
* rebuild and run an app
