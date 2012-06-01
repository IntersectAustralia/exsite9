package au.org.intersect.exsite9.dao.factory;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.ProjectDAO;

public class ProjectDAOFactory
{
    public ProjectDAO createInstance(EntityManager em){
        return new ProjectDAO(em);
    }
}
