package au.org.intersect.exsite9.dao;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataAttribute;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
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

        assertNull(childGroup.getParentGroup());

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

        final MetadataCategory cat = new MetadataCategory("some metadata category", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
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
    public void testGetGroupsWithAssociatedMetadataAttribute()
    {
        final EntityManager em = createEntityManager();
        final GroupDAO groupDAO = groupDAOFactory.createInstance(em);

        final MetadataAssociationDAO metadataAssociationDAO = new MetadataAssociationDAO(em);
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(em);
        final MetadataAttributeDAO metadataAttributeDAO = new MetadataAttributeDAO(em);

        final Group group = new Group("group with metadata and attribute");
        groupDAO.createGroup(group);
        assertNotNull(group.getId());

        final MetadataCategory cat = new MetadataCategory("some metadata category", MetadataCategoryType.FREETEXT, MetadataCategoryUse.optional);
        final MetadataValue val = new MetadataValue("some value");
        cat.getValues().add(val);
        final MetadataAttributeValue mdav = new MetadataAttributeValue("mdav");
        final MetadataAttribute mda = new MetadataAttribute("name", Arrays.asList(mdav));
        metadataAttributeDAO.createMetadataAttribute(mda);
        
        cat.setMetadataAttribute(mda);
        metadataCategoryDAO.createMetadataCategory(cat);

        assertNotNull(cat.getId());
        assertNotNull(val.getId());
        assertNotNull(mdav.getId());
        assertNotNull(mda.getId());

        final MetadataAssociation metadataAssociation = new MetadataAssociation(cat);
        metadataAssociation.getMetadataValues().add(val);
        metadataAssociation.setMetadataAttributeValue(mdav);
        metadataAssociationDAO.createMetadataAssociation(metadataAssociation);
        assertNotNull(metadataAssociation.getId());

        group.getMetadataAssociations().add(metadataAssociation);
        groupDAO.updateGroup(group);

        final List<Group> out = groupDAO.getGroupsWithAssociatedMetadataAttribute(cat, mdav);
        assertFalse(out.isEmpty());
        assertEquals(1, out.size());
        assertEquals(group, out.get(0));

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

        assertNull(researchFile.getParentGroup());

        em.close();
    }
    
    @Test
    public void testGetGroupsContainingSelectedFiles()
    {
        final EntityManager em = createEntityManager();
        final GroupDAO groupDAO = groupDAOFactory.createInstance(em);
        final ResearchFileDAO researchFileDAO = new ResearchFileDAO(em);
        
        // Create files
        
        ResearchFile file1 = new ResearchFile(new File("File1"));
        researchFileDAO.createResearchFile(file1);
        
        ResearchFile file2 = new ResearchFile(new File("File2"));
        researchFileDAO.createResearchFile(file2);
        
        ResearchFile file3 = new ResearchFile(new File("File3"));
        researchFileDAO.createResearchFile(file3);
        
        // Create list of selected files
        
        List<ResearchFile> selectedFiles = new ArrayList<ResearchFile>();
        selectedFiles.add(file1);
        selectedFiles.add(file2);
        selectedFiles.add(file3);
        
        // Create group hierarchy
        
        final Group rootNode = new Group("Root");
        groupDAO.createGroup(rootNode);
        final Group group1 = new Group("Group One");
        groupDAO.createGroup(group1);
        final Group group2 = new Group("Group Two");
        groupDAO.createGroup(group2);
        
        rootNode.getGroups().add(group1);
        group1.setParentGroup(rootNode);
        
        group1.getGroups().add(group2);
        group2.setParentGroup(group1);

        groupDAO.updateGroup(rootNode);
        groupDAO.updateGroup(group1);
        groupDAO.updateGroup(group2);
        
        // No files in any groups, no selected groups
        
        rootNode.getResearchFiles().clear();
        groupDAO.updateGroup(rootNode);
        
        group1.getResearchFiles().clear();
        groupDAO.updateGroup(group1);
        
        group2.getResearchFiles().clear();
        groupDAO.updateGroup(group2);
        
        List<Group> selectedGroups = GroupDAO.getGroupsContainingSelectedFiles(selectedFiles);
        
        assertEquals("No selected groups", 0, selectedGroups.size());
        
        // Root group has a file, 1 selected group
        
        rootNode.getResearchFiles().clear();
        rootNode.getResearchFiles().add(file1);
        file1.setParentGroup(rootNode);
        
        group1.getResearchFiles().clear();
        group2.getResearchFiles().clear();

        groupDAO.updateGroup(rootNode);
        groupDAO.updateGroup(group1);
        groupDAO.updateGroup(group2);
        researchFileDAO.updateResearchFile(file1);
        researchFileDAO.updateResearchFile(file2);
        researchFileDAO.updateResearchFile(file3);
        
        selectedGroups = GroupDAO.getGroupsContainingSelectedFiles(selectedFiles);
        assertEquals("One selected group", 1,selectedGroups.size());
        assertTrue("The root group is selected",selectedGroups.contains(rootNode));
        assertFalse("Group 1 is not selected",selectedGroups.contains(group1));
        assertFalse("Group 2 is not selected",selectedGroups.contains(group2));
        
        // Root group has 2 files, 1 selected group
        
        rootNode.getResearchFiles().clear();
        rootNode.getResearchFiles().add(file1);
        file1.setParentGroup(rootNode);
        rootNode.getResearchFiles().add(file2);
        file2.setParentGroup(rootNode);

        group1.getResearchFiles().clear();
        group2.getResearchFiles().clear();

        groupDAO.updateGroup(rootNode);
        groupDAO.updateGroup(group1);
        groupDAO.updateGroup(group2);
        researchFileDAO.updateResearchFile(file1);
        researchFileDAO.updateResearchFile(file2);
        researchFileDAO.updateResearchFile(file3);
        
        selectedGroups = GroupDAO.getGroupsContainingSelectedFiles(selectedFiles);
        assertEquals("One selected group", 1,selectedGroups.size());
        assertTrue("The root group is selected",selectedGroups.contains(rootNode));
        assertFalse("Group 1 is not selected",selectedGroups.contains(group1));
        assertFalse("Group 2 is not selected",selectedGroups.contains(group2));
        
        // Group 1 has 1 file, 2 selected groups
        rootNode.getResearchFiles().clear();
        group1.getResearchFiles().add(file1);
        file1.setParentGroup(group1);
        group2.getResearchFiles().clear();
        
        groupDAO.updateGroup(rootNode);
        groupDAO.updateGroup(group1);
        groupDAO.updateGroup(group2);
        researchFileDAO.updateResearchFile(file1);
        researchFileDAO.updateResearchFile(file2);
        researchFileDAO.updateResearchFile(file3);
        
        selectedGroups = GroupDAO.getGroupsContainingSelectedFiles(selectedFiles);
        assertEquals("Two selected group", 2,selectedGroups.size());
        assertTrue("The root group is selected",selectedGroups.contains(rootNode));
        assertTrue("Group 1 is selected",selectedGroups.contains(group1));
        assertFalse("Group 2 is not selected",selectedGroups.contains(group2));
        
        // Root node has 1 file, Group 1 has 1 file, 2 selected groups
        
        rootNode.getResearchFiles().clear();
        rootNode.getResearchFiles().add(file1);
        file1.setParentGroup(rootNode);

        group1.getResearchFiles().clear();
        group1.getResearchFiles().add(file2);
        file2.setParentGroup(group1);
        group2.getResearchFiles().clear();
        
        groupDAO.updateGroup(rootNode);
        groupDAO.updateGroup(group1);
        groupDAO.updateGroup(group2);
        researchFileDAO.updateResearchFile(file1);
        researchFileDAO.updateResearchFile(file2);
        researchFileDAO.updateResearchFile(file3);
        
        selectedGroups = GroupDAO.getGroupsContainingSelectedFiles(selectedFiles);
        assertEquals("Two selected group", 2,selectedGroups.size());
        assertTrue("The root group is selected",selectedGroups.contains(rootNode));
        assertTrue("Group 1 is selected",selectedGroups.contains(group1));
        assertFalse("Group 2 is not selected",selectedGroups.contains(group2));
        
        // Group 2 has 1 file, 3 selected groups
        rootNode.getResearchFiles().clear();
        
        group1.getResearchFiles().clear();
        group2.getResearchFiles().clear();
        group2.getResearchFiles().add(file1);
        file1.setParentGroup(group2);

        groupDAO.updateGroup(rootNode);
        groupDAO.updateGroup(group1);
        groupDAO.updateGroup(group2);
        researchFileDAO.updateResearchFile(file1);
        researchFileDAO.updateResearchFile(file2);
        researchFileDAO.updateResearchFile(file3);
        
        selectedGroups = GroupDAO.getGroupsContainingSelectedFiles(selectedFiles);
        assertEquals("Three selected group", 3,selectedGroups.size());
        assertTrue("The root group is selected",selectedGroups.contains(rootNode));
        assertTrue("Group 1 is selected",selectedGroups.contains(group1));
        assertTrue("Group 2 is selected",selectedGroups.contains(group2));
    }
}
