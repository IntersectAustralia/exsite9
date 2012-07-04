/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;

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
        em.getTransaction().begin();
        em.merge(submissionPackage);
        em.getTransaction().commit();
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
}
