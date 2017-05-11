package com.redhat.coolstore.datasources;

import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.datasources.DatasourcesFraction;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Running " + Main.class.getCanonicalName() + ".main"+"  "+System.getProperty("db.username"));
        
        Swarm swarm = new Swarm();

        String useDB = System.getProperty("swarm.use.db", "postgresql");
        
        // Configure the Datasources subsystem with a driver
        // and a datasource
        switch (useDB.toLowerCase()) {
            case "h2":
                swarm.fraction(datasourceWithH2()); break;
            case "postgresql" :
                swarm.fraction(datasourceWithPostgresql()); break;
            case "mysql" :
                swarm.fraction(datasourceWithMysql()); break;
            default:
                swarm.fraction(datasourceWithH2());
        }

        // Start the swarm and deploy the default war
        swarm.start().deploy();
    }

    private static DatasourcesFraction datasourceWithH2() {
        return new DatasourcesFraction()
                .jdbcDriver("h2", (d) -> {
                    d.driverClassName("org.h2.Driver");
                    d.xaDatasourceClass("org.h2.jdbcx.JdbcDataSource");
                    d.driverModuleName("com.h2database.h2");
                })
                .dataSource("ExampleDS", (ds) -> {
                    ds.driverName("h2");
                    ds.connectionUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
                    ds.userName("sa");
                    ds.password("sa");
                });
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
                    ds.connectionUrl("jdbc:postgresql://"+ System.getProperty("db.server","localhost")+":"
                    		+System.getProperty("db.port","5433")+"/"+System.getProperty("db.name","mydb"));
                    ds.userName(System.getProperty("db.username","jasingh"));
                    ds.password(System.getProperty("db.password","123456"));
                });
    }

    private static DatasourcesFraction datasourceWithMysql() {
        return new DatasourcesFraction()
                .jdbcDriver("com.mysql", (d) -> {
                    d.driverClassName("com.mysql.jdbc.Driver");
                    d.xaDatasourceClass("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
                    d.driverModuleName("com.mysql");
                })
                .dataSource("ExampleDS", (ds) -> {
                    ds.driverName("com.mysql");
                    ds.connectionUrl("jdbc:mysql://localhost:3306/mysql");
                    ds.userName("root");
                    ds.password("root");
                });
    }

}
