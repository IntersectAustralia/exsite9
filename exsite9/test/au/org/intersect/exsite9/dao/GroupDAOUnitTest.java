package au.org.intersect.exsite9.dao;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
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

    @Test
    public void testDeleteGroup()
    {
        final EntityManager em = createEntityManager();
        final GroupDAO groupDAO = groupDAOFactory.createInstance(em);

        final Group group = new Group("Group to test delete group with");
        groupDAO.createGroup(group);
        assertNotNull(group.getId());

        groupDAO.deleteGroup(group);

        assertNull(em.find(Group.class, group.getId()));

        em.close();
    }

    @Test
    public void testDeleteGroupInTransaction()
    {
        final EntityManager em = createEntityManager();
        final GroupDAO groupDAO = groupDAOFactory.createInstance(em);

        final Group group = new Group("Group to test delete group (in a transaction) with");
        groupDAO.createGroup(group);
        assertNotNull(group.getId());

        em.getTransaction().begin();
        groupDAO.deleteGroup(group);
        em.getTransaction().commit();

        assertNull(em.find(Group.class, group.getId()));

        em.close();
    }

    @Test
    public void testGetParentGroup()
    {
        final EntityManager em = createEntityManager();
        final GroupDAO groupDAO = groupDAOFactory.createInstance(em);

        final Group parentGroup = new Group("parent");
        final Group childGroup = new Group("child");

        groupDAO.createGroup(parentGroup);
        groupDAO.createGroup(childGroup);
        parentGroup.getGroups().add(childGroup);
        groupDAO.updateGroup(parentGroup);

        assertNotNull(parentGroup.getId());
        assertNotNull(childGroup.getId());

        assertEquals(parentGroup, groupDAO.getParent(childGroup));

        em.close();
    }

    @Test
    public void testGetMultipleParentGroup()
    {
        final EntityManager em = createEntityManager();
        final GroupDAO groupDAO = groupDAOFactory.createInstance(em);

        final Group parent1Group = new Group("parent1");
        final Group parent2Group = new Group("parent2");
        final Group childGroup = new Group("child");

        groupDAO.createGroup(parent1Group);
        groupDAO.createGroup(parent2Group);
        groupDAO.createGroup(childGroup);
        parent1Group.getGroups().add(childGroup);
        parent2Group.getGroups().add(childGroup);
        groupDAO.updateGroup(parent1Group);
        groupDAO.updateGroup(parent2Group);

        assertNotNull(parent1Group.getId());
        assertNotNull(parent2Group.getId());
        assertNotNull(childGroup.getId());

        assertNull(groupDAO.getParent(childGroup));

        em.close();
    }

    @Test
    public void testGetGroupsWithAssociatedMetadata()
    {
        final EntityManager em = createEntityManager();
        final GroupDAO groupDAO = groupDAOFactory.createInstance(em);
        final MetadataAssociationDAO metadataAssociationDAO = new MetadataAssociationDAO(em);
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(em);

        final Group group = new Group("group with metadata");

        groupDAO.createGroup(group);

        assertNotNull(group.getId());

        final MetadataCategory cat = new MetadataCategory("some metadata category");
        final MetadataValue val = new MetadataValue("some value");
        cat.getValues().add(val);

        metadataCategoryDAO.createMetadataCategory(cat);
        assertNotNull(cat.getId());
        assertNotNull(val.getId());

        final MetadataAssociation metadataAssociation = new MetadataAssociation(cat);
        metadataAssociation.getMetadataValues().add(val);
        metadataAssociationDAO.createMetadataAssociation(metadataAssociation);
        assertNotNull(metadataAssociation.getId());

        group.getMetadataAssociations().add(metadataAssociation);
        groupDAO.updateGroup(group);

        final List<Group> out = groupDAO.getGroupsWithAssociatedMetadata(cat, val);
        assertFalse(out.isEmpty());
        assertEquals(1, out.size());
        assertEquals(group, out.get(0));

        em.close();
    }

    @Test
    public void testGetParentResearchFile()
    {
        final EntityManager em = createEntityManager();
        final GroupDAO groupDAO = groupDAOFactory.createInstance(em);
        final ResearchFileDAO researchFileDAO = new ResearchFileDAO(em);

        final Group parentGroup = new Group("parent");
        final ResearchFile researchFile = new ResearchFile(new File("some File.txt"));
        

        groupDAO.createGroup(parentGroup);
        researchFileDAO.createResearchFile(researchFile);
        assertNotNull(parentGroup.getId());
        assertNotNull(researchFile.getId());

        parentGroup.getResearchFiles().add(researchFile);
        groupDAO.updateGroup(parentGroup);

        assertEquals(parentGroup, groupDAO.getParent(researchFile));

        em.close();
    }

    @Test
    public void testGetMultipleParentResearchFile()
    {
        final EntityManager em = createEntityManager();
        final GroupDAO groupDAO = groupDAOFactory.createInstance(em);
        final ResearchFileDAO researchFileDAO = new ResearchFileDAO(em);

        final Group parentGroup1 = new Group("parent 1");
        final Group parentGroup2 = new Group("parent 2");
        final ResearchFile researchFile = new ResearchFile(new File("some File.txt"));

        groupDAO.createGroup(parentGroup1);
        groupDAO.createGroup(parentGroup2);
        researchFileDAO.createResearchFile(researchFile);
        assertNotNull(parentGroup1.getId());
        assertNotNull(parentGroup2.getId());
        assertNotNull(researchFile.getId());

        parentGroup1.getResearchFiles().add(researchFile);
        parentGroup2.getResearchFiles().add(researchFile);
        groupDAO.updateGroup(parentGroup1);
        groupDAO.updateGroup(parentGroup2);

        assertNull(groupDAO.getParent(researchFile));

        em.close();
    }
}
