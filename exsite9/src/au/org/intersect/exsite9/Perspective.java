/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;

import au.org.intersect.exsite9.view.AssociatedMetadataView;
import au.org.intersect.exsite9.view.MetadataBrowserView;
import au.org.intersect.exsite9.view.ProjectExplorerView;
import au.org.intersect.exsite9.view.SubmissionPackageBrowserView;

/**
 * A Perspective holds the configuration of configuration of views.
 */
public final class Perspective implements IPerspectiveFactory
{
    /**
     * {@inheritDoc}
     */
    public void createInitialLayout(final IPageLayout layout)
    {
        final IFolderLayout leftFolder = layout.createFolder("folder.left", IPageLayout.LEFT, 0.30f, layout.getEditorArea());
        final IFolderLayout rightFolder = layout.createFolder("folder.right", IPageLayout.RIGHT, 0.70f, layout.getEditorArea());
        final IFolderLayout bottomLeftFolder = layout.createFolder("folder.bottomLeft", IPageLayout.BOTTOM, 0.65f, "folder.left");

        leftFolder.addView(ProjectExplorerView.ID);
        leftFolder.addView(SubmissionPackageBrowserView.ID);
        bottomLeftFolder.addView(AssociatedMetadataView.ID);
        rightFolder.addView(MetadataBrowserView.ID);

        // Configure the Project Explorer View
        final IViewLayout projectExplorerViewLayout = layout.getViewLayout(ProjectExplorerView.ID);
        projectExplorerViewLayout.setCloseable(false);
        projectExplorerViewLayout.setMoveable(false);

        // Configure the Submission Package Browser View
        final IViewLayout submissionPackageBrowserViewLayout = layout.getViewLayout(SubmissionPackageBrowserView.ID);
        submissionPackageBrowserViewLayout.setCloseable(false);
        submissionPackageBrowserViewLayout.setMoveable(false);

        // Configure the Metadata Browser View
        final IViewLayout metadataBrowserViewLayout = layout.getViewLayout(MetadataBrowserView.ID);
        metadataBrowserViewLayout.setCloseable(false);
        metadataBrowserViewLayout.setMoveable(false);
        
        // Configure the Associated Metadata View
        final IViewLayout associatedMetadataViewLayout = layout.getViewLayout(AssociatedMetadataView.ID);
        associatedMetadataViewLayout.setCloseable(false);
        associatedMetadataViewLayout.setMoveable(false);

        layout.setEditorAreaVisible(false);

        // Disables the minimize & maximize buttons on views and editors.
        layout.setFixed(true);

        // Clears all the activities - since they are persisted magically.
        final IWorkbenchActivitySupport activitySupport = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getWorkbench().getActivitySupport();
        final Set<String> enabledActivities = new HashSet<String>();
        activitySupport.setEnabledActivityIds(enabledActivities);
    }
}
