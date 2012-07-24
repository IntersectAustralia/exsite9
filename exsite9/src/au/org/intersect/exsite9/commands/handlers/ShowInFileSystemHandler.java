package au.org.intersect.exsite9.commands.handlers;

import java.awt.Desktop;
import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.ResearchFile;

public class ShowInFileSystemHandler implements IHandler
{

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
        final IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveWorkbenchWindow(event)
                .getActivePage().getSelection();
        final Object selectionObject = selection.getFirstElement();
        
        try
        {
            Desktop.getDesktop().open(((ResearchFile)selectionObject).getFile().getParentFile());
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
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