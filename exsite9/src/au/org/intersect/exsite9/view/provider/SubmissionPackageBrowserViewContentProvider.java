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

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.service.IProjectService;
import au.org.intersect.exsite9.service.ISubmissionPackageService;
import au.org.intersect.exsite9.view.SubmissionPackageBrowserView;

/**
 * Provides content to {@link SubmissionPackageBrowserView}
 */
public final class SubmissionPackageBrowserViewContentProvider implements ITreeContentProvider
{
    private final IProjectService projectService;
    private final ISubmissionPackageService submissionPackageService;

    public SubmissionPackageBrowserViewContentProvider()
    {
        this((IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class), (ISubmissionPackageService) PlatformUI.getWorkbench().getService(ISubmissionPackageService.class));
    }

    SubmissionPackageBrowserViewContentProvider(final IProjectService projectService, final ISubmissionPackageService submissionPackageService)
    {
        this.projectService = projectService;
        this.submissionPackageService = submissionPackageService;
    }


    @Override
    public void dispose()
    {
    }

    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
    {
    }

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

    @Override
    public Object[] getChildren(final Object parentElement)
    {
        if (parentElement instanceof Project)
        {
            final Project project = (Project) parentElement;
            final List<SubmissionPackage> toReturn = new ArrayList<SubmissionPackage>(project.getSubmissionPackages().size());
            for (final SubmissionPackage subPack : project.getSubmissionPackages())
            {
                toReturn.add(this.submissionPackageService.findSubmissionPackageById(subPack.getId()));
            }
            return toReturn.toArray();
        }
        return Collections.emptyList().toArray();
    }

    @Override
    public Object getParent(final Object element)
    {
        return null;
    }

    @Override
    public boolean hasChildren(final Object element)
    {
        if (element instanceof Project)
        {
            final Project project = (Project) element;
            return !project.getSubmissionPackages().isEmpty();
        }
        return false;
    }
}
