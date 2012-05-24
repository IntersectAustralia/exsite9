/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view.provider;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.eclipse.jface.viewers.ViewerCell;
import org.junit.Test;

/**
 * Tests {@link ProjectExplorerViewLabelProvider}
 */
public final class ProjectExplorerViewLabelProviderUnitTest
{

    @Test
    public void testGetText()
    {
        final ProjectExplorerViewLabelProvider toTest = new ProjectExplorerViewLabelProvider();
        final ViewerCell cell = mock(ViewerCell.class);

        try
        {
            toTest.update(cell);
            fail();
        }
        catch (final IllegalArgumentException e)
        {
            // Expected.
        }

        when(cell.getElement()).thenReturn("");
        try
        {
            toTest.update(cell);
            fail();
        }
        catch (final IllegalArgumentException e)
        {
            // Expected.
        }

        when(cell.getElement()).thenReturn(new Object());
        try
        {
            toTest.update(cell);
            fail();
        }
        catch (final IllegalArgumentException e)
        {
            // Expected.
        }

        // The rest of the class is difficult to test because there is no Workbench at unit test time.
    }
    
}