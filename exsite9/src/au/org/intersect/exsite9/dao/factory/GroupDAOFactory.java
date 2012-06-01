package au.org.intersect.exsite9.dao.factory;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.GroupDAO;

public class GroupDAOFactory
{
    public GroupDAO createInstance(EntityManager em)
    {
        return new GroupDAO(em);
    }
}
