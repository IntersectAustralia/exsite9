/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import au.org.intersect.exsite9.dao.SchemaDAO;
import au.org.intersect.exsite9.dao.factory.SchemaDAOFactory;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Schema;

/**
 * 
 */
public final class SchemaService implements ISchemaService
{
    private final EntityManagerFactory emf;
    private final SchemaDAOFactory schemaDAOFactory;

    public SchemaService(final EntityManagerFactory emf, final SchemaDAOFactory schemaDAOFactory)
    {
        this.emf = emf;
        this.schemaDAOFactory = schemaDAOFactory;
    }

    @Override
    public void addMetadataCategoryToSchema(final Schema schema, final MetadataCategory metadataCategory)
    {
        final EntityManager em = this.emf.createEntityManager();
        try
        {
            final SchemaDAO schemaDAO = this.schemaDAOFactory.createInstance(em);
            schema.getMetadataCategories().add(metadataCategory);
            schemaDAO.updateSchema(schema);
        }
        finally
        {
            em.close();
        }
    }

}
