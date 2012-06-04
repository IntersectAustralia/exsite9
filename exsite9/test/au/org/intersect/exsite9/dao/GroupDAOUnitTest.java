package au.org.intersect.exsite9.dao;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.ResearchFile;

public class GroupDAOUnitTest extends DAOTest
{
    private static GroupDAOFactory groupDAOFactory;
    
    @BeforeClass
    public static void setupOnce()
    {
        groupDAOFactory = new GroupDAOFactory();
    }
    
    @Test
    public void createNewGroupTest()
    {
        EntityManager em = createEntityManager();
        GroupDAO groupDAO = groupDAOFactory.createInstance(em);
        
        Group group = new Group("Group One");
        
        groupDAO.createGroup(group);
        
        Group group2 = groupDAO.findById(group.getId());
        
        assertEquals(group, group2);
        
        em.close();
    }
    
    @Test
    public void updateGroupTest()
    {
        EntityManager em = createEntityManager();
        GroupDAO groupDAO = groupDAOFactory.createInstance(em);
        
        Group group = new Group("Group One");
        groupDAO.createGroup(group);
        
        Group group2 = groupDAO.findById(group.getId());
        assertEquals(group, group2);
        
        group.getResearchFiles().add(new ResearchFile(new File("/tmp/test1.txt")));
        groupDAO.updateGroup(group);
        
        group2 = groupDAO.findById(group.getId());
        assertEquals(group, group2);
        
        em.close();
    }
}
