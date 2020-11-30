###requirements:
+ jdk 11
+ maven 3.6.3

how to create database schema, run next commands:
+ mvn clean compile -U
+ mvn -Pdev jpa-schema:generate
+ mvn -Pdev sql:execute

how to :
+ build - mvn clean package
+ run local - java -jar <jarname>.jar
