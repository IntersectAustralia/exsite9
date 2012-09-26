/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao.factory;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.FolderDAO;

/**
 * Creates an instance of a {@link FolderDAO}
 */
public class FolderDAOFactory
{
    public FolderDAO createInstance(EntityManager em)
    {
        return new FolderDAO(em);
    }
}
