/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.commands.handlers;

import java.awt.Desktop;
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

/**
 * Command handler to open the containing directory of a file on disk, via the plugin.xml
 */
public class ShowInFileSystemHandler implements IHandler
{
    private static final Logger LOG = Logger.getLogger(ShowInFileSystemHandler.class);

    @Override
    public void addHandlerListener(final IHandlerListener handlerListener)
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
        
        try
        {
            Desktop.getDesktop().open(((ResearchFile)selectionObject).getFile().getParentFile());
        }
        catch (IOException e)
        {
            LOG.error("Cannot access the file system", e);
            MessageDialog.openError(shell, "", "Unable to access the file system.");
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
    public void removeHandlerListener(final IHandlerListener handlerListener)
    {     
    }

}
