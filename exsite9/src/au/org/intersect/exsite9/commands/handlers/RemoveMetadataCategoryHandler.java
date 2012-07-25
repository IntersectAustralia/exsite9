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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IMetadataCategoryService;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.IResearchFileService;
import au.org.intersect.exsite9.service.ISchemaService;

/**
 * Handles the operation of removing a metadata category
 */
public final class RemoveMetadataCategoryHandler implements IHandler
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
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project project = projectManager.getCurrentProject();
        if (project == null)
        {
            throw new IllegalStateException("Trying to edit a null project");
        }

        final String eventParam = event.getParameter("au.org.intersect.exsite9.commands.RemoveMetadataCategoryCommand.categoryParameter");
        final Long id = Long.valueOf(eventParam);
        final IMetadataCategoryService metadataCategoryService = (IMetadataCategoryService) PlatformUI.getWorkbench().getService(IMetadataCategoryService.class);
        final MetadataCategory metadataCategory = metadataCategoryService.findById(id);

        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
        final boolean confirm = MessageDialog.openConfirm(shell, "Are you sure?", "Are you sure you wish to delete metadata category '" +
                metadataCategory.getName() + "'? Associations with Groups and Files will also be removed.");
        if (!confirm)
        {
            return null;
        }

        final IResearchFileService researchFileService = (IResearchFileService) PlatformUI.getWorkbench().getService(IResearchFileService.class);
        final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
        final ISchemaService schemaService = (ISchemaService) PlatformUI.getWorkbench().getService(ISchemaService.class);

        for (final MetadataValue metadataValue : metadataCategory.getValues())
        {
            for (final ResearchFile researchFile : researchFileService.getResearchFilesWithAssociatedMetadata(metadataCategory, metadataValue))
            {
                researchFileService.disassociateMetadata(researchFile, metadataCategory, metadataValue);
            }
            for (final Group group : groupService.getGroupsWithAssociatedMetadata(metadataCategory, metadataValue))
            {
                groupService.disassociateMetadata(group, metadataCategory, metadataValue);
            }
        }

        final Schema schema = project.getSchema();

        schemaService.removeMetadataCategoryFromSchema(schema, metadataCategory);
        metadataCategoryService.deleteMetadataCategory(metadataCategory);
        
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
