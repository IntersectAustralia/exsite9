/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao.factory;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.ProjectDAO;

/**
 * Creates an instance of a {@link ProjectDAO}
 */
public class ProjectDAOFactory
{
    public ProjectDAO createInstance(EntityManager em){
        return new ProjectDAO(em);
    }
}
