package au.org.intersect.exsite9.dao.factory;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.FieldOfResearchDAO;

public final class FieldOfResearchDAOFactory
{
    public FieldOfResearchDAO createInstance(final EntityManager em)
    {
        return new FieldOfResearchDAO(em);
    }
}
