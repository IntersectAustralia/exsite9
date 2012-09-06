package au.org.intersect.exsite9.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.util.ArrayList;
import java.util.Collections;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.factory.MetadataCategoryDAOFactory;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;

public class MetadataCategoryServiceUnitTest extends DAOTest
{
    private MetadataCategoryService metadataCategoryService;

    @Test
    public void createNewMetadataCategoryTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                       .toReturn(createEntityManager());

        MetadataCategoryDAOFactory metadataCategoryDAOFactory = new MetadataCategoryDAOFactory();

        metadataCategoryService = new MetadataCategoryService(emf, metadataCategoryDAOFactory);

        MetadataCategory category = metadataCategoryService.createNewMetadataCategory("Names", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional, false, null);
        MetadataCategory categoryFoundById = metadataCategoryService.findById(category.getId());

        assertEquals(category, categoryFoundById);
    }
    
    @Test
    public void updateMetadataCategoryTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                        .toReturn(createEntityManager());
        
        MetadataCategoryDAOFactory metadataCategoryDAOFactory = new MetadataCategoryDAOFactory();

        metadataCategoryService = new MetadataCategoryService(emf, metadataCategoryDAOFactory);

        MetadataCategory category = metadataCategoryService.createNewMetadataCategory("Names", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional, false, null);
        
        metadataCategoryService.updateMetadataCategory(category, "NameUpdated", MetadataCategoryUse.required, false, null);
        
        MetadataCategory updatedCategoryFoundById = metadataCategoryDAOFactory.createInstance(createEntityManager())
                .findById(category.getId());

        assertTrue(updatedCategoryFoundById.getName().equals("NameUpdated"));
        
        assertTrue(updatedCategoryFoundById.getUse().equals(MetadataCategoryUse.required));
    }

    @Test
    public void testDeleteMetadataCategory()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager());
        final MetadataCategoryDAOFactory metadataCategoryDAOFactory = new MetadataCategoryDAOFactory();
        metadataCategoryService = new MetadataCategoryService(emf, metadataCategoryDAOFactory);

        final MetadataCategory category = metadataCategoryService.createNewMetadataCategory("Names",
            MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional, false, Collections.<MetadataValue>emptyList());
        assertNotNull(category.getId());

        metadataCategoryService.deleteMetadataCategory(category);
        assertNotNull(category.getId());

        assertNull(metadataCategoryService.findById(category.getId()));
    }

    @Test
    public void testAddValueToMetadataCategory()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager());
        final MetadataCategoryDAOFactory metadataCategoryDAOFactory = new MetadataCategoryDAOFactory();
        metadataCategoryService = new MetadataCategoryService(emf, metadataCategoryDAOFactory);

        MetadataCategory category = metadataCategoryService.createNewMetadataCategory("Names",
            MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional, false, new ArrayList<MetadataValue>());
        assertNotNull(category.getId());

        final String value1 = "value1";

        final MetadataValue mdv1 = metadataCategoryService.addValueToMetadataCategory(category, value1);
        assertEquals(value1, mdv1.getValue());
        assertNotNull(mdv1.getId());

        category = metadataCategoryService.findById(category.getId());

        final MetadataValue mdv11 = metadataCategoryService.addValueToMetadataCategory(category, value1);
        assertEquals(mdv1, mdv11);

        category = metadataCategoryService.findById(category.getId());
        final String value2 = "value2";

        final MetadataValue mdv2 = metadataCategoryService.addValueToMetadataCategory(category, value2);
        assertEquals(value2, mdv2.getValue());
        assertNotNull(mdv2.getId());
    }
}
