/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service.factory;

import java.io.File;

import javax.persistence.EntityManagerFactory;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import au.org.intersect.exsite9.dao.factory.MetadataCategoryDAOFactory;
import au.org.intersect.exsite9.dao.factory.SchemaDAOFactory;
import au.org.intersect.exsite9.service.SchemaService;

/**
 * Responsible for creating instances of {@link SchemaService}
 */
public final class SchemaServiceFactory extends AbstractServiceFactory
{

    /**
     * 
     */
    public SchemaServiceFactory()
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Object create(final Class serviceInterface, final IServiceLocator parentLocator, final IServiceLocator locator)
    {
        // Lookup the default schema directory (which is under the workspace directory).
        final String workspaceDir = Platform.getInstallLocation().getURL().getPath();
        final File schemaDir = new File(workspaceDir, "schemas");

        final File schemaDirToUse;
        if (schemaDir.exists() && schemaDir.isDirectory())
        {
            schemaDirToUse = schemaDir;
        }
        else
        {
            schemaDirToUse = null;
        }

        final File configurationDir = new File(workspaceDir, "configuration");
        final File metadataSchemaSchema = new File(configurationDir, "ExSite9MetadataSchema.rng");

        final EntityManagerFactory emf = (EntityManagerFactory) PlatformUI.getWorkbench().getService(EntityManagerFactory.class);
        final SchemaDAOFactory schemaDAOFactory = new SchemaDAOFactory();
        final MetadataCategoryDAOFactory metadataCategoryDAOFactory = new MetadataCategoryDAOFactory();
        final SchemaService schemaService = new SchemaService(schemaDirToUse, metadataSchemaSchema, emf, schemaDAOFactory, metadataCategoryDAOFactory);
        return schemaService;
    }
}
