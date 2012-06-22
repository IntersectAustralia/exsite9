package au.org.intersect.exsite9.service.factory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.config.TargetDatabase;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;
import org.hsqldb.jdbcDriver;

public final class EntityManagerFactoryFactory extends AbstractServiceFactory
{
    private static final Logger LOG = Logger.getLogger(EntityManagerFactoryFactory.class);

    /**
     * @{inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Object create(final Class serviceInterface, final IServiceLocator parentLocator, final IServiceLocator locator)
    {
        LOG.info("Initializing EntityManagerFactory");

        final Map<String, Object> properties = new HashMap<String, Object>();
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
        properties.put(PersistenceUnitProperties.DDL_GENERATION, "create-tables");
        
        // Rewire eclipselink to log via log4j
        properties.put("eclipselink.logging.logger", "org.eclipse.persistence.logging.CommonsLoggingSessionLog");
        properties.put("eclipselink.logging.level", "FINE");
        properties.put("eclipselink.logging.exceptions", "true");

        return new PersistenceProvider().createEntityManagerFactory("jpa", properties);
    }

}
