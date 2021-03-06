/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;

/**
 * Tests {@link MetadataCategoryDAO}
 */
public final class MetadataCategoryDAOUnitTest
{

    @Test
    public void testCreateMetadataCategory()
    {
        final EntityManager em = Mockito.mock(EntityManager.class);
        final EntityTransaction et = Mockito.mock(EntityTransaction.class);
        final MetadataCategoryDAO toTest = new MetadataCategoryDAO(em);

        final MetadataCategory mdc = new MetadataCategory("My MDC Name", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);

        when(em.getTransaction()).thenReturn(et);

        toTest.createMetadataCategory(mdc);

        verify(et).begin();
        verify(em).persist(mdc);
        verify(et).commit();
    }

    @Test
    public void testUpdateMedataCategory()
    {
        final EntityManager em = Mockito.mock(EntityManager.class);
        final EntityTransaction et = Mockito.mock(EntityTransaction.class);
        final MetadataCategoryDAO toTest = new MetadataCategoryDAO(em);

        final MetadataCategory mdc = new MetadataCategory("My MDC Name", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);

        when(em.getTransaction()).thenReturn(et);

        toTest.updateMetadataCategory(mdc);

        verify(et).begin();
        verify(em).merge(mdc);
        verify(et).commit();
    }

    @Test
    public void testFindByID()
    {
        final EntityManager em = Mockito.mock(EntityManager.class);
        final MetadataCategoryDAO toTest = new MetadataCategoryDAO(em);

        toTest.findById(7l);

        verify(em).find(MetadataCategory.class, 7l);
    }

    @Test
    public void testDelete()
    {
        final EntityManager em = Mockito.mock(EntityManager.class);
        final EntityTransaction et = Mockito.mock(EntityTransaction.class);
        final MetadataCategoryDAO toTest = new MetadataCategoryDAO(em);

        final MetadataCategory mdc = new MetadataCategory("My MDC Name", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);

        when(em.getTransaction()).thenReturn(et);
        when(em.merge(mdc)).thenReturn(mdc);
        toTest.delete(mdc);
        verify(em).remove(mdc);
    }
}
