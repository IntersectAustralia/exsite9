/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.commands.handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.OutputSupplier;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.ISubmissionPackageService;

/**
 * Command handler to export a submission package xml from a project, via the plugin.xml
 */
public class ExportSubmissionPackageXMLHandler implements IHandler
{
    private static final String NEW_LINE = System.getProperty("line.separator");

    @Override
    public void addHandlerListener(IHandlerListener handlerListener)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
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

        // Before we generate the output XML, we should ensure that all files selected for this SubmissionPackage are still where we think they are.
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
            MessageDialog.openWarning(shell, "Files missing", "The following files are missing. They will not be included in the submission package XML."
                + NEW_LINE + sb.toString());
        }
        
        final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
        fileDialog.setOverwrite(true);
        fileDialog.setFileName(submissionPackage.getName() + ".xml");
        final String filePath = fileDialog.open();

        if (filePath == null)
        {
            return null;
        }

        
        final ISubmissionPackageService submissionPackageService = (ISubmissionPackageService) PlatformUI.getWorkbench().getService(ISubmissionPackageService.class);
        
        final String xml = submissionPackageService.buildXMLForSubmissionPackage(currentproject, submissionPackage);

        final File fileToWrite = new File(filePath);
        final OutputSupplier<FileOutputStream> outputSupplier = Files.newOutputStreamSupplier(fileToWrite, false);
        try
        {
            ByteStreams.write(xml.getBytes(Charsets.UTF_8), outputSupplier);
        }
        catch (final IOException e)
        {
            MessageDialog.openError(shell, "Error", "Could not save to file " + fileToWrite.getAbsolutePath());
        }

        return null;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public boolean isHandled()
    {
        return true;
    }

    @Override
    public void removeHandlerListener(IHandlerListener handlerListener)
    {
        // TODO Auto-generated method stub
    }

}
