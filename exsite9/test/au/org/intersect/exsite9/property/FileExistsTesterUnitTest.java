/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.property;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.Test;
import org.mockito.Mockito;

import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Tests {@link FileExistsTester}
 */
public final class FileExistsTesterUnitTest
{
    @Test
    public void testFileExistsTester()
    {
        final File fileExists = Mockito.mock(File.class);
        final File fileNotExists = Mockito.mock(File.class);

        when(fileExists.exists()).thenReturn(true);
        when(fileNotExists.exists()).thenReturn(false);

        final ResearchFile rf1 = new ResearchFile(fileExists);
        final ResearchFile rf2 = new ResearchFile(fileNotExists);

        final FileExistsTester fet = new FileExistsTester();

        assertTrue(fet.test(rf1, "", null, null));
        assertFalse(fet.test(rf2, "", null, null));
    }
}
