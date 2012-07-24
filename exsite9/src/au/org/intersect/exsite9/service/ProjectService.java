/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;

import au.org.intersect.exsite9.dao.FolderDAO;
import au.org.intersect.exsite9.dao.MetadataAssociationDAO;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.SubmissionPackageDAO;
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataAssociationDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.dao.factory.SubmissionPackageDAOFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;
import au.org.intersect.exsite9.xml.MetadataSchemaXMLBuilder;

public class ProjectService implements IProjectService
{
    private final Logger LOG = Logger.getLogger(ProjectService.class);

    private final EntityManagerFactory entityManagerFactory;
    private final ProjectDAOFactory projectDAOFactory;
    private final FolderDAOFactory folderDAOFactory;
    private final ResearchFileDAOFactory researchFileDAOFactory;
    private final MetadataAssociationDAOFactory metaAssociationDAOFactory;
    private final SubmissionPackageDAOFactory submissionPackageDAOFactory;

    public ProjectService(final EntityManagerFactory entityManagerFactory, final ProjectDAOFactory projectDAOFactory,
            final FolderDAOFactory folderDAOFactory, final ResearchFileDAOFactory researchFileDAOFactory,
            final MetadataAssociationDAOFactory metadataAssociationDAOFactory,
            final SubmissionPackageDAOFactory submissionPackageDAOFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.projectDAOFactory = projectDAOFactory;
        this.folderDAOFactory = folderDAOFactory;
        this.researchFileDAOFactory = researchFileDAOFactory;
        this.metaAssociationDAOFactory = metadataAssociationDAOFactory;
        this.submissionPackageDAOFactory = submissionPackageDAOFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Project createProject(final ProjectFieldsDTO projectFields, final Schema schema)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final Project project = new Project(projectFields);
            final ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
            project.setSchema(schema);
            projectDAO.createProject(project);
            return project;
        }
        finally
        {
            em.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mapFolderToProject(final Project project, final Folder folder)
    {
        EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
            FolderDAO folderDAO = folderDAOFactory.createInstance(em);
            folderDAO.createFolder(folder);
            project.getFolders().add(folder);
            projectDAO.updateProject(project);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public List<Project> getAllProjects()
    {
        EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
            return projectDAO.findAllProjects();
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public Project editProject(final ProjectFieldsDTO projectFields, final Long id)
    {
        EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
            Project project = projectDAO.findById(id);

            project.setName(projectFields.getName());
            project.setDescription(projectFields.getDescription());
            project.setOwner(projectFields.getOwner());
            project.setCollectionType(projectFields.getCollectionType());
            project.setRightsStatement(projectFields.getRightsStatement());
            project.setAccessRights(projectFields.getAccessRights());
            project.setLicence(projectFields.getLicence());
            project.setIdentifier(projectFields.getIdentifier());
            project.setSubject(projectFields.getSubject());
            project.setElectronicLocation(projectFields.getElectronicLocation());
            project.setPhysicalLocation(projectFields.getPhysicalLocation());
            project.setPlaceOrRegionName(projectFields.getPlaceOrRegionName());
            project.setLatitudeLongitude(projectFields.getLatitudeLongitude());
            project.setDatesOfCapture(projectFields.getDatesOfCapture());
            project.setCitationInformation(projectFields.getCitationInformation());
            project.setRelatedParty(projectFields.getRelatedParty());
            project.setRelatedActivity(projectFields.getRelatedActivity());
            project.setRelatedInformation(projectFields.getRelatedInformation());

            projectDAO.updateProject(project);
            return project;
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public Project findProjectById(Long id)
    {
        final EntityManager em = this.entityManagerFactory.createEntityManager();
        try
        {
            final ProjectDAO projectDAO = this.projectDAOFactory.createInstance(em);
            return projectDAO.findById(id);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public Project removeFoldersFromProject(Project project, List<Folder> deletedFolderList)
    {
        EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final ProjectDAO projectDAO = this.projectDAOFactory.createInstance(em);
            final FolderDAO folderDAO = this.folderDAOFactory.createInstance(em);
            final ResearchFileDAO researchFileDAO = this.researchFileDAOFactory.createInstance(em);
            final MetadataAssociationDAO metadataAssociationDAO = this.metaAssociationDAOFactory.createInstance(em);
            final SubmissionPackageDAO submissionPackageDAO = this.submissionPackageDAOFactory.createInstance(em);

            project = projectDAO.findById(project.getId());

            Iterator<Folder> folderIter = project.getFolders().iterator();
            while (folderIter.hasNext())
            {
                Folder folder = folderIter.next();
                if (deletedFolderList.contains(folder))
                {
                    LOG.info("Removing folder id= " + folder.getId());

                    em.getTransaction().begin();

                    Iterator<ResearchFile> fileIter = folder.getFiles().iterator();
                    while (fileIter.hasNext())
                    {
                        ResearchFile researchFile = fileIter.next();
                        Group parentGroup = researchFile.getParentGroup();
                        parentGroup.getResearchFiles().remove(researchFile);

                        Iterator<MetadataAssociation> maIter = researchFile.getMetadataAssociations().iterator();
                        while (maIter.hasNext())
                        {
                            MetadataAssociation metadataAssociation = maIter.next();
                            metadataAssociationDAO.removeMetadataAssociation(metadataAssociation);
                            maIter.remove();
                        }

                        // Remove the research file from any submission packages it is part of.
                        final List<SubmissionPackage> submissionPackages = submissionPackageDAO
                                .findSubmissionPackagesWithResearchFile(researchFile);
                        for (final SubmissionPackage submissionPackage : submissionPackages)
                        {
                            submissionPackage.getResearchFiles().remove(researchFile);
                            submissionPackageDAO.updateSubmissionPackage(submissionPackage);
                        }

                        fileIter.remove();
                        researchFileDAO.removeResearchFile(researchFile);
                    }
                    folderIter.remove();
                    folderDAO.removeFolder(folder);
                    em.getTransaction().commit();
                }
            }
            return project;
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public void updateFolderPath(long folderId, File newFileForFolder)
    {
        EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final FolderDAO folderDAO = this.folderDAOFactory.createInstance(em);
            final ResearchFileDAO researchFileDAO = this.researchFileDAOFactory.createInstance(em);

            Folder folder = folderDAO.findById(folderId);

            // modify the path in each of the research files
            List<ResearchFile> researchFiles = folder.getFiles();

            for (ResearchFile researchFile : researchFiles)
            {
                String originalPath = researchFile.getFile().getAbsolutePath();
                String newPath = originalPath.replace(folder.getFolder().getAbsolutePath(),
                        newFileForFolder.getAbsolutePath());
                File replacementFile = new File(newPath);
                researchFile.setFile(replacementFile);
                researchFileDAO.updateResearchFile(researchFile);
            }

            folder.setFolder(newFileForFolder);
            folderDAO.updateFolder(folder);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public String buildMetadataSchemaXML(Project project)
    {
        return MetadataSchemaXMLBuilder.buildXML(project);
    }

    @Override
    public void replaceResearchFileInSubmissionPackageAndDeleteReplacedFile(long fileToBeReplacedId, long fileToBeUsedId)
    {
        EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final ResearchFileDAO researchFileDAO = this.researchFileDAOFactory.createInstance(em);
            final MetadataAssociationDAO metadataAssociationDAO = this.metaAssociationDAOFactory.createInstance(em);
            final SubmissionPackageDAO submissionPackageDAO = this.submissionPackageDAOFactory.createInstance(em);
            final FolderDAO folderDAO = this.folderDAOFactory.createInstance(em);

            ResearchFile fileToBeReplacedAndDeleted = researchFileDAO.findById(fileToBeReplacedId);
            ResearchFile fileToBeUsed = researchFileDAO.findById(fileToBeUsedId);
            
            em.getTransaction().begin();

            Group parentGroup = fileToBeReplacedAndDeleted.getParentGroup();
            parentGroup.getResearchFiles().remove(fileToBeReplacedAndDeleted);

            Iterator<MetadataAssociation> maIter = fileToBeReplacedAndDeleted.getMetadataAssociations().iterator();
            while (maIter.hasNext())
            {
                MetadataAssociation metadataAssociation = maIter.next();
                metadataAssociationDAO.removeMetadataAssociation(metadataAssociation);
                maIter.remove();
            }

            // Remove the research file from any submission packages it is part of.
            final List<SubmissionPackage> submissionPackages = submissionPackageDAO
                    .findSubmissionPackagesWithResearchFile(fileToBeReplacedAndDeleted);
            for (final SubmissionPackage submissionPackage : submissionPackages)
            {
                submissionPackage.getResearchFiles().remove(fileToBeReplacedAndDeleted);
                submissionPackage.getResearchFiles().add(fileToBeUsed);
                submissionPackageDAO.updateSubmissionPackage(submissionPackage);
            }
            
            Folder folder = researchFileDAO.getParentFolder(fileToBeReplacedAndDeleted);
            folder.getFiles().remove(fileToBeReplacedAndDeleted);
            folderDAO.updateFolder(folder);

            researchFileDAO.removeResearchFile(fileToBeReplacedAndDeleted);
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }   
}
