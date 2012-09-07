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
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.SchemaDAO;
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataAssociationDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.dao.factory.SubmissionPackageDAOFactory;
import au.org.intersect.exsite9.domain.FieldOfResearch;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.Schema;
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
        SchemaDAO schemaDAO = new SchemaDAO(emf.createEntityManager());

        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, researchFileDAOFactory,
                metadataAssociationDAOFactory, submissionPackageDAOFactory);

        final Schema schema = new Schema("", "", "", Boolean.TRUE);
        schemaDAO.createSchema(schema);
        Project project = projectService.createProject(new ProjectFieldsDTO("name", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation"), schema);

        Project newProject = projectDAOFactory.createInstance(createEntityManager()).findById(project.getId());

        assertEquals(project, newProject);
    }

    @Test
    public void mapFolderToProjectTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);

        stub(emf.createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager());

        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        SchemaDAO schemaDAO = new SchemaDAO(emf.createEntityManager());

        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, researchFileDAOFactory,
                metadataAssociationDAOFactory, submissionPackageDAOFactory);

        Folder folder = new Folder(new File("/tmp"));

        final Schema schema = new Schema("", "", "", Boolean.TRUE);
        schemaDAO.createSchema(schema);

        Project project = projectService.createProject(new ProjectFieldsDTO("name", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation"), schema);

        projectService.mapFolderToProject(project, folder);

        Project newProject = projectDAOFactory.createInstance(createEntityManager()).findById(project.getId());

        assertEquals(project, newProject);
    }

    @Test
    public void removeFoldersFromprojectTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);

        stub(emf.createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager())
                .toReturn(createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager());

        Folder folder1 = new Folder(new File("/tmp1"));
        Folder folder2 = new Folder(new File("/tmp2"));

        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        SchemaDAO schemaDAO = new SchemaDAO(emf.createEntityManager());

        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, researchFileDAOFactory,
                metadataAssociationDAOFactory, submissionPackageDAOFactory);

        final Schema schema = new Schema("", "", "", Boolean.TRUE);
        schemaDAO.createSchema(schema);

        Project project = projectService.createProject(new ProjectFieldsDTO("name", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation"), schema);

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
                .toReturn(createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager());

        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        final SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        final SchemaDAO schemaDAO = new SchemaDAO(emf.createEntityManager());
        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, researchFileDAOFactory,
                metadataAssociationDAOFactory, submissionPackageDAOFactory);

        assertEquals(0, projectService.getAllProjects().size());

        final Schema schema = new Schema("", "", "", Boolean.TRUE);
        schemaDAO.createSchema(schema);

        final ProjectFieldsDTO p1dto = new ProjectFieldsDTO("p1", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation");
        final Project p1 = projectService.createProject(p1dto, schema);
        assertNotNull(p1.getId());

        List<Project> out = projectService.getAllProjects();
        assertEquals(1, out.size());
        assertEquals(p1, out.get(0));

        final ProjectFieldsDTO p2dto = new ProjectFieldsDTO("p2", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation");
        final Project p2 = projectService.createProject(p2dto, schema);
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
                .toReturn(createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager());

        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        final SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        final SchemaDAO schemaDAO = new SchemaDAO(emf.createEntityManager());
        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, researchFileDAOFactory,
                metadataAssociationDAOFactory, submissionPackageDAOFactory);

        assertEquals(0, projectService.getAllProjects().size());

        final Schema schema = new Schema("", "", "", Boolean.TRUE);
        schemaDAO.createSchema(schema);

        final ProjectFieldsDTO p1dto = new ProjectFieldsDTO("name", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation");
        
        final Project p1 = projectService.createProject(p1dto, schema);
        assertNotNull(p1.getId());

        final String name = "newName";
        final String owner = "newOwner";
        final String institution = "newInstitution";
        final String email = "new email";
        final String description = "newDesc";
        final String collectionType = "newcollectionType";
        final String rightsStatement = "newrights";
        final String accessRights = "newaccessRights";
        final String license = "newlicense";
        final String identifier = "newidentifier";
        final String subject = "newsubject";
        final String electronicLocation = "newelectronicLocation";
        final String physicalLocation = "newphysicalLocation";
        final String placeOrRegionName = "newplaceOrRegionName";
        final String geographicalCoverage = "newgeoCoverage";
        final String datesOfCapture = "newdatesOfCapture";
        final String citationInfo = "newcitationInfo";
        final String countries = "newCountries";
        final String languages = "newlanguages";
        final FieldOfResearch fieldOfResearch = new FieldOfResearch("new Code", "new FOR");
        final String fundingBody = "newFundingBody";
        final String grantID = "newGrantID";
        final String relatedParty = "newrelatedParty";
        final String relatedGrant = "newrelatedActivity";
        final String relatedInfo = "newrelatedInfo";
        final ProjectFieldsDTO updatedFields = new ProjectFieldsDTO(name, owner, institution, email, description, collectionType,
                rightsStatement, accessRights, license, identifier, subject, electronicLocation, physicalLocation,
                placeOrRegionName, geographicalCoverage, datesOfCapture, citationInfo, countries, languages, fieldOfResearch,
                fundingBody, grantID, relatedParty, relatedGrant, relatedInfo);

        projectService.editProject(updatedFields, p1.getId());

        final Project editedProject = projectService.findProjectById(p1.getId());
        assertEquals(p1, editedProject);

        assertEquals(name, editedProject.getName());
        assertEquals(owner, editedProject.getOwner());
        assertEquals(institution, editedProject.getInstitution());
        assertEquals(email, editedProject.getEmail());
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
        assertEquals(geographicalCoverage, editedProject.getGeographicalCoverage());
        assertEquals(datesOfCapture, editedProject.getDatesOfCapture());
        assertEquals(citationInfo, editedProject.getCitationInformation());
        assertEquals(countries, editedProject.getCountries());
        assertEquals(languages, editedProject.getLanguages());
        assertEquals(fieldOfResearch, editedProject.getFieldOfResearch());
        assertEquals(grantID, editedProject.getGrantID());
        assertEquals(relatedParty, editedProject.getRelatedParty());
        assertEquals(relatedGrant, editedProject.getRelatedGrant());
        assertEquals(relatedInfo, editedProject.getRelatedInformation());
    }

    @Test
    public void updateFolderPathTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);

        stub(emf.createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager())
                .toReturn(createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager());

        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        SchemaDAO schemaDAO = new SchemaDAO(emf.createEntityManager());

        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, researchFileDAOFactory,
                metadataAssociationDAOFactory, submissionPackageDAOFactory);

        ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(emf.createEntityManager());

        Folder folder = new Folder(new File("/tmp"));
        ResearchFile file1 = new ResearchFile(new File("/tmp/file1.txt"));
        ResearchFile file2 = new ResearchFile(new File("/tmp/file2.txt"));
        researchFileDAO.createResearchFile(file1);
        researchFileDAO.createResearchFile(file2);

        folder.getFiles().add(file1);
        folder.getFiles().add(file2);

        final Schema schema = new Schema("", "", "", Boolean.TRUE);
        schemaDAO.createSchema(schema);

        Project project = projectService.createProject(new ProjectFieldsDTO("name", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation"), schema);

        projectService.mapFolderToProject(project, folder);

        File updatedFileObject = new File("/new/location");

        projectService.updateFolderPath(folder.getId(), updatedFileObject);

        Folder folderFromDB = folderDAOFactory.createInstance(emf.createEntityManager()).findById(folder.getId());

        assertTrue(folderFromDB.getFolder().getPath().equals(updatedFileObject.getPath()));
        
        for (ResearchFile file : folderFromDB.getFiles())
        {
            assertEquals(updatedFileObject.getAbsolutePath() + File.separator + file.getFile().getName(), file.getFile().getAbsolutePath());
        }

    }

    @Test
    public void testEditProjectSchema()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);

        stub(emf.createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager())
                .toReturn(createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager()).toReturn(createEntityManager());

        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        SchemaDAO schemaDAO = new SchemaDAO(emf.createEntityManager());

        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, researchFileDAOFactory,
                metadataAssociationDAOFactory, submissionPackageDAOFactory);

        final Schema schema1 = new Schema("schema1", "", "", Boolean.TRUE);
        schemaDAO.createSchema(schema1);
        assertNotNull(schema1.getId());
        final Schema schema2 = new Schema("schema2", "", "", Boolean.TRUE);
        schemaDAO.createSchema(schema2);
        assertNotNull(schema2.getId());

        final Project project = projectService.createProject(new ProjectFieldsDTO("name", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation"), schema1);
        assertNotNull(project.getId());

        projectService.editProject(schema2, project.getId());

        final Project updatedProject = projectService.findProjectById(project.getId());
        assertEquals(schema2, updatedProject.getSchema());
    }
}
