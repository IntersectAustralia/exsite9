/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.commands.handlers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

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

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.ISubmissionPackageService;

/**
 * Responsible for exporting a submission package to a ZIP file.
 */
public class ExportSubmissionPackageZIPHandler implements IHandler
{
    private static final Logger LOG = Logger.getLogger(ExportSubmissionPackageZIPHandler.class);

    private static final String NEW_LINE = System.getProperty("line.separator");

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

        // Before we generate the output ZIP, we should ensure that all files selected for this SubmissionPackage are still where we think they are.
        final Collection<ResearchFile> missingFiles = Collections2.filter(submissionPackage.getResearchFiles(), new Predicate<ResearchFile>()
        {
            @Override
            public boolean apply(final ResearchFile input)
            {
                return !input.getFile().exists();
            }
        });

        if (!missingFiles.isEmpty())
        {
            final StringBuilder sb = new StringBuilder();
            for (final Iterator<ResearchFile> iter = missingFiles.iterator(); iter.hasNext(); )
            {
                sb.append(iter.next().getFile().getAbsolutePath());
                if (iter.hasNext())
                {
                    sb.append(NEW_LINE);
                }
            }
            MessageDialog.openWarning(shell, "Files missing", "The following files are missing. They will not be included in the submission package ZIP file."
                + NEW_LINE + sb.toString());
        }

        final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
        fileDialog.setOverwrite(true);
        fileDialog.setFileName(submissionPackage.getName() + ".zip");
        final String filePath = fileDialog.open();

        if (filePath == null)
        {
            return null;
        }
        
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
            MessageDialog.openError(shell, "Could not generate submission package ZIP file", "Could not generate submission package ZIP file.\n\n" + cause.getMessage());
            LOG.error("Could not create submission package ZIP file.", e);
            return null;
        }
        catch (final InterruptedException e)
        {
            LOG.info("User cancelled generation of ZIP file.");
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
