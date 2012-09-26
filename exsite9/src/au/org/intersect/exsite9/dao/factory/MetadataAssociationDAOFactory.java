/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao.factory;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.MetadataAssociationDAO;

/**
 * Creates an instance of a {@link MetadataAssociationDAO}
 */
public final class MetadataAssociationDAOFactory
{
    public MetadataAssociationDAO createInstance(final EntityManager em)
    {
        return new MetadataAssociationDAO(em);
    }
}
