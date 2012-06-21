package au.org.intersect.exsite9.jobs;

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
    private static final String jobName = "IdentifyAllNewFilesForProject";
    
    private final Project project;
    private final Folder folder;
    
    public IdentifyAllNewFilesForProjectJob()
    {
        super(jobName);
        
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project project = projectManager.getCurrentProject();
        this.project = project;
        this.folder = null;
    }
    
    public IdentifyAllNewFilesForProjectJob(Project project)
    {
        super(jobName);
        this.project = project;
        this.folder = null;
    }

    public IdentifyAllNewFilesForProjectJob(Project project, Folder folder)
    {
        super(jobName);
        this.project = project;
        this.folder = folder;
    }
    
    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
        if (project == null)
        {
            throw new IllegalStateException("Trying to edit a null project");
        }

        if (project.getId() == null)
        {
            throw new IllegalStateException("Active project doesn't have an Id");
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

}
