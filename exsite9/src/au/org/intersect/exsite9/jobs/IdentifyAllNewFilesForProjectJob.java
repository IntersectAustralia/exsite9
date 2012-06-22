package au.org.intersect.exsite9.jobs;

import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IFileService;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.view.ProjectExplorerView;

public class IdentifyAllNewFilesForProjectJob extends Job
{
    
    private static final Logger LOG = Logger.getLogger(IdentifyAllNewFilesForProjectJob.class);
    
    private static final String jobName = "IdentifyAllNewFilesForProject";
    
    private static final Semaphore semaphore = new Semaphore(1);
    
    private final Folder folder;
    
    public IdentifyAllNewFilesForProjectJob()
    {
        super(jobName);
        this.folder = null;
    }

    public IdentifyAllNewFilesForProjectJob(Folder folder)
    {
        super(jobName);
        this.folder = folder;
    }
    
    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
        // Only allow one instance of the job to run at a time.
        if(!semaphore.tryAcquire())
        {
            LOG.info("Finding files cancelled due to already running process.");
            return Status.CANCEL_STATUS;
        }
        
        try
        {
            final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
            final Project project = projectManager.getCurrentProject();
     
            if (project == null || project.getId() == null)
            {
                return Status.CANCEL_STATUS;
            }
            
            final IFileService fileService = (IFileService) PlatformUI.getWorkbench().getService(IFileService.class);
            if (folder == null)
            {
                fileService.identifyNewFilesForProject(project);
            }
            else
            {
                fileService.identifyNewFilesForFolder(project, this.folder);
            }
            
            // Refresh the project explorer view in the ui thread
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                    
                    final IViewReference[] viewRefs = window.getActivePage().getViewReferences();
                    
                    for(int i = 0; i < viewRefs.length;++i)
                    {
                        final IViewReference viewRef = viewRefs[i];
                        final IViewPart viewPart = viewRef.getView(false);
                        
                        if (viewPart instanceof ProjectExplorerView)
                        {
                            final ProjectExplorerView view = (ProjectExplorerView) viewPart;
                            view.refreshAndExpand();
                            break;
                        }
                    }
                }
            });
            
            return Status.OK_STATUS;
        }
        finally
        {
            semaphore.release();
        }
    }

}
