/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

import au.org.intersect.exsite9.view.ProjectExplorerView;

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
        // Add the Project Explorer View
        layout.addStandaloneView(ProjectExplorerView.ID, true, IPageLayout.LEFT, 1.0f, layout.getEditorArea());

        // Configure the added Project Explorer View
        final IViewLayout projectExplorerViewLayout = layout.getViewLayout(ProjectExplorerView.ID);
        projectExplorerViewLayout.setCloseable(false);
        projectExplorerViewLayout.setMoveable(false);

        layout.setEditorAreaVisible(true);

        // Disables the minimize & maximize buttons on views and editors.
        layout.setFixed(true);
    }
}
