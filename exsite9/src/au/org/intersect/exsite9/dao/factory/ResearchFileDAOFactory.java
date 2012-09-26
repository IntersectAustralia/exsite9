/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao.factory;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.ResearchFileDAO;

/**
 * Creates an instance of a {@link ResearchFileDAO}
 */
public class ResearchFileDAOFactory
{
    public ResearchFileDAO createInstance(EntityManager em)
    {
        return new ResearchFileDAO(em);
    }
}
