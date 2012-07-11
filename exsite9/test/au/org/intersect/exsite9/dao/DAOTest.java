/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.dao;

import static org.eclipse.persistence.config.PersistenceUnitProperties.TRANSACTION_TYPE;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitTransactionType;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.config.TargetDatabase;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.hsqldb.jdbcDriver;
import org.junit.After;
import org.junit.Before;

public abstract class DAOTest
{
    protected EntityManagerFactory emf;

    /**
     * Creates a new EntityManagerFactory for each method marked @Test - so each test can start with a clean DB.
     */
    @Before
    public void initEntityManagerFactory()
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

        this.emf = new PersistenceProvider().createEntityManagerFactory("jpa", properties);
    }

    /**
     * Shuts down the EntityManagerFactory after each method marked @Test 
     */
    @After
    public void destroyEntityManagerFactory()
    {
        this.emf.close();
        this.emf = null;
    }

    /**
     * Provides an Entity Manager
     * @return the Entity Manager
     */
    protected EntityManager createEntityManager()
    {
        assertNotNull("EntityManagerFactory must not be null to construct an EntityManager", this.emf);
        return this.emf.createEntityManager();
    }
}
