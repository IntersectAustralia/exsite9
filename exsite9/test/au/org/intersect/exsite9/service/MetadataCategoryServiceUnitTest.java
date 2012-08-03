package au.org.intersect.exsite9.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.factory.MetadataCategoryDAOFactory;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;

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

        MetadataCategory category = metadataCategoryService.createNewMetadataCategory("Names", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional, null);
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

        MetadataCategory category = metadataCategoryService.createNewMetadataCategory("Names", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional, null);
        
        metadataCategoryService.updateMetadataCategory(category, "NameUpdated", MetadataCategoryUse.required, null);
        
        MetadataCategory updatedCategoryFoundById = metadataCategoryDAOFactory.createInstance(createEntityManager())
                .findById(category.getId());

        assertTrue(updatedCategoryFoundById.getName().equals("NameUpdated"));
        
        assertTrue(updatedCategoryFoundById.getUse().equals(MetadataCategoryUse.required));
    }
}
