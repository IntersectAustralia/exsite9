/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * 
 */
public final class ViewUtils
{
    public static IViewPart getViewByID(final IWorkbenchWindow window, final String viewID)
    {
        final IViewReference[] refs = window.getActivePage().getViewReferences();
        for (final IViewReference viewRef : refs)
        {
            if (viewRef.getId().equals(viewID))
            {
                return viewRef.getView(false);
            }
        }
        return null;
    }
}
