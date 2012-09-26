/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao.factory;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.SchemaDAO;

/**
 * Creates an instance of a {@link SchemaDAO}
 */
public final class SchemaDAOFactory
{
    public SchemaDAO createInstance(final EntityManager em)
    {
        return new SchemaDAO(em);
    }
}
