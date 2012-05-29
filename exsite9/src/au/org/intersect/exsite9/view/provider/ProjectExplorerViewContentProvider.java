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

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.view.ProjectExplorerView;

/**
 * Provides content to {@link ProjectExplorerView}
 */
public final class ProjectExplorerViewContentProvider implements ITreeContentProvider
{
    /**
     * Constructor
     */
    public ProjectExplorerViewContentProvider()
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
            return new Object[]{viewInput.getProject().getRootNode()};
        }
        return Collections.emptyList().toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getChildren(final Object parentElement)
    {
        if (parentElement instanceof Group)
        {
            final List<Object> toReturn = new ArrayList<Object>();
            final Group group = (Group) parentElement;
            toReturn.addAll(group.getGroups());
            toReturn.addAll(group.getResearchFiles());
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
        if (element instanceof Group)
        {
            final Group group = (Group) element;
            return !group.getGroups().isEmpty() || !group.getResearchFiles().isEmpty();
        }
        return false;
    }
}
