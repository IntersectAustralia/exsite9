/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.database;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.config.TargetDatabase;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.hsqldb.jdbcDriver;

public class ExSite9EntityManagerFactory
{
    private static final Logger LOG = Logger.getLogger(ExSite9EntityManagerFactory.class);

    private static EntityManagerFactory emf = null;

    public ExSite9EntityManagerFactory()
    {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PersistenceUnitProperties.TARGET_DATABASE, TargetDatabase.HSQL);
        properties.put(PersistenceUnitProperties.JDBC_DRIVER, jdbcDriver.class.getCanonicalName());
        
        final String connectionURL;

        final List<String> platformArgs = Arrays.asList(Platform.getCommandLineArgs());
        if (platformArgs.contains("-exsite9.hsqldb.debug"))
        {
            connectionURL = "jdbc:hsqldb:hsql://localhost/exsite9";
            LOG.info("Using HSQLDB connection string for debug: " + connectionURL);
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

        // Rewire eclipselink to log via log4j
        properties.put("eclipselink.logging.logger", "org.eclipse.persistence.logging.CommonsLoggingSessionLog");
        properties.put("eclipselink.logging.level", "INFO");
        properties.put("eclipselink.logging.exceptions", "true");

        emf = new PersistenceProvider().createEntityManagerFactory("jpa", properties);
    }

    public EntityManager getEntityManager() 
    {
        return emf.createEntityManager();
    }
    
}
