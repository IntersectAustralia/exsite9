package au.org.intersect.exsite9.commands.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.jobs.Job;
import au.org.intersect.exsite9.jobs.IdentifyAllNewFilesForProjectJob;

public class ReloadProjectHandler implements IHandler
{

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException
    {
        Job identifyAllNewFilesForProject = new IdentifyAllNewFilesForProjectJob();
        identifyAllNewFilesForProject.schedule();
        return null;
    }

    @Override
    public void addHandlerListener(final IHandlerListener handlerListener)
    {

    }

    @Override
    public void dispose()
    {

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
