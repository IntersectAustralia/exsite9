package au.org.intersect.exsite9.dao;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Test;
import org.mockito.Mockito;

import au.org.intersect.exsite9.domain.MetadataAttribute;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;

public final class MetadataAttributeDAOUnitTest
{
    @Test
    public void testCreateMetadataCategory()
    {
        final EntityManager em = Mockito.mock(EntityManager.class);
        final EntityTransaction et = Mockito.mock(EntityTransaction.class);
        final MetadataAttributeDAO toTest = new MetadataAttributeDAO(em);

        final MetadataAttribute mda = new MetadataAttribute("name", Collections.<MetadataAttributeValue>emptyList());

        when(em.getTransaction()).thenReturn(et);

        toTest.createMetadataAttribute(mda);

        verify(et).begin();
        verify(em).persist(mda);
        verify(et).commit();
    }

    @Test
    public void testUpdateMedataCategory()
    {
        final EntityManager em = Mockito.mock(EntityManager.class);
        final EntityTransaction et = Mockito.mock(EntityTransaction.class);
        final MetadataAttributeDAO toTest = new MetadataAttributeDAO(em);

        final MetadataAttribute mda = new MetadataAttribute("name", Collections.<MetadataAttributeValue>emptyList());

        when(em.getTransaction()).thenReturn(et);

        toTest.updateMetadataAttribute(mda);

        verify(et).begin();
        verify(em).merge(mda);
        verify(et).commit();
    }

    @Test
    public void testFindByID()
    {
        final EntityManager em = Mockito.mock(EntityManager.class);
        final MetadataAttributeDAO toTest = new MetadataAttributeDAO(em);

        toTest.findById(7l);

        verify(em).find(MetadataAttribute.class, 7l);
    }

    @Test
    public void testDelete()
    {
        final EntityManager em = Mockito.mock(EntityManager.class);
        final EntityTransaction et = Mockito.mock(EntityTransaction.class);
        final MetadataAttributeDAO toTest = new MetadataAttributeDAO(em);

        final MetadataAttribute mda = new MetadataAttribute("name", Collections.<MetadataAttributeValue>emptyList());

        when(em.getTransaction()).thenReturn(et);
        when(em.merge(mda)).thenReturn(mda);
        toTest.delete(mda);
        verify(em).remove(mda);
    }
}
