package au.org.intersect.exsite9.commands.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.google.common.base.Strings;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IMetadataCategoryService;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.MetadataCategoryService;
import au.org.intersect.exsite9.wizard.metadatacategory.AddMetadataCategoryWizard;

public class AddMetadataCategoryHandler implements IHandler
{

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
    public Object execute(ExecutionEvent event) throws ExecutionException
    {        
        MetadataCategory metadataCategory = null;
        
        System.out.println("category parameter = " + event.getParameter("au.org.intersect.exsite9.commands.AddMetadataCategoryCommand.categoryParameter"));
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project project = projectManager.getCurrentProject();
        if (project == null)
        {
            throw new IllegalStateException("Trying to edit a null project");
        }
        
        String eventParam = event.getParameter("au.org.intersect.exsite9.commands.AddMetadataCategoryCommand.categoryParameter");
        
        if (!Strings.isNullOrEmpty(eventParam))
        {
           Long id = Long.valueOf(eventParam);
           IMetadataCategoryService metadataCategpryService = (IMetadataCategoryService) PlatformUI.getWorkbench().getService(IMetadataCategoryService.class);
           metadataCategory = metadataCategpryService.findById(id);
        }
        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
        final AddMetadataCategoryWizard wizard = new AddMetadataCategoryWizard(project, metadataCategory);
        final WizardDialog wizardDialog = new WizardDialog(shell, wizard);
        wizardDialog.open();

        return wizard.getProjectWithUpdatedMetadataCategories();
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
    public void removeHandlerListener(final IHandlerListener handlerListener)
    {
        // TODO Auto-generated method stub

    }

}
