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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.service.ISubmissionPackageService;

/**
 * Handler used to delete a submission package.
 */
public final class DeleteSubmissionPackageHandler implements IHandler
{

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
            throw new RuntimeException("Trying to delete something that is not a SubmissionPackage");
        }

        final SubmissionPackage submissionPackage = (SubmissionPackage) selectedObject;

        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
        final boolean confirm = MessageDialog.openConfirm(shell, "Are you sure?", "Are you sure you want to delete submission package '" + submissionPackage.getName() + "'?");

        if (confirm)
        {
            final ISubmissionPackageService submissionPackageService = (ISubmissionPackageService) PlatformUI.getWorkbench().getService(ISubmissionPackageService.class);
            submissionPackageService.deleteSubmissionPackage(submissionPackage);
        }

        return null;
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
    public void removeHandlerListener(final IHandlerListener handlerListener)
    {
    }
}
