/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.dao;

import static org.eclipse.persistence.config.PersistenceUnitProperties.TRANSACTION_TYPE;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitTransactionType;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.config.TargetDatabase;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.hsqldb.jdbcDriver;

public class JPATest
{

    private static EntityManagerFactory emf = null;
    
    public static EntityManager createEntityManager()
    {
        if (emf == null)
        {
            Map<String, Object> properties = new HashMap<String, Object>();
            
            properties.put(TRANSACTION_TYPE, PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
            
            properties.put(PersistenceUnitProperties.TARGET_DATABASE, TargetDatabase.HSQL);
            properties.put(PersistenceUnitProperties.JDBC_DRIVER, jdbcDriver.class.getCanonicalName());
            properties.put(PersistenceUnitProperties.JDBC_URL,"jdbc:hsqldb:mem:unit-testing-jpa");
            
            properties.put(PersistenceUnitProperties.DDL_GENERATION, "drop-and-create-tables");
            
            properties.put("eclipselink.logging.level", "FINE");
            properties.put("eclipselink.logging.timestamp", "false");
            properties.put("eclipselink.logging.session", "false");
            properties.put("eclipselink.logging.thread", "false");
            properties.put("eclipselink.logging.exceptions", "true");
    
            emf = new PersistenceProvider().createEntityManagerFactory("jpa", properties);
        }
        
        return emf.createEntityManager();
    }
}
