/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.FolderDAO;
import au.org.intersect.exsite9.dao.MetadataCategoryDAO;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataAssociationDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

/**
 * Tests {@link ResearchFileService}
 */
public final class ResearchFileServiceUnitTest extends DAOTest
{

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

        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(emf.createEntityManager());
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final ResearchFileService toTest = new ResearchFileService(emf, projectDAOFactory, researchFileDAOFactory, metadataAssocationDAOFactory, folderDAOFactory, groupDAOFactory);

        final MetadataCategory metadataCategory = new MetadataCategory("metadataCategory", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        final MetadataValue metadataValue = new MetadataValue("metadataValue");
        metadataCategory.getValues().add(metadataValue);
        metadataCategoryDAO.createMetadataCategory(metadataCategory);

        final File file = new File("some file");
        final ResearchFile rf = new ResearchFile(file);
        toTest.associateMetadata(rf, metadataCategory, metadataValue);

        final List<MetadataAssociation> metadataAssociations = rf.getMetadataAssociations();
        assertEquals(1, metadataAssociations.size());
        MetadataAssociation metadataAssociation = metadataAssociations.get(0);
        assertNotNull(metadataAssociation);
        assertEquals(metadataCategory, metadataAssociation.getMetadataCategory());
        assertEquals(1, metadataAssociation.getMetadataValues().size());
        assertEquals(metadataValue, metadataAssociation.getMetadataValues().get(0));

        // Doing it again does nothing.
        toTest.associateMetadata(rf, metadataCategory, metadataValue);
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

        toTest.associateMetadata(rf, metadataCategory, metadataValue2);
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

        toTest.associateMetadata(rf, metadataCategory2, metadataValue3);
        assertEquals(2, rf.getMetadataAssociations().size());
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

        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(emf.createEntityManager());
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final ResearchFileService toTest = new ResearchFileService(emf, projectDAOFactory, researchFileDAOFactory, metadataAssocationDAOFactory, folderDAOFactory, groupDAOFactory);

        final MetadataCategory metadataCategory = new MetadataCategory("metadataCategory", MetadataCategoryType.FREETEXT, MetadataCategoryUse.optional);
        final MetadataValue metadataValue = new MetadataValue("metadataValue");
        metadataCategory.getValues().add(metadataValue);
        metadataCategoryDAO.createMetadataCategory(metadataCategory);

        final File file = new File("some file");
        final ResearchFile rf = new ResearchFile(file);
        toTest.associateMetadata(rf, metadataCategory, metadataValue);

        List<MetadataAssociation> metadataAssociations = rf.getMetadataAssociations();
        assertEquals(1, metadataAssociations.size());
        MetadataAssociation metadataAssociation = metadataAssociations.get(0);
        assertNotNull(metadataAssociation);
        assertEquals(metadataCategory, metadataAssociation.getMetadataCategory());
        assertEquals(1, metadataAssociation.getMetadataValues().size());
        assertEquals(metadataValue, metadataAssociation.getMetadataValues().get(0));

        final MetadataValue newMetadataValue = new MetadataValue("new metadataValue");
        metadataCategory.getValues().add(newMetadataValue);
        metadataCategoryDAO.updateMetadataCategory(metadataCategory);
        toTest.associateMetadata(rf, metadataCategory, newMetadataValue);

        metadataAssociations = rf.getMetadataAssociations();
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

        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(createEntityManager());
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        final ResearchFileDAO researchFileDAO = new ResearchFileDAO(createEntityManager());
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final ResearchFileService toTest = new ResearchFileService(emf, projectDAOFactory, researchFileDAOFactory, metadataAssocationDAOFactory, folderDAOFactory, groupDAOFactory);

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

        final File file = new File("some file");
        final ResearchFile rf = new ResearchFile(file);
        researchFileDAO.createResearchFile(rf);
        assertNotNull(rf.getId());

        toTest.disassociateMetadata(rf, metadataCategory1, metadataValue1);
        assertTrue(rf.getMetadataAssociations().isEmpty());
        toTest.associateMetadata(rf, metadataCategory1, metadataValue1);
        toTest.associateMetadata(rf, metadataCategory2, metadataValue2);
        toTest.associateMetadata(rf, metadataCategory2, metadataValue3);

        List<MetadataAssociation> metadataAssociations = rf.getMetadataAssociations();
        assertEquals(2, metadataAssociations.size());

        toTest.disassociateMetadata(rf, metadataCategory2, metadataValue2);
        metadataAssociations = rf.getMetadataAssociations();
        assertEquals(2, metadataAssociations.size());

        toTest.disassociateMetadata(rf, metadataCategory1, metadataValue1);
        metadataAssociations = rf.getMetadataAssociations();
        assertEquals(1, metadataAssociations.size());

        toTest.disassociateMetadata(rf, metadataCategory2, metadataValue3);
        metadataAssociations = rf.getMetadataAssociations();
        assertEquals(0, metadataAssociations.size());
    }

    @Test
    public void testGetResearchFileWithAssociatedMetadata()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager());

        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(createEntityManager());
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        final ResearchFileDAO researchFileDAO = new ResearchFileDAO(createEntityManager());
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final ResearchFileService toTest = new ResearchFileService(emf, projectDAOFactory, researchFileDAOFactory, metadataAssocationDAOFactory, folderDAOFactory, groupDAOFactory);

        final ResearchFile rf = new ResearchFile(new File("file on disk"));
        researchFileDAO.createResearchFile(rf);
        assertNotNull(rf.getId());

        final MetadataCategory mdc = new MetadataCategory("mdc-testGetGroupsWithAssociatedMetadata", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        final MetadataValue mdv = new MetadataValue("mdv-testGetGroupsWithAssociatedMetadata");
        mdc.getValues().add(mdv);
        metadataCategoryDAO.createMetadataCategory(mdc);

        toTest.associateMetadata(rf, mdc, mdv);

        final List<ResearchFile> out = toTest.getResearchFilesWithAssociatedMetadata(mdc, mdv);
        assertEquals(1, out.size());
        assertEquals(rf, out.get(0));
    }

    @Test
    public void testFindResearchFileByID()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager());

        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        final ResearchFileDAO researchFileDAO = new ResearchFileDAO(createEntityManager());
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final ResearchFileService toTest = new ResearchFileService(emf, projectDAOFactory, researchFileDAOFactory, metadataAssocationDAOFactory, folderDAOFactory, groupDAOFactory);

        final ResearchFile rf = new ResearchFile(new File("file on disk"));
        researchFileDAO.createResearchFile(rf);
        assertNotNull(rf.getId());

        final ResearchFile out = toTest.findResearchFileByID(rf.getId());
        assertEquals(rf, out);
    }

    @Test
    public void testDisassociateMultipleMetadataValues()
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

        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final MetadataCategoryDAO metadataCategoryDAO = new MetadataCategoryDAO(createEntityManager());
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        final ResearchFileDAO researchFileDAO = new ResearchFileDAO(createEntityManager());
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final ResearchFileService toTest = new ResearchFileService(emf, projectDAOFactory, researchFileDAOFactory, metadataAssocationDAOFactory, folderDAOFactory, groupDAOFactory);

        final MetadataCategory metadataCategory2 = new MetadataCategory("metadataCategory two", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        final MetadataValue metadataValue2 = new MetadataValue("metadataValue two");
        final MetadataValue metadataValue3 = new MetadataValue("metadataValue three");
        metadataCategory2.getValues().add(metadataValue2);
        metadataCategory2.getValues().add(metadataValue3);
        metadataCategoryDAO.createMetadataCategory(metadataCategory2);

        final File file = new File("some file");
        final ResearchFile rf = new ResearchFile(file);
        researchFileDAO.createResearchFile(rf);
        assertNotNull(rf.getId());

        final MetadataCategory metadataCategory1 = new MetadataCategory("metadataCategory", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        final MetadataValue metadataValue1 = new MetadataValue("metadataValue");
        metadataCategory1.getValues().add(metadataValue1);
        metadataCategoryDAO.createMetadataCategory(metadataCategory1);

        assertTrue(rf.getMetadataAssociations().isEmpty());
        toTest.associateMetadata(rf, metadataCategory1, metadataValue1);
        toTest.associateMetadata(rf, metadataCategory2, metadataValue2);
        toTest.associateMetadata(rf, metadataCategory2, metadataValue3);

        List<MetadataAssociation> metadataAssociations = rf.getMetadataAssociations();
        assertEquals(2, metadataAssociations.size());

        toTest.disassociateMultipleMetadataValues(rf, metadataCategory2, Arrays.asList(metadataValue2, metadataValue3));
        metadataAssociations = rf.getMetadataAssociations();
        assertEquals(1, metadataAssociations.size());
    }

    @Test
    public void testUpdateResearchFile()
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

        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        final ResearchFileDAO researchFileDAO = new ResearchFileDAO(createEntityManager());
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final ResearchFileService toTest = new ResearchFileService(emf, projectDAOFactory, researchFileDAOFactory, metadataAssocationDAOFactory, folderDAOFactory, groupDAOFactory);
        
        final File file = new File("some file");
        final ResearchFile rf = new ResearchFile(file);
        researchFileDAO.createResearchFile(rf);
        assertNotNull(rf.getId());

        final File newFile = new File("some new file");
        rf.setFile(newFile);
        toTest.updateResearchFile(rf);

        assertEquals(newFile, toTest.findResearchFileByID(rf.getId()).getFile());
    }

    @Test
    public void testConsolidateSubFolderIntoParentFolder()
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

        final MetadataAssociationDAOFactory metadataAssocationDAOFactory = new MetadataAssociationDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        final ResearchFileDAO researchFileDAO = new ResearchFileDAO(createEntityManager());
        final FolderDAO folderDAO = new FolderDAO(createEntityManager());
        final ProjectDAO projectDAO = new ProjectDAO(createEntityManager());
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final ResearchFileService toTest = new ResearchFileService(emf, projectDAOFactory, researchFileDAOFactory, metadataAssocationDAOFactory, folderDAOFactory, groupDAOFactory);

        final File file1 = new File("parent file");
        final File file2 = new File("sub file");
        final ResearchFile rf1 = new ResearchFile(file1);
        final ResearchFile rf2 = new ResearchFile(file2);
        researchFileDAO.createResearchFile(rf1);
        researchFileDAO.createResearchFile(rf2);
        assertNotNull(rf1.getId());
        assertNotNull(rf2.getId());

        final File parent = new File("parent");
        final Folder parentFolder = new Folder(parent);
        parentFolder.getFiles().add(rf1);
        folderDAO.createFolder(parentFolder);

        final File sub = new File("sub");
        final Folder subFolder = new Folder(sub);
        subFolder.getFiles().add(rf2);
        folderDAO.createFolder(subFolder);

        final Project project = new Project(new ProjectFieldsDTO("name", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation"));
        project.getFolders().add(parentFolder);
        project.getFolders().add(subFolder);
        projectDAO.createProject(project);
        assertNotNull(project.getId());

        toTest.consolidateSubFolderIntoParentFolder(project, parentFolder, subFolder);
        assertTrue(parentFolder.getFiles().contains(rf2));
        assertTrue(subFolder.getFiles().isEmpty());
        assertFalse(project.getFolders().contains(subFolder));
    }
}
