package au.org.intersect.exsite9.service.factory;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import au.org.intersect.exsite9.service.ProjectManager;

public final class ProjectManagerFactory extends AbstractServiceFactory
{
    /**
     * @{inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Object create(final Class serviceInterface, final IServiceLocator parentLocator, final IServiceLocator locator)
    {
        return new ProjectManager();
    }
}
