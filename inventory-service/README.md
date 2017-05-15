# Inventory Service on Wildfly Swarm
Below Approaches can be taken to transform to new tech stack
  - Using standalone xml, defining datasources.
  - Using java api
  - Using project-defaults.yml

##### 1. XML: How to ?
  - Need access to PostgreSQL.
  - In standalone.xml file, edit credentials for your postgreSQL setup i.e servername,port,username,password.
  - POM File - pom-config-xml.xml
  - `mvn clean install -f  pom-config-xml.xml`
  - `java -jar target/inventory-service-config-xml-swarm.jar`


##### 2. Config-API: How to ?
  - Need access to PostgreSQL
  - Change file extension of [module.xml.txt](src/main/resources/modules/org/postgresql/main/module.xml.txt) to `module.xml`
  - Change file extension of [Main.java.txt](src/main/java/com/redhat/coolstore/datasources/Main.java.txt) to `Main.java`
  - `mvn clean install -f  pom-config-api.xml`
  - `java -jar target/inventory-service-config-api-swarm.jar`

##### 3. project-defaults: How to ?
  - Create file as per [link](https://howto.wildfly-swarm.io/create-a-datasource)
  - To ensure the correct file is being used, mv `standalone.xml` to `standalone.xml.txt`
  - Use `pom-config-yml.xml` for build.
  - `mvn clean install -f  pom-config-yml.xml`
  - `java -jar target/inventory-service-config-xml-swarm.jar`



#### URL http://localhost:8080/api/availability/{itemID}, http://localhost:8080/api/swagger.json

#### Deployment
##### Approach 1: Using fabric8
  - Create [fabric8](src/main) with files [credentials-secret.yml](src/main/fabric8/credentials-secret.yml), [deployment.yml](src/main/fabric8/deployment.yml), [route.yml](src/main/fabric8/route.yml), [svc.yml](src/main/fabric8/svc.ym).
  - On local use `oc` utility to create cluster `oc cluster up`
  - Create new-app for `postgresql` i.e `oc new-app -e POSTGRESQL_USER=jasingh -ePOSTGRESQL_PASSWORD=123456 -ePOSTGRESQL_DATABASE=testdb docker.io/centos/postgresql-95-centos7 --name=my-database`
  - Build: as `root` run `mvn clean package fabric8:build`
  - Deploy: `mvn fabric8:deploy`
  - Run `oc get pods` , `oc get routes`.

#### Observations
XML approach works only when file name is `standalone.xml` and is in base folder of inventory service [link](./) or is in resources folder [link](src/main/resources)
You need not explicitly define, automatically picked, indicates somewhere hard-coded in lib.
Yet to see lib source code
