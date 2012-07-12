/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.commands.handlers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.ISubmissionPackageService;

/**
 * Responsible for exporting a submission package to a ZIP file.
 */
public class ExportSubmissionPackageZIPHandler implements IHandler
{
    private static final Logger LOG = Logger.getLogger(ExportSubmissionPackageZIPHandler.class);

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
        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();

        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project currentproject = projectManager.getCurrentProject();

        final IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
        final Object selectedObject = selection.getFirstElement();

        if (!(selectedObject instanceof SubmissionPackage))
        {
            throw new IllegalArgumentException("Trying to export XML from an object that is not a SubmissionPackage");
        }

        final SubmissionPackage submissionPackage = (SubmissionPackage) selectedObject;

        final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
        fileDialog.setOverwrite(true);
        fileDialog.setFileName(submissionPackage.getName() + ".zip");
        final String filePath = fileDialog.open();

        if (filePath == null)
        {
            return null;
        }

        // TODO Before we generate the output ZIP, we should ensure that all files selected for this SubmissionPackage are still where we think they are.

        final ISubmissionPackageService submissionPackageService = (ISubmissionPackageService) PlatformUI.getWorkbench().getService(ISubmissionPackageService.class);
        final IRunnableWithProgress zipBuilderRunnable = submissionPackageService.buildZIPForSubmissionPackage(currentproject, submissionPackage, new File(filePath));

        final ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
        try
        {
            progressDialog.run(true, true, zipBuilderRunnable);
        }
        catch (final InvocationTargetException e)
        {
            final Throwable cause = e.getCause();
            MessageDialog.openError(shell, "Could not generate submission package ZIP file", "Could not generate submission package ZIP file. " + cause.getMessage());
            LOG.error("Could not create submission package ZIP file. ", e);
            return null;
        }
        catch (final InterruptedException e)
        {
            LOG.info("User cancelled generation of ZIP file ");
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
