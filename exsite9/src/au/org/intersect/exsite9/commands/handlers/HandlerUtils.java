/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.commands.handlers;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;

/**
 * Utilities used by the Handlers
 */
public final class HandlerUtils
{
    public static void activateShowProjectActivity(final IWorkbenchWindow activeWorkbenchWindow)
    {
        final IWorkbenchActivitySupport activitySupport = activeWorkbenchWindow.getWorkbench().getActivitySupport();
        final IActivityManager activityManager = activitySupport.getActivityManager();
        final Set<String> enabledActivities = new HashSet<String>();
        final String id = "au.org.intersect.exsite9.activity.showProject";
        if (activityManager.getActivity(id).isDefined())
        {
            enabledActivities.add(id);
        }
        activitySupport.setEnabledActivityIds(enabledActivities);
    }
}
