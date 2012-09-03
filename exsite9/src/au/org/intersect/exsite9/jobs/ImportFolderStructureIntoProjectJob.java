package au.org.intersect.exsite9.jobs;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IResearchFileService;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.view.ProjectExplorerView;
import au.org.intersect.exsite9.view.ViewUtils;

public class ImportFolderStructureIntoProjectJob extends Job
{
    
    private static final Logger LOG = Logger.getLogger(ImportFolderStructureIntoProjectJob.class);
    
    private static final String jobName = "ImportFolderStructureIntoProject";
    
    private final Folder folder;
    
    public ImportFolderStructureIntoProjectJob(Folder folder)
    {
        super(jobName);
        this.folder = folder;
    }
    
    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
        // Only allow one instance of the job to run at a time.
        
        
        try
        {
            SemaphoreManager.getBackgroundJobSemaphore().acquire();
            
            final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
            final Project project = projectManager.getCurrentProject();
     
            if (project == null || project.getId() == null)
            {
                return Status.CANCEL_STATUS;
            }
            
            monitor.beginTask("Importing Folder Structure...", IProgressMonitor.UNKNOWN);
            
            final IResearchFileService fileService = (IResearchFileService) PlatformUI.getWorkbench().getService(IResearchFileService.class);
            fileService.importFolderStructureForProject(project, folder);
            
            // Refresh the project explorer view in the ui thread
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    final ProjectExplorerView view = (ProjectExplorerView) ViewUtils.getViewByID(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), ProjectExplorerView.ID);
                    view.refresh();
                }
            });
            
            return Status.OK_STATUS;
        }
        catch(Exception e)
        {
            LOG.warn("Error importing folder structure",e);
            return Status.OK_STATUS;
        }
        finally
        {
            monitor.done();
            SemaphoreManager.getBackgroundJobSemaphore().release();
        }
    }

}
