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
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.NewFilesGroup;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.utils.AlphabeticalGroupComparator;
import au.org.intersect.exsite9.domain.utils.AlphabeticalResearchFileComparator;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IProjectService;
import au.org.intersect.exsite9.view.ProjectExplorerView;

/**
 * Provides content to {@link ProjectExplorerView}
 */
public final class ProjectExplorerViewContentProvider implements ITreeContentProvider
{
    private final boolean includeNewFilesGroup;
    private final IGroupService groupService;
    private final IProjectService projectService;

    /**
     * Constructor
     */
    public ProjectExplorerViewContentProvider(final boolean includeNewFilesGroup)
    {
        this((IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class), (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class), includeNewFilesGroup);
    }

    ProjectExplorerViewContentProvider(final IGroupService groupService, final IProjectService projectService, final boolean includeNewFilesGroup)
    {
        this.groupService = groupService;
        this.projectService = projectService;
        this.includeNewFilesGroup = includeNewFilesGroup;
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
        if (inputElement instanceof ProjectViewInputWrapper)
        {
            final ProjectViewInputWrapper viewInput = (ProjectViewInputWrapper) inputElement;
            return new Object[]{this.projectService.findProjectById(viewInput.getProject().getId())};
        }
        return Collections.emptyList().toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getChildren(final Object parentElement)
    {
        final Group rootGroup;
        if (parentElement instanceof Project)
        {
            final Project project = (Project) parentElement;
            rootGroup = this.groupService.findGroupByID(project.getRootNode().getId());
        }
        else if (parentElement instanceof Group)
        {
            rootGroup = this.groupService.findGroupByID(((Group) parentElement).getId());
        }
        else
        {
            return Collections.emptyList().toArray();
        }

        final List<Group> groups;
        if (!this.includeNewFilesGroup)
        {
            groups = new ArrayList<Group>(Collections2.filter(rootGroup.getGroups(), Predicates.not(Predicates.instanceOf(NewFilesGroup.class))));
        }
        else
        {
            groups = new ArrayList<Group>(rootGroup.getGroups());
        }

        final List<ResearchFile> researchFiles = new ArrayList<ResearchFile>(rootGroup.getResearchFiles());

        Collections.sort(groups, new AlphabeticalGroupComparator());
        Collections.sort(researchFiles, new AlphabeticalResearchFileComparator());

        final List<Object> toReturn = new ArrayList<Object>();
        toReturn.addAll(groups);
        toReturn.addAll(researchFiles);
        return toReturn.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getParent(final Object element)
    {
        if (element instanceof Group)
        {
            final Group group = (Group) element;
            return group.getParentGroup();
        }
        if (element instanceof ResearchFile)
        {
            final ResearchFile researchFile = (ResearchFile) element;
            return researchFile.getParentGroup();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasChildren(final Object element)
    {
        if (element instanceof Project)
        {
            final Project project = (Project) element;
            final Group group = project.getRootNode();
            return !group.getGroups().isEmpty() || !group.getResearchFiles().isEmpty();
        }
        if (element instanceof Group)
        {
            final Group group = (Group) element;
            return !group.getGroups().isEmpty() || !group.getResearchFiles().isEmpty();
        }
        return false;
    }
}
