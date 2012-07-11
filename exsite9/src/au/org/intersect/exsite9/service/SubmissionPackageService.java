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

import au.org.intersect.exsite9.dao.GroupDAO;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.SubmissionPackageDAO;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.SubmissionPackageDAOFactory;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.util.ProgressRunnableWithError;
import au.org.intersect.exsite9.xml.SIPXMLBuilder;
import au.org.intersect.exsite9.zip.SIPZIPBuilder;

/**
 * 
 */
public final class SubmissionPackageService implements ISubmissionPackageService
{
    private final EntityManagerFactory entityManagerFactory;
    private final SubmissionPackageDAOFactory submissionPackageDAOFactory;
    private final ProjectDAOFactory projectDAOFactory;
    private final GroupDAOFactory groupDAOFactory;

    public SubmissionPackageService(final EntityManagerFactory entityManagerFactory,
                                    final SubmissionPackageDAOFactory submissionPackageDAOFactory,
                                    final ProjectDAOFactory projectDAOFactory,
                                    final GroupDAOFactory groupDAOFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.submissionPackageDAOFactory = submissionPackageDAOFactory;
        this.projectDAOFactory = projectDAOFactory;
        this.groupDAOFactory = groupDAOFactory;
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

    @Override
    public String buildXMLForSubmissionPackage(Project project, SubmissionPackage submissionPackage)
    {
        final EntityManager em = this.entityManagerFactory.createEntityManager();
        try
        {
            GroupDAO groupDAO = groupDAOFactory.createInstance(em);
            return SIPXMLBuilder.buildXML(project, 
                                          groupDAO.getGroupsContainingSelectedFiles(submissionPackage.getResearchFiles()), 
                                          submissionPackage, false);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public ProgressRunnableWithError buildZIPForSubmissionPackage(final Project project, final SubmissionPackage submissionPackage, final File fileToWrite)
    {
        final EntityManager em = this.entityManagerFactory.createEntityManager();

        try
        {
            final GroupDAO groupDAO = groupDAOFactory.createInstance(em);

            final List<Group> selectedGroups = groupDAO.getGroupsContainingSelectedFiles(submissionPackage.getResearchFiles());
            return new SIPZIPBuilder(project, selectedGroups, submissionPackage, fileToWrite);
        }
        finally
        {
            em.close();
        }

    }

}
