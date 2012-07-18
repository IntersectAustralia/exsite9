/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.listexcludedfiles;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.dto.HierarchyMoveDTO;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IProjectManager;

/**
 * Wizard used to List the excluded files.
 */
public final class ListExcludedFilesWizard extends Wizard
{
    private ListExcludedFilesWizardPage1 page1;

    /**
     * 
     */
    public ListExcludedFilesWizard()
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addPages()
    {
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project project = projectManager.getCurrentProject();
        final Group excludedFilesGroup = project.getExcludedFilesNode();
        final List<ResearchFile> excludedFiles = excludedFilesGroup.getResearchFiles();

        this.page1 = new ListExcludedFilesWizardPage1(excludedFiles);
        addPage(page1);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean performFinish()
    {
        final List<ResearchFile> filesToInclude = this.page1.getExcludedFilesToInclude();
        if (filesToInclude.isEmpty())
        {
            return true;
        }

        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);

        final Project project = projectManager.getCurrentProject();
        final Group excludedFilesGroup = project.getExcludedFilesNode();
        final Group newFilesGroup = project.getNewFilesNode();

        final List<HierarchyMoveDTO> moveObjects = new ArrayList<HierarchyMoveDTO>(filesToInclude.size());

        for (final ResearchFile rf : filesToInclude)
        {
            moveObjects.add(new HierarchyMoveDTO(rf, excludedFilesGroup, newFilesGroup));
        }

        final String out = groupService.performHierarchyMove(moveObjects);

        if (out != null)
        {
            final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
            MessageDialog.openError(shell, "Could not re-include files", out);
        }

        return true;
    }
}
