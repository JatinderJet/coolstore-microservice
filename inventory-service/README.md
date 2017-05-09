# Inventory Service on Wildfly Swarm
Two Approaches can be taken to transform to new tech stack
  - Using standalone xml, defining datasources.
  - Using java config-api

#### 1. XML: How to ?
  - Need access to PostgreSQL.
  - In standalone.xml file, edit credentials for your postgreSQL setup i.e servername,port,username,password.
  - POM File - pom-config-xml.xml
  - `mvn clean install -f  pom-config-xml.xml`
  - `java -jar target/inventory-service-config-xml-swarm.jar`


#### 2. Config-API: How to ?
  - Need access to PostgreSQL
  - Change file extension of [module.xml.txt](src/main/resources/modules/org/postgresql/main/module.xml.txt) to `module.xml`
  - Change file extension of [Main.java.txt](src/main/java/com/redhat/coolstore/datasources/Main.java.txt) to `Main.java`
  - `mvn clean install -f  pom-config-api.xml`
  - `java -jar target/inventory-service-config-api-swarm.jar`

#### URL http://localhost:8080/api/availability/{itemID}, http://localhost:8080/api/swagger.json

#### Observations
XML approach works only when file name is `standalone.xml` and is in base folder of inventory service [link](./) or is in resources folder [link](src/main/resources)
You need not explicitly define, automatically picked, indicates somewhere hard-coded in lib.
Yet to see lib source code
