/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;

public final class SubmissionPackageDAO
{
    private EntityManager em;

    public SubmissionPackageDAO(final EntityManager em)
    {
        this.em = em;
    }

    public void createSubmissionPackage(final SubmissionPackage submissionPackage)
    {
        em.getTransaction().begin();
        em.persist(submissionPackage);
        em.getTransaction().commit();
    }

    public void updateSubmissionPackage(final SubmissionPackage submissionPackage)
    {
        final boolean activeTransaction = em.getTransaction().isActive();

        if (!activeTransaction)
        {
            em.getTransaction().begin();
        }
        em.merge(submissionPackage);
        if (!activeTransaction)
        {
            em.getTransaction().commit();
        }
    }

    public void deleteSubmissionPackage(final SubmissionPackage submissionPackage)
    {
        em.getTransaction().begin();
        em.remove(em.merge(submissionPackage));
        em.getTransaction().commit();
    }

    public SubmissionPackage findSubmissionPackageById(final Long id)
    {
        return em.find(SubmissionPackage.class, id);
    }

    public List<SubmissionPackage> findSubmissionPackagesWithResearchFile(final ResearchFile researchFile)
    {
        final TypedQuery<SubmissionPackage> query = em.createQuery("SELECT s FROM SubmissionPackage s WHERE :researchFile MEMBER OF s.researchFiles", SubmissionPackage.class);
        query.setParameter("researchFile", researchFile);
        return query.getResultList();
    }
}
