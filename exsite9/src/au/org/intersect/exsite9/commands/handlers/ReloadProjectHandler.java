package au.org.intersect.exsite9.commands.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IFileService;

public class ReloadProjectHandler implements IHandler
{

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException
    {
        final IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveWorkbenchWindow(event)
                .getActivePage().getSelection();
        final Project project = (Project) selection.getFirstElement();

        if (project != null)
        {
            final IFileService fileService = (IFileService) PlatformUI.getWorkbench().getService(IFileService.class);
            fileService.identifyNewFilesForProject(project);
        }
        return null;
    }

    @Override
    public void addHandlerListener(final IHandlerListener handlerListener)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isEnabled()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isHandled()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void removeHandlerListener(IHandlerListener handlerListener)
    {
        // TODO Auto-generated method stub

    }
}
