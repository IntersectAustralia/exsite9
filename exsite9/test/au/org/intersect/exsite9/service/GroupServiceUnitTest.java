package au.org.intersect.exsite9.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.GroupDAO;
import au.org.intersect.exsite9.dao.MetadataAssociationDAO;
import au.org.intersect.exsite9.dao.MetadataCategoryDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataAssociationDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.ResearchFileSortField;
import au.org.intersect.exsite9.domain.SortFieldDirection;
import au.org.intersect.exsite9.dto.HierarchyMoveDTO;

public class GroupServiceUnitTest extends DAOTest
{

    private GroupService groupService;
    
    @Test
    public void createNewGroupTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);

        stub(emf.createEntityManager()).toReturn(createEntityManager());
        
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory, researchFileDAOFactory);
        
        Group group = groupService.createNewGroup("Group One");
        
        Group newGroup = groupDAOFactory.createInstance(createEntityManager()).findById(group.getId());
        
        assertEquals(group, newGroup);
    }
    
    @Test
    public void addGroupToGroupTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager());
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory, researchFileDAOFactory);
        
        Group parentGroup = new Group("Parent");
        Group childGroup = new Group("Child");
        
        assertEquals(parentGroup.getGroups().size(),0);
        
        groupService.addChildGroup(parentGroup, childGroup);
        
        assertEquals(parentGroup.getGroups().size(),1);
    }
    
    @Test
    public void moveGroupsTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());
        
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory, researchFileDAOFactory);
        
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
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());
        
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory, researchFileDAOFactory);
        
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
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());
        
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory, researchFileDAOFactory);
        
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
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());

        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(emf.createEntityManager());
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory, researchFileDAOFactory);

        final MetadataCategory metadataCategory = new MetadataCategory("metadataCategory", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
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
        final MetadataCategory metadataCategory2 = new MetadataCategory("metadatacategory number 2", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        final MetadataValue metadataValue3 = new MetadataValue("metadata value three");
        metadataCategory2.getValues().add(metadataValue3);
        metadataCategoryDAO.createMetadataCategory(metadataCategory2);

        groupService.associateMetadata(group, metadataCategory2, metadataValue3);
        assertEquals(2, group.getMetadataAssociations().size());
    }

    @Test
    public void testAssociateMetadataToFreetextCategory()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());

        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(emf.createEntityManager());
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory, researchFileDAOFactory);

        final MetadataCategory metadataCategory = new MetadataCategory("metadataCategory", MetadataCategoryType.FREETEXT, MetadataCategoryUse.optional);
        final MetadataValue metadataValue = new MetadataValue("metadataValue");
        metadataCategory.getValues().add(metadataValue);
        metadataCategoryDAO.createMetadataCategory(metadataCategory);
        final Group group = new Group("group name");
        groupService.associateMetadata(group, metadataCategory, metadataValue);

        List<MetadataAssociation> metadataAssociations = group.getMetadataAssociations();
        assertEquals(1, metadataAssociations.size());
        MetadataAssociation metadataAssociation = metadataAssociations.get(0);
        assertNotNull(metadataAssociation);
        assertEquals(metadataCategory, metadataAssociation.getMetadataCategory());
        assertEquals(1, metadataAssociation.getMetadataValues().size());
        assertEquals(metadataValue, metadataAssociation.getMetadataValues().get(0));

        final MetadataValue newMetadataValue = new MetadataValue("new metadataValue");
        metadataCategory.getValues().add(newMetadataValue);
        metadataCategoryDAO.updateMetadataCategory(metadataCategory);
        groupService.associateMetadata(group, metadataCategory, newMetadataValue);

        metadataAssociations = group.getMetadataAssociations();
        assertEquals(1, metadataAssociations.size());
        assertEquals(newMetadataValue, metadataAssociations.get(0).getMetadataValues().get(0));
    }

    @Test
    public void testDisassociateMetadata()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());

        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(emf.createEntityManager());
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory, researchFileDAOFactory);

        // Disassociate metadata that is not associated.
        final MetadataCategory metadataCategory1 = new MetadataCategory("metadataCategory", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        final MetadataValue metadataValue1 = new MetadataValue("metadataValue");
        metadataCategory1.getValues().add(metadataValue1);
        metadataCategoryDAO.createMetadataCategory(metadataCategory1);

        final MetadataCategory metadataCategory2 = new MetadataCategory("metadataCategory two", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        final MetadataValue metadataValue2 = new MetadataValue("metadataValue two");
        final MetadataValue metadataValue3 = new MetadataValue("metadataValue three");
        metadataCategory2.getValues().add(metadataValue2);
        metadataCategory2.getValues().add(metadataValue3);
        metadataCategoryDAO.createMetadataCategory(metadataCategory2);

        final Group group = groupService.createNewGroup("group name");
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

    @Test
    public void testDeleteGroup()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());

        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final GroupDAO groupDAO = new GroupDAO(emf.createEntityManager());
        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final MetadataAssociationDAO metadataAssociationDAO = new MetadataAssociationDAO(emf.createEntityManager());
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(emf.createEntityManager());
        final ResearchFileDAO researchFileDAO = new ResearchFileDAO(emf.createEntityManager());
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory, researchFileDAOFactory);

        final Group parentGroup = groupService.createNewGroup("parent");
        final Group childGroup = groupService.createNewGroup("child");
        groupService.addChildGroup(parentGroup, childGroup);
        assertTrue(parentGroup.getGroups().contains(childGroup));

        final ResearchFile rf = new ResearchFile(new File("someFile.txt"));
        researchFileDAO.createResearchFile(rf);
        childGroup.getResearchFiles().add(rf);
        groupDAO.updateGroup(childGroup);

        final MetadataCategory mc = new MetadataCategory("name", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        final MetadataValue mv = new MetadataValue("value");
        final MetadataAssociation ma = new MetadataAssociation(mc);
        mc.getValues().add(mv);
        ma.getMetadataValues().add(mv);
        metadataCategoryDAO.createMetadataCategory(mc);
        metadataAssociationDAO.createMetadataAssociation(ma);

        childGroup.getMetadataAssociations().add(ma);
        groupDAO.updateGroup(childGroup);

        groupService.deleteGroup(childGroup);

        final Group updatedGroup = groupDAO.findById(parentGroup.getId());

        assertTrue(updatedGroup.getGroups().isEmpty());
        assertFalse(updatedGroup.getResearchFiles().isEmpty());
    }

    @Test
    public void testRenameGroup()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());

        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final GroupDAO groupDAO = new GroupDAO(emf.createEntityManager());
        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        groupService = new GroupService(emf, groupDAOFactory, metadataAssocationDAOFactory, researchFileDAOFactory);

        final Group myGroup = groupService.createNewGroup("testRenameGroup");

        groupService.renameGroup(myGroup, "myNewGroupName");
        final Group groupOut = groupDAO.findById(myGroup.getId());
        assertEquals("myNewGroupName", groupOut.getName());
    }

    @Test
    public void testGetGroupsWithAssociatedMetadata()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager());

        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(emf.createEntityManager());
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        groupService = new GroupService(emf, groupDAOFactory, metadataAssociationDAOFactory, researchFileDAOFactory);

        final Group myGroup = groupService.createNewGroup("testGetGroupsWithAssociatedMetadata");

        final MetadataCategory mdc = new MetadataCategory("mdc-testGetGroupsWithAssociatedMetadata", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        final MetadataValue mdv = new MetadataValue("mdv-testGetGroupsWithAssociatedMetadata");
        mdc.getValues().add(mdv);
        metadataCategoryDAO.createMetadataCategory(mdc);

        groupService.associateMetadata(myGroup, mdc, mdv);

        final List<Group> out = groupService.getGroupsWithAssociatedMetadata(mdc, mdv);
        assertEquals(1, out.size());
        assertEquals(myGroup, out.get(0));
    }

    @Test
    public void testFindGroupById()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager());

        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        groupService = new GroupService(emf, groupDAOFactory, metadataAssociationDAOFactory, researchFileDAOFactory);

        final Group myGroup = groupService.createNewGroup("testGetGroupsWithAssociatedMetadata");
        assertNotNull(myGroup.getId());

        assertNull(groupService.findGroupByID(myGroup.getId() + 1000l));
        assertEquals(myGroup, groupService.findGroupByID(myGroup.getId()));
    }

    @Test
    public void testDeleteGroupCheck1()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager());

        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        groupService = new GroupService(emf, groupDAOFactory, metadataAssociationDAOFactory, researchFileDAOFactory);

        final Group parentOfGroupToDelete = new Group("groupToDelete");
        final Group groupToDelete = new Group("groupToDelete");

        parentOfGroupToDelete.getGroups().add(groupToDelete);
        groupToDelete.setParentGroup(parentOfGroupToDelete);

        final Group siblingOfGroupToDelete = new Group("sibling");

        parentOfGroupToDelete.getGroups().add(siblingOfGroupToDelete);
        siblingOfGroupToDelete.setParentGroup(parentOfGroupToDelete);

        final Group childOfGroupToDelete = new Group("sibling");

        groupToDelete.getGroups().add(childOfGroupToDelete);
        childOfGroupToDelete.setParentGroup(groupToDelete);

        final String out = groupService.deleteGroupCheck(groupToDelete);
        assertNotNull(out);
    }

    @Test
    public void testDeleteGroupCheck2()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager());

        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        groupService = new GroupService(emf, groupDAOFactory, metadataAssociationDAOFactory, researchFileDAOFactory);

        final Group parentOfGroupToDelete = new Group("groupToDelete");
        final Group groupToDelete = new Group("groupToDelete");

        parentOfGroupToDelete.getGroups().add(groupToDelete);
        groupToDelete.setParentGroup(parentOfGroupToDelete);

        final File file1 = new File("some file");
        final ResearchFile rf1 = new ResearchFile(file1);
        final ResearchFile rf2 = new ResearchFile(file1);

        rf1.setParentGroup(parentOfGroupToDelete);
        parentOfGroupToDelete.getResearchFiles().add(rf1);

        rf2.setParentGroup(groupToDelete);
        groupToDelete.getResearchFiles().add(rf2);

        final String out = groupService.deleteGroupCheck(groupToDelete);
        assertNotNull(out);
    }

    @Test
    public void testDeleteGroupCheck3()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager());

        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        groupService = new GroupService(emf, groupDAOFactory, metadataAssociationDAOFactory, researchFileDAOFactory);

        final Group parentOfGroupToDelete = new Group("groupToDelete");
        final Group groupToDelete = new Group("groupToDelete");

        parentOfGroupToDelete.getGroups().add(groupToDelete);
        groupToDelete.setParentGroup(parentOfGroupToDelete);

        final File file1 = new File("some file");
        final File file2 = new File("some 2nd file");
        final ResearchFile rf1 = new ResearchFile(file1);
        final ResearchFile rf2 = new ResearchFile(file2);

        rf1.setParentGroup(parentOfGroupToDelete);
        parentOfGroupToDelete.getResearchFiles().add(rf1);

        rf2.setParentGroup(groupToDelete);
        groupToDelete.getResearchFiles().add(rf2);

        final String out = groupService.deleteGroupCheck(groupToDelete);
        assertNull(out);
    }

    @Test
    public void testSortResearchFilesInGroup()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager());

        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        groupService = new GroupService(emf, groupDAOFactory, metadataAssociationDAOFactory, researchFileDAOFactory);

        final Group parent = groupService.createNewGroup("parent");
        final Group child = groupService.createNewGroup("child");
        groupService.addChildGroup(parent, child);

        assertEquals(ResearchFileSortField.NAME, parent.getResearchFileSortField());
        assertEquals(SortFieldDirection.ASC, parent.getResearchFileSortDirection());
        assertEquals(ResearchFileSortField.NAME, child.getResearchFileSortField());
        assertEquals(SortFieldDirection.ASC, child.getResearchFileSortDirection());

        final ResearchFileSortField sortField = ResearchFileSortField.SIZE;
        final SortFieldDirection sortDirection = SortFieldDirection.DESC;
        groupService.sortResearchFilesInGroup(parent, sortField, sortDirection);

        final Group parentOut = groupService.findGroupByID(parent.getId());
        final Group childOut = groupService.findGroupByID(child.getId());

        assertEquals(sortField, parentOut.getResearchFileSortField());
        assertEquals(sortDirection, parentOut.getResearchFileSortDirection());
        assertEquals(sortField, childOut.getResearchFileSortField());
        assertEquals(sortDirection, childOut.getResearchFileSortDirection());
    }
}
