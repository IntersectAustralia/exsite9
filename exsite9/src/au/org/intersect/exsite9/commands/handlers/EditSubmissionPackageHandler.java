/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.commands.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.wizard.CustomButtonFocusWizardDialog;
import au.org.intersect.exsite9.wizard.createsubmissionpackage.CreateSubmissionPackageWizard;

/**
 * Handler to Edit a submission package.
 */
public final class EditSubmissionPackageHandler implements IHandler
{

    /**
     * @{inheritDoc}
     */
    @Override
    public void dispose()
    {

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException
    {
        final IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
        final Object selectedObject = selection.getFirstElement();

        if (!(selectedObject instanceof SubmissionPackage))
        {
            throw new IllegalArgumentException("Trying to edit an object that is not a SubmissionPackage");
        }

        final SubmissionPackage toEdit = (SubmissionPackage) selectedObject;

        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project project = projectManager.getCurrentProject();

        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
        final CreateSubmissionPackageWizard wizard = new CreateSubmissionPackageWizard(toEdit, project.getSubmissionPackages(), "Edit Submission Package");

        final WizardDialog wizardDialog = new CustomButtonFocusWizardDialog(shell, wizard, IDialogConstants.NEXT_ID);
        wizardDialog.open();
        
        return wizard.getSubmissionPackage();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isEnabled()
    {
        return true;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isHandled()
    {
        return true;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addHandlerListener(final IHandlerListener handlerListener)
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void removeHandlerListener(final IHandlerListener handlerListener)
    {
    }

}
