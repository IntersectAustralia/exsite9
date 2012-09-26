/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
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
import au.org.intersect.exsite9.wizard.metadatacategory.AddMetadataCategoryWizard;

/**
 * Command handler to add a metadata category to a project, via the plugin.xml
 */
public class AddMetadataCategoryHandler implements IHandler
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
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project project = projectManager.getCurrentProject();
        if (project == null)
        {
            throw new IllegalStateException("Trying to edit a null project");
        }
        final String eventParam = event.getParameter("au.org.intersect.exsite9.commands.AddMetadataCategoryCommand.categoryParameter");

        // Since the same handler is used to add Add a MetadataCategory AND Edit one, we need to check if there is an argument.
        final MetadataCategory metadataCategory;
        
        if (!Strings.nullToEmpty(eventParam).isEmpty())
        {
           final Long id = Long.valueOf(eventParam);
           final IMetadataCategoryService metadataCategoryService = (IMetadataCategoryService) PlatformUI.getWorkbench().getService(IMetadataCategoryService.class);
           metadataCategory = metadataCategoryService.findById(id);
        }
        else
        {
            metadataCategory = null;
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
