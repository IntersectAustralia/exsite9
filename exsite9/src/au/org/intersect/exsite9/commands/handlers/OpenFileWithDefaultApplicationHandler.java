/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.commands.handlers;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.util.DesktopUtils;

/**
 * Command handler opens the selected file with the systems default application for that file type, via the plugin.xml
 */
public class OpenFileWithDefaultApplicationHandler implements IHandler
{
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final Logger LOG = Logger.getLogger(OpenFileWithDefaultApplicationHandler.class);
    
    @Override
    public void addHandlerListener(IHandlerListener arg0)
    {
    }

    @Override
    public void dispose()
    {
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
        final Shell shell = activeWorkbenchWindow.getShell();
        
        final IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveWorkbenchWindow(event)
                .getActivePage().getSelection();
        final Object selectionObject = selection.getFirstElement();

        final File file = ((ResearchFile)selectionObject).getFile();
        try
        {
            DesktopUtils.openWithDefaultApplication(file);
        }
        catch (IOException e)
        {
            LOG.error("Unable to open file " + file.getAbsolutePath(), e);
            MessageDialog.openError(shell, "", "Unable to open file " + file.getAbsolutePath() + NEW_LINE + "Reason: " + e.getMessage());
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
    public void removeHandlerListener(IHandlerListener arg0)
    {        
    }

}
