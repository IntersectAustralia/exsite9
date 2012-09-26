/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

/**
 * Tests {@link ProjectDAO}
 */
public class ProjectDAOUnitTest extends DAOTest
{
    private static ProjectDAOFactory projectDAOFactory;
    
    @BeforeClass
    public static void setupOnce()
    {
        projectDAOFactory = new ProjectDAOFactory();
    }
    
    @Test
    public void createNewProjectTest()
    {
        EntityManager em = createEntityManager();
        ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
        
        Project project = new Project();
        project.setName("Project One");
        project.setDescription("This is project one.");
        project.setOwner("Owner One");
        
        Group rootNode = new Group(project.getName());
        Group newFilesNode = new Group("New Files");
        
        project.setRootNode(rootNode);
        project.setNewFilesNode(newFilesNode);
        
        projectDAO.createProject(project);
        
        Project persistedProject = projectDAO.findById(project.getId());
        
        assertEquals(project, persistedProject);
        
        Project project2 = new Project();
        project2.setName("Project Two");
        project2.setDescription("This is project two.");
        project2.setOwner("Owner Two");
        
        Group rootNode2 = new Group(project2.getName());
        Group newFilesNode2 = new Group("New Files");
        
        project2.setRootNode(rootNode2);
        project2.setNewFilesNode(newFilesNode2);
        
        projectDAO.createProject(project2);
        
        List<Project> projectList = projectDAO.findAllProjects();
        
        assertTrue(projectList.size() == 2);
        assertTrue(projectList.contains(project));
        assertTrue(projectList.contains(project2));
        
        em.close();
    }

    @Test
    public void testUpdateProject()
    {
        final Project project = new Project(new ProjectFieldsDTO("name", "owner", "institution", "email", "desc",
            "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
            "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
            "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
            "relatedInformation"));
        assertNull(project.getId());

        final EntityManager em = createEntityManager();
        final ProjectDAO projectDAO = projectDAOFactory.createInstance(em);

        projectDAO.createProject(project);
        assertNotNull(project.getId());

        project.setDescription("new description");
        projectDAO.updateProject(project);

        assertEquals("new description", projectDAO.findById(project.getId()).getDescription());

        // test it in a transaction
        em.getTransaction().begin();
        project.setDescription("a newer description");
        projectDAO.updateProject(project);
        em.getTransaction().commit();

        assertEquals("a newer description", projectDAO.findById(project.getId()).getDescription());
    }

    @Test
    public void testFindProjectWithSubmissionPackage()
    {
        final Project project = new Project(new ProjectFieldsDTO("name", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation"));
        assertNull(project.getId());

        final EntityManager em = createEntityManager();
        final ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
        final SubmissionPackageDAO submissionPackageDAO = new SubmissionPackageDAO(em);

        final SubmissionPackage sp1 = new SubmissionPackage();
        submissionPackageDAO.createSubmissionPackage(sp1);
        project.getSubmissionPackages().add(sp1);
        projectDAO.createProject(project);

        assertNotNull(project.getId());

        assertEquals(project, projectDAO.findProjectWithSubmissionPackage(sp1));
    }
}