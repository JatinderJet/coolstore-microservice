# Inventory Service on Wildfly Swarm 
	Two Approaches can be taken to transform to new tech stack
	- Using standalone xml, defining datasources. 
	- Using java config-api
	
## 1. XML How to ?
	- Need access to PostgreSQL. 
	- In standalone xml file, edit credentials for your postgreSQL setup i.e servername,port,username,password. 
	- POM File - pom-config-xml.xml
	- mvn clean install -f  pom-config-xml.xml
	- java -jar target/my-inventory-service-config-xml-swarm.jar
	- URL - localhost:8080/api/availability/{itemID}, http://localhost:8080/api/swagger.json
	
	
## 2. Config-API How to ? 
 
	