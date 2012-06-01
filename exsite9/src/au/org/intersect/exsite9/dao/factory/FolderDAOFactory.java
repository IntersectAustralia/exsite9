package au.org.intersect.exsite9.dao.factory;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.FolderDAO;

public class FolderDAOFactory
{
    public FolderDAO createInstance(EntityManager em)
    {
        return new FolderDAO(em);
    }
}
