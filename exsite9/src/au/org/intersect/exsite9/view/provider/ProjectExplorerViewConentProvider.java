/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import au.org.intersect.exsite9.domain.Node;
import au.org.intersect.exsite9.view.ProjectExplorerView;

/**
 * Provides content to {@link ProjectExplorerView}
 */
public final class ProjectExplorerViewConentProvider implements ITreeContentProvider
{
    /**
     * Constructor
     */
    public ProjectExplorerViewConentProvider()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getElements(final Object inputElement)
    {
        if (inputElement instanceof ProjectExplorerViewInput)
        {
            final ProjectExplorerViewInput viewInput = (ProjectExplorerViewInput) inputElement;
            return new Object[]{viewInput.getProject()};
        }
        return Collections.emptyList().toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getChildren(final Object parentElement)
    {
        if (parentElement instanceof Node)
        {
            final List<Object> toReturn = new ArrayList<Object>();
            final Node node = (Node) parentElement;
            toReturn.addAll(node.getGroups());
            toReturn.addAll(node.getFiles());
            return toReturn.toArray();
        }
        return Collections.emptyList().toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getParent(final Object element)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasChildren(final Object element)
    {
        if (element instanceof Node)
        {
            final Node node = (Node) element;
            return !node.getGroups().isEmpty() || !node.getFiles().isEmpty();
        }
        return false;
    }
}
