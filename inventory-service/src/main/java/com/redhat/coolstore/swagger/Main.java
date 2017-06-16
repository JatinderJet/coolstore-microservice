package com.redhat.coolstore.swagger;

import com.redhat.coolstore.model.Inventory;
import com.redhat.coolstore.rest.AvailabilityEndpoint;
import com.redhat.coolstore.rest.RestApplication;
import com.redhat.coolstore.service.InventoryService;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.swagger.SwaggerArchive;
import org.wildfly.swarm.logging.LoggingFraction;
import org.wildfly.swarm.cdi.CDIFraction;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.jpa.JPAFraction;
import org.jboss.shrinkwrap.api.asset.StringAsset;
//import org.wildfly.swarm.container.Container;

/**
 * @author Lance Ball
 */
public class Main {

	public static void main(String[] args) throws Exception {
		try{
		Swarm swarm = new Swarm();

		SwaggerArchive archive = ShrinkWrap.create(SwaggerArchive.class, "swagger-app.war");
		JAXRSArchive deployment = archive.as(JAXRSArchive.class).addPackages(true,"com.redhat.coolstore.swagger","com.redhat.coolstore.model","com.redhat.coolstore.service","com.redhat.coolstore.rest");

		// Tell swagger where our resources are
		archive.setResourcePackages("com.redhat.coolstore.swagger","com.redhat.coolstore.model","com.redhat.coolstore.service","com.redhat.coolstore.rest");

		deployment.addClass(RestApplication.class);
		deployment.addClass(AvailabilityEndpoint.class);
		deployment.addClass(Inventory.class);
		deployment.addClass(InventoryService.class);
		deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
		deployment.addAsWebInfResource(new ClassLoaderAsset("import.sql", Main.class.getClassLoader()), "classes/META-INF/load.sql");
		deployment.addAsWebInfResource(new StringAsset("<beans xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + 
			       "    xsi:schemaLocation=\"\n" + 
			       "        http://xmlns.jcp.org/xml/ns/javaee\n" + 
			       "        http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd\" bean-discovery-mode=\"all\">\n" + 
			       "</beans>"), "beans.xml");
		//deployment.addAsWebInfResource(new ClassLoaderAsset("beans.xml", Main.class.getClassLoader()), "beans.xml");
		


		deployment.addAllDependencies();
		
		swarm.fraction(datasourceWithPostgresql());
		
		swarm.fraction(LoggingFraction.createDefaultLoggingFraction())
		.fraction(new CDIFraction())
		.fraction(new JPAFraction())
		.start()
		.deploy(deployment);
		}catch(Exception e){
			e.printStackTrace();
				throw e;
		}
	}
	private static DatasourcesFraction datasourceWithPostgresql() {
		return new DatasourcesFraction()
				.jdbcDriver(System.getProperty("db.drivername", "org.postgresql"), (d) -> {
					d.driverClassName(System.getProperty("db.driver.className","org.postgresql.Driver"));
					d.xaDatasourceClass(System.getProperty("db.driver.datasourceClass","org.postgresql.xa.PGXADataSource"));
					d.driverModuleName(System.getProperty("db.driver.module","org.postgresql"));
				})
				.dataSource(System.getProperty("db.datasource","InventoryDS"), (ds) -> {
					ds.driverName(System.getProperty("db.drivername","org.postgresql"));
					ds.connectionUrl("jdbc:postgresql://"+ "172.17.0.2"+":"
							+"5432"+"/"+"inventory");
					ds.userName(System.getProperty("db.username","jasingh"));
					ds.password(System.getProperty("db.password","123456"));
				});
	}
}