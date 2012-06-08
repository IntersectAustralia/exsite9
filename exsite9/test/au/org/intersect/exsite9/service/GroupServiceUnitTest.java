package au.org.intersect.exsite9.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.database.ExSite9EntityManagerFactory;
import au.org.intersect.exsite9.domain.Group;
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
        
        groupService = new GroupService(emf, groupDAOFactory);
        
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
        
        groupService = new GroupService(emf, groupDAOFactory);
        
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
        
        groupService = new GroupService(emf, groupDAOFactory);
        
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
    
}