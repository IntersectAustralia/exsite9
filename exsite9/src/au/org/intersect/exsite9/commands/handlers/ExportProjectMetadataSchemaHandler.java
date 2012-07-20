package au.org.intersect.exsite9.commands.handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.OutputSupplier;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.IProjectService;

public class ExportProjectMetadataSchemaHandler implements IHandler
{

    @Override
    public void addHandlerListener(IHandlerListener handlerListener)
    {
    }

    @Override
    public void dispose()
    {
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();

        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project currentProject = projectManager.getCurrentProject();
        
        if (currentProject.getSchema() == null)
        {
            throw new IllegalArgumentException("Trying to export XML for a project with no metadata schema");
        }
        
        final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
        fileDialog.setOverwrite(true);
        fileDialog.setFileName(currentProject.getSchema().getName() + ".xml");
        
        final String filePath = fileDialog.open();

        if (filePath == null)
        {
            return null;
        }
        
        final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);
        final String xml = projectService.buildMetadataSchemaXML(currentProject);

        final File fileToWrite = new File(filePath);
        final OutputSupplier<FileOutputStream> outputSupplier = Files.newOutputStreamSupplier(fileToWrite, false);
        try
        {
            ByteStreams.write(xml.getBytes(Charsets.UTF_8), outputSupplier);
        }
        catch (final IOException e)
        {
            MessageDialog.openError(shell, "Error", "Could not save to file " + fileToWrite.getAbsolutePath());
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
    public void removeHandlerListener(IHandlerListener handlerListener)
    {
    }

}
