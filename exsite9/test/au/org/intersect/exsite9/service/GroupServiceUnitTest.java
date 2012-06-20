package au.org.intersect.exsite9.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.MetadataCategoryDAO;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataAssociationDAOFactory;
import au.org.intersect.exsite9.database.ExSite9EntityManagerFactory;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.dto.HierarchyMoveDTO;

public class GroupServiceUnitTest extends DAOTest
{

    private GroupService groupService;
    
    @Test
    public void createNewGroupTest()
    {
        ExSite9EntityManagerFactory emf = mock(ExSite9EntityManagerFactory.class);

        stub(emf.getEntityManager()).toReturn(createEntityManager());
        
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory);
        
        Group group = groupService.createNewGroup("Group One");
        
        Group newGroup = groupDAOFactory.createInstance(createEntityManager()).findById(group.getId());
        
        assertEquals(group, newGroup);
    }
    
    @Test
    public void addGroupToGroupTest()
    {
        ExSite9EntityManagerFactory emf = mock(ExSite9EntityManagerFactory.class);
        stub(emf.getEntityManager()).toReturn(createEntityManager());
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory);
        
        Group parentGroup = new Group("Parent");
        Group childGroup = new Group("Child");
        
        assertEquals(parentGroup.getGroups().size(),0);
        
        groupService.addChildGroup(parentGroup, childGroup);
        
        assertEquals(parentGroup.getGroups().size(),1);
    }
    
    @Test
    public void moveGroupsTest()
    {
        ExSite9EntityManagerFactory emf = mock(ExSite9EntityManagerFactory.class);
        stub(emf.getEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());
        
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory);
        
        Group parent1Group = new Group("Parent1");
        Group child1Group = new Group("Child1");

        Group parent2Group = new Group("Parent2");
        Group child2Group = new Group("Child2");

        groupService.addChildGroup(parent1Group, child1Group);
        groupService.addChildGroup(parent2Group, child2Group);
        
        List<HierarchyMoveDTO> moveList = new ArrayList<HierarchyMoveDTO>(0);
        
        assertEquals(parent1Group.getGroups().size(),1);
        assertEquals(parent2Group.getGroups().size(),1);
        
        groupService.performHierarchyMove(moveList);
        
        assertEquals(parent1Group.getGroups().size(),1);
        assertEquals(parent2Group.getGroups().size(),1);
        
        moveList.add(new HierarchyMoveDTO(child1Group,parent1Group,parent2Group));
        groupService.performHierarchyMove(moveList);
        assertEquals(parent1Group.getGroups().size(),0);
        assertEquals(parent2Group.getGroups().size(),2);
        moveList.clear();

        moveList.add(new HierarchyMoveDTO(child2Group,parent2Group,parent1Group));
        groupService.performHierarchyMove(moveList);
        assertEquals(parent1Group.getGroups().size(),1);
        assertEquals(parent2Group.getGroups().size(),1);
        moveList.clear();
    
        moveList.add(new HierarchyMoveDTO(child1Group,parent2Group,parent1Group));
        groupService.performHierarchyMove(moveList);
        assertEquals(parent1Group.getGroups().size(),2);
        assertEquals(parent2Group.getGroups().size(),0);
        moveList.clear();
    }
   
    @Test
    public void moveGroupWithGroupTest()
    {
        ExSite9EntityManagerFactory emf = mock(ExSite9EntityManagerFactory.class);
        stub(emf.getEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());
        
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory);
        
        Group parentGroup = new Group("Parent Group");
        Group childGroup = new Group("Child Group");
        Group grandchildGroup = new Group("Grandchild Group");
        Group grandchild2Group = new Group("Grandchild 2 Group");

        groupService.addChildGroup(childGroup, grandchildGroup);
        groupService.addChildGroup(parentGroup, childGroup);
        
        assertTrue("Child is in parent",parentGroup.getGroups().contains(childGroup));
        
        groupService.addChildGroup(childGroup, grandchild2Group);
        
        assertTrue("Child is still in parent",parentGroup.getGroups().contains(childGroup));
        
        Group parent2Group = new Group("Parent Two");
        
        assertEquals(parentGroup.getGroups().size(),1);
        assertEquals(childGroup.getGroups().size(),2);
        assertEquals(parent2Group.getGroups().size(),0);
        
        List<HierarchyMoveDTO> moveList = new ArrayList<HierarchyMoveDTO>(0);
        moveList.add(new HierarchyMoveDTO(childGroup,parentGroup,parent2Group));
        
        groupService.performHierarchyMove(moveList);

        assertEquals(parentGroup.getGroups().size(),0);
        assertEquals(childGroup.getGroups().size(),2);
        assertEquals(parent2Group.getGroups().size(),1);

        moveList.clear();
    }
    
    @Test
    public void moveGroupWithFileTest()
    {
        ExSite9EntityManagerFactory emf = mock(ExSite9EntityManagerFactory.class);
        stub(emf.getEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());
        
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory);
        
        Group parentGroup = new Group("Parent Group");
        Group childGroup = new Group("Child Group");
        
        ResearchFile file = new ResearchFile(new File("test.dat"));
        
        childGroup.getResearchFiles().add(file);
        
        groupService.addChildGroup(parentGroup, childGroup);
        
        Group parent2Group = new Group("Parent Two");
        
        assertEquals(parentGroup.getGroups().size(),1);
        assertEquals(childGroup.getGroups().size(),0);
        assertEquals(childGroup.getResearchFiles().size(),1);
        assertEquals(parent2Group.getGroups().size(),0);
        
        List<HierarchyMoveDTO> moveList = new ArrayList<HierarchyMoveDTO>(0);
        moveList.add(new HierarchyMoveDTO(childGroup,parentGroup,parent2Group));
        
        groupService.performHierarchyMove(moveList);

        assertEquals(parentGroup.getGroups().size(),0);
        assertEquals(childGroup.getGroups().size(),0);
        assertEquals(childGroup.getResearchFiles().size(),1);
        assertEquals(parent2Group.getGroups().size(),1);
        
        moveList.clear();
    }

    @Test
    public void testAssociateMetadata()
    {
        final ExSite9EntityManagerFactory emf = mock(ExSite9EntityManagerFactory.class);
        stub(emf.getEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());

        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(emf.getEntityManager());
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory);

        final MetadataCategory metadataCategory = new MetadataCategory("metadataCategory");
        final MetadataValue metadataValue = new MetadataValue("metadataValue");
        metadataCategory.getValues().add(metadataValue);
        metadataCategoryDAO.createMetadataCategory(metadataCategory);
        final Group group = new Group("group name");
        groupService.associateMetadata(group, metadataCategory, metadataValue);

        final List<MetadataAssociation> metadataAssociations = group.getMetadataAssociations();
        assertEquals(1, metadataAssociations.size());
        MetadataAssociation metadataAssociation = metadataAssociations.get(0);
        assertNotNull(metadataAssociation);
        assertEquals(metadataCategory, metadataAssociation.getMetadataCategory());
        assertEquals(1, metadataAssociation.getMetadataValues().size());
        assertEquals(metadataValue, metadataAssociation.getMetadataValues().get(0));

        // Doing it again does nothing.
        groupService.associateMetadata(group, metadataCategory, metadataValue);
        assertEquals(1, metadataAssociations.size());
        metadataAssociation = metadataAssociations.get(0);
        assertNotNull(metadataAssociation);
        assertEquals(metadataCategory, metadataAssociation.getMetadataCategory());
        assertEquals(1, metadataAssociation.getMetadataValues().size());
        assertEquals(metadataValue, metadataAssociation.getMetadataValues().get(0));

        // Add another association under the same category.
        final MetadataValue metadataValue2 = new MetadataValue("metadataValue two");
        metadataCategory.getValues().add(metadataValue2);
        metadataCategoryDAO.updateMetadataCategory(metadataCategory);

        groupService.associateMetadata(group, metadataCategory, metadataValue2);
        assertEquals(1, metadataAssociations.size());
        metadataAssociation = metadataAssociations.get(0);
        assertNotNull(metadataAssociation);
        assertEquals(metadataCategory, metadataAssociation.getMetadataCategory());
        assertEquals(2, metadataAssociation.getMetadataValues().size());
        assertTrue(metadataAssociation.getMetadataValues().contains(metadataValue));
        assertTrue(metadataAssociation.getMetadataValues().contains(metadataValue2));

        // Ad another association under a different category.
        final MetadataCategory metadataCategory2 = new MetadataCategory("metadatacategory number 2");
        final MetadataValue metadataValue3 = new MetadataValue("metadata value three");
        metadataCategory2.getValues().add(metadataValue3);
        metadataCategoryDAO.createMetadataCategory(metadataCategory2);

        groupService.associateMetadata(group, metadataCategory2, metadataValue3);
        assertEquals(2, group.getMetadataAssociations().size());
    }

    @Test
    public void testDisassociateMetadata()
    {
        final ExSite9EntityManagerFactory emf = mock(ExSite9EntityManagerFactory.class);
        stub(emf.getEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());

        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(emf.getEntityManager());
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory);

        // Disassociate metadata that is not associated.
        final MetadataCategory metadataCategory1 = new MetadataCategory("metadataCategory");
        final MetadataValue metadataValue1 = new MetadataValue("metadataValue");
        metadataCategory1.getValues().add(metadataValue1);
        metadataCategoryDAO.createMetadataCategory(metadataCategory1);

        final MetadataCategory metadataCategory2 = new MetadataCategory("metadataCategory two");
        final MetadataValue metadataValue2 = new MetadataValue("metadataValue two");
        final MetadataValue metadataValue3 = new MetadataValue("metadataValue three");
        metadataCategory2.getValues().add(metadataValue2);
        metadataCategory2.getValues().add(metadataValue3);
        metadataCategoryDAO.createMetadataCategory(metadataCategory2);

        final Group group = new Group("group name");
        groupService.disassociateMetadata(group, metadataCategory1, metadataValue1);
        assertTrue(group.getMetadataAssociations().isEmpty());
        groupService.associateMetadata(group, metadataCategory1, metadataValue1);
        groupService.associateMetadata(group, metadataCategory2, metadataValue2);
        groupService.associateMetadata(group, metadataCategory2, metadataValue3);

        List<MetadataAssociation> metadataAssociations = group.getMetadataAssociations();
        assertEquals(2, metadataAssociations.size());

        groupService.disassociateMetadata(group, metadataCategory2, metadataValue2);
        metadataAssociations = group.getMetadataAssociations();
        assertEquals(2, metadataAssociations.size());

        groupService.disassociateMetadata(group, metadataCategory1, metadataValue1);
        metadataAssociations = group.getMetadataAssociations();
        assertEquals(1, metadataAssociations.size());

        groupService.disassociateMetadata(group, metadataCategory2, metadataValue3);
        metadataAssociations = group.getMetadataAssociations();
        assertEquals(0, metadataAssociations.size());
    }
}
