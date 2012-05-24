/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view.provider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import au.org.intersect.exsite9.domain.File;
import au.org.intersect.exsite9.domain.Node;
import au.org.intersect.exsite9.view.ProjectExplorerView;

/**
 * Provides labels to {@link ProjectExplorerView}
 */
public final class ProjectExplorerViewLabelProvider extends LabelProvider
{
    /**
     * Constructor
     */
    public ProjectExplorerViewLabelProvider()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText(final Object element)
    {
        if (element instanceof Node)
        {
            final Node node = (Node) element;
            return node.getName();
        }
        if (element instanceof File)
        {
            final File file = (File) element;
            return file.getName();
        }
        return "<unknown>";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Image getImage(final Object element)
    {
        return super.getImage(element);
    }
}
