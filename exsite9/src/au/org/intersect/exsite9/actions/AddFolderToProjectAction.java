/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectService;

/**
 * 
 */
public final class AddFolderToProjectAction extends Action
{
    private final Project theProject;

    public AddFolderToProjectAction(final Project theProjet)
    {
        this.theProject = theProjet;
    }

    @Override
    public String getText()
    {
        return "Add Folder";
    }

    @Override
    public void run()
    {
        final Shell parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

        final DirectoryDialog directoryDialog = new DirectoryDialog(parent, SWT.OPEN);
        directoryDialog.setMessage("Choose a folder to add to the project.");
        directoryDialog.setText("Choose a folder");

        final String path = directoryDialog.open();
        if (path != null)
        {
            final File directory = new File(path);

            // TODO: Deal with this
            // directory.isDirectory() && directory.canRead();

            final Folder folder = new Folder(directory);

            final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);
            projectService.mapFolderToProject(this.theProject, folder);
        }
    }
}
