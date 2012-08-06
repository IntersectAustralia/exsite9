/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.domain.Schema;

/**
 * Schema DAO.
 */
public final class SchemaDAO
{
    private final EntityManager em;

    public SchemaDAO(final EntityManager em)
    {
        this.em = em;
    }

    public void createSchema(final Schema schema)
    {
        em.getTransaction().begin();
        em.persist(schema);
        em.getTransaction().commit();
    }

    public void updateSchema(final Schema schema)
    {
        em.getTransaction().begin();
        em.merge(schema);
        em.getTransaction().commit();
    }

    public void delete(final Schema schema)
    {
        em.getTransaction().begin();
        em.remove(em.merge(schema));
        em.getTransaction().commit();
    }
}
