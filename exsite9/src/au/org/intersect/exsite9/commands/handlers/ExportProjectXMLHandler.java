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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.OutputSupplier;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.xml.ProjectXMLBuilder;

/**
 * Command handler to export the xml from a project, via the plugin.xml
 */
public final class ExportProjectXMLHandler implements IHandler
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
        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();

        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project currentproject = projectManager.getCurrentProject();

        final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
        fileDialog.setOverwrite(true);
        fileDialog.setFileName(currentproject.getName() + ".xml");
        final String filePath = fileDialog.open();

        if (filePath == null)
        {
            return null;
        }

        final String xml = ProjectXMLBuilder.buildXML(currentproject);

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
