/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.jface.operation.IRunnableWithProgress;

import au.org.intersect.exsite9.dao.GroupDAO;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.SubmissionPackageDAO;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.SubmissionPackageDAOFactory;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.xml.SIPXMLBuilder;
import au.org.intersect.exsite9.zip.SIPZIPBuilderRunnable;

/**
 * A service for manipulating {@link SubmissionPackage}s
 */
public final class SubmissionPackageService implements ISubmissionPackageService
{
    private final EntityManagerFactory entityManagerFactory;
    private final SubmissionPackageDAOFactory submissionPackageDAOFactory;
    private final ProjectDAOFactory projectDAOFactory;

    public SubmissionPackageService(final EntityManagerFactory entityManagerFactory,
                                    final SubmissionPackageDAOFactory submissionPackageDAOFactory,
                                    final ProjectDAOFactory projectDAOFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.submissionPackageDAOFactory = submissionPackageDAOFactory;
        this.projectDAOFactory = projectDAOFactory;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public SubmissionPackage createSubmissionPackage(final Project project, final String name, final String description, final List<ResearchFile> researchFiles)
    {
        final SubmissionPackage submissionPackage = new SubmissionPackage();
        submissionPackage.setName(name);
        submissionPackage.setDescription(description);
        submissionPackage.getResearchFiles().addAll(researchFiles);

        final EntityManager em = this.entityManagerFactory.createEntityManager();

        try
        {
            final SubmissionPackageDAO submissionPackageDAO = this.submissionPackageDAOFactory.createInstance(em);
            submissionPackageDAO.createSubmissionPackage(submissionPackage);

            project.getSubmissionPackages().add(submissionPackage);
            final ProjectDAO projectDAO = this.projectDAOFactory.createInstance(em);
            projectDAO.updateProject(project);

            return submissionPackage;
        }
        finally
        {
            em.close();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public SubmissionPackage updateSubmissionPackage(final SubmissionPackage submissionPackage, final String name, final String description, final List<ResearchFile> researchFiles)
    {
        submissionPackage.setName(name);
        submissionPackage.setDescription(description);
        submissionPackage.getResearchFiles().clear();
        submissionPackage.getResearchFiles().addAll(researchFiles);

        final EntityManager em = this.entityManagerFactory.createEntityManager();

        try
        {
            final SubmissionPackageDAO submissionPackageDAO = this.submissionPackageDAOFactory.createInstance(em);
            submissionPackageDAO.updateSubmissionPackage(submissionPackage);
            return submissionPackage;
        }
        finally
        {
            em.close();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public SubmissionPackage updateSubmissionPackage(final SubmissionPackage submissionPackage, final List<ResearchFile> researchFiles)
    {
        return updateSubmissionPackage(submissionPackage, submissionPackage.getName(), submissionPackage.getDescription(), researchFiles);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public SubmissionPackage findSubmissionPackageById(final Long id)
    {
        final EntityManager em = this.entityManagerFactory.createEntityManager();
        try
        {
            final SubmissionPackageDAO submissionPackageDAO = this.submissionPackageDAOFactory.createInstance(em);
            return submissionPackageDAO.findSubmissionPackageById(id);
        }
        finally
        {
            em.close();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void deleteSubmissionPackage(final SubmissionPackage submissionPackage)
    {
        final EntityManager em = this.entityManagerFactory.createEntityManager();
        try
        {
            final ProjectDAO projectDAO = this.projectDAOFactory.createInstance(em);

            final Project project = projectDAO.findProjectWithSubmissionPackage(submissionPackage);
            project.getSubmissionPackages().remove(submissionPackage);
            projectDAO.updateProject(project);

            final SubmissionPackageDAO submissionPackageDAO = this.submissionPackageDAOFactory.createInstance(em);
            submissionPackageDAO.deleteSubmissionPackage(submissionPackage);
        }
        finally
        {
            em.close();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String buildXMLForSubmissionPackage(Project project, SubmissionPackage submissionPackage)
    {
        final EntityManager em = this.entityManagerFactory.createEntityManager();
        try
        {
            return SIPXMLBuilder.buildXML(project, 
                                          GroupDAO.getGroupsContainingSelectedFiles(submissionPackage.getResearchFiles()), 
                                          submissionPackage, false);
        }
        finally
        {
            em.close();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public IRunnableWithProgress buildZIPForSubmissionPackage(final Project project, final SubmissionPackage submissionPackage, final File fileToWrite)
    {
        final EntityManager em = this.entityManagerFactory.createEntityManager();

        try
        {
            final List<Group> selectedGroups = GroupDAO.getGroupsContainingSelectedFiles(submissionPackage.getResearchFiles());
            return new SIPZIPBuilderRunnable(project, selectedGroups, submissionPackage, fileToWrite);
        }
        finally
        {
            em.close();
        }

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public List<SubmissionPackage> findSubmissionPackagesWithResearchFile(final ResearchFile researchFile)
    {
        final EntityManager em = this.entityManagerFactory.createEntityManager();
        try
        {
            final SubmissionPackageDAO submissionPackageDAO = submissionPackageDAOFactory.createInstance(em);
            return submissionPackageDAO.findSubmissionPackagesWithResearchFile(researchFile);
        }
        finally
        {
            em.close();
        }
    }
}
