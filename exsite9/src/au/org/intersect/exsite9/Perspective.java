/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

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
        layout.addStandaloneView(ProjectExplorerView.ID, false, IPageLayout.LEFT, 1.0f, layout.getEditorArea());
        layout.setEditorAreaVisible(false);
    }
}
