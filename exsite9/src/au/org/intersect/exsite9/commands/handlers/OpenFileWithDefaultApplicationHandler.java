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

public class OpenFileWithDefaultApplicationHandler implements IHandler
{
    private static final Logger LOG = Logger.getLogger(OpenFileWithDefaultApplicationHandler.class);
    
    @Override
    public void addHandlerListener(IHandlerListener arg0)
    {
        // TODO Auto-generated method stub
        
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
            Desktop.getDesktop().open(((ResearchFile)selectionObject).getFile());
        }
        catch (IOException e)
        {
            LOG.error("The user's computer does not recognise this file or does not have a default application for it.");
            MessageDialog.openError(shell, "", "Unable to open this file.");
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
