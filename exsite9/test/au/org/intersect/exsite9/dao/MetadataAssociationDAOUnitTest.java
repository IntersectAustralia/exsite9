/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Test;
import org.mockito.Mockito;

import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataCategory;

/**
 * Tests {@link MetadataAssociationDAO}
 */
public final class MetadataAssociationDAOUnitTest
{
    @Test
    public void testCreateMetadataAssociation()
    {
        final EntityManager em = Mockito.mock(EntityManager.class);
        final EntityTransaction et = Mockito.mock(EntityTransaction.class);

        final MetadataAssociationDAO toTest = new MetadataAssociationDAO(em);

        final MetadataCategory mdc = new MetadataCategory("mdc");
        final MetadataAssociation mda = new MetadataAssociation(mdc);

        when(em.getTransaction()).thenReturn(et);

        toTest.createMetadataAssociation(mda);

        verify(et).begin();
        verify(em).persist(mda);
        verify(et).commit();
    }

    @Test
    public void testUpdateMetadataAssociation()
    {
        final EntityManager em = Mockito.mock(EntityManager.class);
        final EntityTransaction et = Mockito.mock(EntityTransaction.class);

        final MetadataAssociationDAO toTest = new MetadataAssociationDAO(em);

        final MetadataCategory mdc = new MetadataCategory("mdc");
        final MetadataAssociation mda = new MetadataAssociation(mdc);

        when(em.getTransaction()).thenReturn(et);

        toTest.updateMetadataAssociation(mda);

        verify(et).begin();
        verify(em).merge(mda);
        verify(et).commit();
    }

    @Test
    public void testRemoveMetadataAssociation()
    {
        final EntityManager em = Mockito.mock(EntityManager.class);
        final EntityTransaction et = Mockito.mock(EntityTransaction.class);

        final MetadataAssociationDAO toTest = new MetadataAssociationDAO(em);

        final MetadataCategory mdc = new MetadataCategory("mdc");
        final MetadataAssociation mda = new MetadataAssociation(mdc);

        when(em.getTransaction()).thenReturn(et);
        when(em.merge(mda)).thenReturn(mda);

        toTest.removeMetadataAssociation(mda);

        verify(et).begin();
        verify(em).remove(mda);
        verify(et).commit();
    }

    @Test
    public void testRemoveMetadataAssociationInTrans()
    {
        final EntityManager em = Mockito.mock(EntityManager.class);
        final EntityTransaction et = Mockito.mock(EntityTransaction.class);

        final MetadataAssociationDAO toTest = new MetadataAssociationDAO(em);

        final MetadataCategory mdc = new MetadataCategory("mdc");
        final MetadataAssociation mda = new MetadataAssociation(mdc);

        when(em.getTransaction()).thenReturn(et);
        when(em.merge(mda)).thenReturn(mda);
        when(et.isActive()).thenReturn(true);

        toTest.removeMetadataAssociation(mda);

        verify(em).remove(mda);
    }
}
