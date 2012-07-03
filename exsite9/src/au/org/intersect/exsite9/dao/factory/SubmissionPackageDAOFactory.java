/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao.factory;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.SubmissionPackageDAO;

/**
 * 
 */
public final class SubmissionPackageDAOFactory
{
    public SubmissionPackageDAO createInstance(final EntityManager em)
    {
        return new SubmissionPackageDAO(em);
    }
}
