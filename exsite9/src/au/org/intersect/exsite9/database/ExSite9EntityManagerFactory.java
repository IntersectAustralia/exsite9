/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.database;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.core.runtime.Platform;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.config.TargetDatabase;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.hsqldb.jdbcDriver;

public class ExSite9EntityManagerFactory
{

    private static EntityManagerFactory emf = null;
    private static Map<String, Object> properties = new HashMap<String, Object>();

    private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<EntityManager>();

    private static void init() {
        properties.put(PersistenceUnitProperties.TARGET_DATABASE, TargetDatabase.HSQL);
        properties.put(PersistenceUnitProperties.JDBC_DRIVER, jdbcDriver.class.getCanonicalName());

        final String connectionURL;

        final List<String> platformArgs = Arrays.asList(Platform.getCommandLineArgs());
        if (platformArgs.contains("-exsite9.hsqldb.debug"))
        {
            connectionURL = "jdbc:hsqldb:hsql://localhost/exsite9";
            System.out.println("Using HSQLDB connection string for debug: " + connectionURL);
        }
        else
        {
            final String workspace = Platform.getInstallLocation().getURL().getPath();
            connectionURL = "jdbc:hsqldb:file:" + workspace + "/database/exsite9;shutdown=true;hsqldb.write_delay=false;";
        }
        properties.put(PersistenceUnitProperties.JDBC_URL, connectionURL);

        properties.put(PersistenceUnitProperties.JDBC_USER, "sa");
        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, "");
        properties.put(PersistenceUnitProperties.DDL_GENERATION, "drop-and-create-tables");
        
        properties.put("eclipselink.logging.level", "FINE");
        properties.put("eclipselink.logging.timestamp", "false");
        properties.put("eclipselink.logging.session", "false");
        properties.put("eclipselink.logging.thread", "false");
        properties.put("eclipselink.logging.exceptions", "true");

        emf = new PersistenceProvider().createEntityManagerFactory("jpa", properties);
    }

    public static EntityManager createEntityManager() {
        EntityManager entityManager = ENTITY_MANAGER_CACHE.get();
        if (entityManager == null) {
            init();
            entityManager = emf.createEntityManager();
            ENTITY_MANAGER_CACHE.set(entityManager);
        }
        return entityManager;
    }
}
