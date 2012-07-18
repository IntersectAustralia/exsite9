package au.org.intersect.exsite9.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataAssociationDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.dao.factory.SubmissionPackageDAOFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

public final class ProjectServiceUnitTest extends DAOTest
{
    private ProjectService projectService;

    @Test
    public void createNewProjectTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);

        stub(emf.createEntityManager()).toReturn(createEntityManager());

        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();

        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, researchFileDAOFactory,
                metadataAssociationDAOFactory, submissionPackageDAOFactory);

        Project project = projectService.createProject(new ProjectFieldsDTO("name", "owner", "description", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", ""));

        Project newProject = projectDAOFactory.createInstance(createEntityManager()).findById(project.getId());

        assertEquals(project, newProject);
    }

    @Test
    public void mapFolderToProjectTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);

        stub(emf.createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager());

        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();

        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, researchFileDAOFactory,
                metadataAssociationDAOFactory, submissionPackageDAOFactory);

        Folder folder = new Folder(new File("/tmp"));

        Project project = projectService.createProject(new ProjectFieldsDTO("name", "owner", "description", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", ""));

        projectService.mapFolderToProject(project, folder);

        Project newProject = projectDAOFactory.createInstance(createEntityManager()).findById(project.getId());

        assertEquals(project, newProject);
    }

    @Test
    public void removeFoldersFromprojectTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);

        stub(emf.createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager())
                .toReturn(createEntityManager()).toReturn(createEntityManager());

        Folder folder1 = new Folder(new File("/tmp1"));
        Folder folder2 = new Folder(new File("/tmp2"));

        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();

        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, researchFileDAOFactory,
                metadataAssociationDAOFactory, submissionPackageDAOFactory);

        Project project = projectService.createProject(new ProjectFieldsDTO("name", "owner", "description", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", ""));

        projectService.mapFolderToProject(project, folder1);
        projectService.mapFolderToProject(project, folder2);

        List<Folder> deletedFolderList = new ArrayList<Folder>(0);

        deletedFolderList.add(folder2);

        projectService.removeFoldersFromProject(project, deletedFolderList);

        Project newProject = projectDAOFactory.createInstance(createEntityManager()).findById(project.getId());

        assertEquals(1, newProject.getFolders().size());
        assertEquals(folder1.getFolder().getPath(), newProject.getFolders().get(0).getFolder().getPath());
    }

    @Test
    public void testGetAllProjects()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager())
                .toReturn(createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager());

        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        final SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, researchFileDAOFactory,
                metadataAssociationDAOFactory, submissionPackageDAOFactory);

        assertEquals(0, projectService.getAllProjects().size());

        final ProjectFieldsDTO p1dto = new ProjectFieldsDTO("p1", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "");
        final Project p1 = projectService.createProject(p1dto);
        assertNotNull(p1.getId());

        List<Project> out = projectService.getAllProjects();
        assertEquals(1, out.size());
        assertEquals(p1, out.get(0));

        final ProjectFieldsDTO p2dto = new ProjectFieldsDTO("p2", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "");
        final Project p2 = projectService.createProject(p2dto);
        assertNotNull(p2.getId());

        out = projectService.getAllProjects();
        assertEquals(2, out.size());
        assertTrue(out.contains(p1));
        assertTrue(out.contains(p2));
    }

    @Test
    public void testEditProject()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager())
                .toReturn(createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager());

        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        final SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, researchFileDAOFactory,
                metadataAssociationDAOFactory, submissionPackageDAOFactory);

        assertEquals(0, projectService.getAllProjects().size());

        final ProjectFieldsDTO p1dto = new ProjectFieldsDTO("p1", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "");
        final Project p1 = projectService.createProject(p1dto);
        assertNotNull(p1.getId());

        final String name = "newName";
        final String owner = "newOwner";
        final String description = "newDesc";
        final String collectionType = "collectionType";
        final String rightsStatement = "rights";
        final String accessRights = "accessRights";
        final String license = "license";
        final String identifier = "identifier";
        final String subject = "subject";
        final String electronicLocation = "electronicLocation";
        final String physicalLocation = "physicalLocation";
        final String placeOrRegionName = "placeOrRegionName";
        final String latitudeLongitude = "latitudeLogitude";
        final String datesOfCapture = "datesOfCapture";
        final String citationInfo = "citationInfo";
        final String relatedParty = "relatedParty";
        final String relatedActivity = "relatedActivity";
        final String relatedInfo = "relatedInfo";
        final ProjectFieldsDTO updatedFields = new ProjectFieldsDTO(name, owner, description, collectionType,
                rightsStatement, accessRights, license, identifier, subject, electronicLocation, physicalLocation,
                placeOrRegionName, latitudeLongitude, datesOfCapture, citationInfo, relatedParty, relatedActivity,
                relatedInfo);

        projectService.editProject(updatedFields, p1.getId());

        final Project editedProject = projectService.findProjectById(p1.getId());
        assertEquals(p1, editedProject);

        assertEquals(name, editedProject.getName());
        assertEquals(owner, editedProject.getOwner());
        assertEquals(description, editedProject.getDescription());
        assertEquals(collectionType, editedProject.getCollectionType());
        assertEquals(rightsStatement, editedProject.getRightsStatement());
        assertEquals(accessRights, editedProject.getAccessRights());
        assertEquals(license, editedProject.getLicence());
        assertEquals(identifier, editedProject.getIdentifier());
        assertEquals(subject, editedProject.getSubject());
        assertEquals(electronicLocation, editedProject.getElectronicLocation());
        assertEquals(physicalLocation, editedProject.getPhysicalLocation());
        assertEquals(placeOrRegionName, editedProject.getPlaceOrRegionName());
        assertEquals(latitudeLongitude, editedProject.getLatitudeLongitude());
        assertEquals(datesOfCapture, editedProject.getDatesOfCapture());
        assertEquals(citationInfo, editedProject.getCitationInformation());
        assertEquals(relatedParty, editedProject.getRelatedParty());
        assertEquals(relatedActivity, editedProject.getRelatedActivity());
        assertEquals(relatedInfo, editedProject.getRelatedInformation());
    }

    @Test
    public void updateFolderPathTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);

        stub(emf.createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager())
                .toReturn(createEntityManager()).toReturn(createEntityManager());

        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();

        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, researchFileDAOFactory,
                metadataAssociationDAOFactory, submissionPackageDAOFactory);

        Folder folder = new Folder(new File("/tmp"));

        Project project = projectService.createProject(new ProjectFieldsDTO("name", "owner", "description", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", ""));

        projectService.mapFolderToProject(project, folder);

        File updatedFileObject = new File("/new/location");

        projectService.updateFolderPath(folder.getId(), updatedFileObject);

        Folder folderFromDB = folderDAOFactory.createInstance(emf.createEntityManager()).findById(folder.getId());

        assertTrue(folderFromDB.getFolder().getPath().equals(updatedFileObject.getPath()));

    }
}
