/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static org.junit.Assert.*;
import static au.org.intersect.exsite9.test.Assert.*;

import java.io.File;

import org.junit.Test;

/**
 * Tests {@link ResearchFile}
 */
public final class ResearchFileUnitTest
{

    @Test
    public void testConstruction()
    {
        final File file1 = new File("/tmp1/filename1");
        final File file2 = new File("/tmp2/filename2");

        final ResearchFile toTest1 = new ResearchFile(file1);
        final ResearchFile toTest2 = new ResearchFile(file1);
        final ResearchFile toTest3 = new ResearchFile(file2);

        final Project project1 = new Project();
        toTest1.setProject(project1);
        assertEquals(project1, toTest1.getProject());

        assertEquals(toTest1, toTest1);
        assertEquals(file1, toTest1.getFile());

        assertEquals(toTest1, toTest2);
        assertEquals(toTest2, toTest1);
        assertEquals(toTest1.hashCode(), toTest2.hashCode());

        assertNotEqualsHashCode(toTest2, toTest3);

        assertNotEquals(toTest1, null);
        assertNotEquals(toTest1, new Object());
        assertNotEquals(toTest1, file1);

        final Long id = Long.valueOf(72121);
        toTest1.setId(id);
        assertEquals(id, toTest1.getId());

        final String toString = toTest1.toString();
        assertTrue(toString.contains("file=" + file1));
    }
    
    @Test
    public void testIsMissingRequiredMetadata()
    {
        Project project = new Project();
        project.setName("My project");
        
        Schema schema = new Schema();
        schema.setName("schema");
        project.setSchema(schema);
        
        MetadataCategory cat1 = new MetadataCategory("cat1");
        cat1.setId(1L);
        schema.getMetadataCategories().add(cat1);
        
        MetadataCategory cat2 = new MetadataCategory("cat2");
        cat2.setId(2L);
        schema.getMetadataCategories().add(cat2);
        
        ResearchFile file = new ResearchFile(new File("test1"));
        file.setId(1L);
        file.setProject(project);
        
        assertFalse("No required metadata missing", file.isMissingRequiredMetadata());
        
        MetadataAssociation assoc1 = new MetadataAssociation();
        assoc1.setId(1L);
        assoc1.setMetadataCategory(cat1);
        file.getMetadataAssociations().add(assoc1);

        MetadataAssociation assoc2 = new MetadataAssociation();
        assoc2.setId(2L);
        assoc2.setMetadataCategory(cat2);
        file.getMetadataAssociations().add(assoc2);

        assertFalse("No required metadata missing", file.isMissingRequiredMetadata());
        
        cat1.setUse(MetadataCategoryUse.recommended);
        cat2.setUse(MetadataCategoryUse.recommended);
        
        assertFalse("No required metadata missing", file.isMissingRequiredMetadata());

        cat1.setUse(MetadataCategoryUse.required);
        cat2.setUse(MetadataCategoryUse.required);

        assertFalse("No required metadata missing", file.isMissingRequiredMetadata());

        file.getMetadataAssociations().remove(assoc2);
        
        assertTrue("Required metadata missing", file.isMissingRequiredMetadata());
    }
}
