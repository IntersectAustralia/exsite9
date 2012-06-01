package au.org.intersect.exsite9.dao.factory;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.ResearchFileDAO;

public class ResearchFileDAOFactory
{
    public ResearchFileDAO createInstance(EntityManager em)
    {
        return new ResearchFileDAO(em);
    }
}
