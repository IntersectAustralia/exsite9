package au.org.intersect.exsite9.jobs;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.IResearchFileService;
import au.org.intersect.exsite9.view.ProjectExplorerView;
import au.org.intersect.exsite9.view.ViewUtils;

public class ConsolidateFoldersJob extends Job
{

    private static final String jobName = "ConsolidateFoldersForProject";
    
    private final Folder parentFolder;
    private final List<Folder> subFolders;
    
    public ConsolidateFoldersJob(Folder parentFolder, List<Folder>subFolders)
    {
        super(jobName);
        this.parentFolder = parentFolder;
        this.subFolders = subFolders;
    }
    
    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
        try
        {
            SemaphoreManager.getBackgroundJobSemaphore().acquire();
            
            final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
            final Project project = projectManager.getCurrentProject();
     
            if (project == null || project.getId() == null)
            {
                return Status.CANCEL_STATUS;
            }
     
            monitor.beginTask("Consolidating Folders", IProgressMonitor.UNKNOWN);
            
            final IResearchFileService fileService = (IResearchFileService) PlatformUI.getWorkbench().getService(IResearchFileService.class);
            
            for(final Folder subFolder : subFolders)
            {
                fileService.consolidateSubFolderIntoParentFolder(project, parentFolder, subFolder);
            }
            
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
        catch(InterruptedException ie)
        {
            // TODO: handle this
            return Status.CANCEL_STATUS;
        }
        finally
        {
            SemaphoreManager.getBackgroundJobSemaphore().release();
            monitor.done();
        }
    }

}
