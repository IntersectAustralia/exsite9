/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
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

/**
 * Checks for new files either within a particular watched folder of a project or across all of the watched folders of a project
 */
public class IdentifyAllNewFilesForProjectJob extends Job
{
    
    private static final Logger LOG = Logger.getLogger(IdentifyAllNewFilesForProjectJob.class);
    
    private static final String jobName = "IdentifyAllNewFilesForProject";
    
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
        if(!SemaphoreManager.getBackgroundJobSemaphore().tryAcquire())
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
            
            monitor.beginTask("Indentify files...", IProgressMonitor.UNKNOWN);
            
            final IResearchFileService fileService = (IResearchFileService) PlatformUI.getWorkbench().getService(IResearchFileService.class);
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
                    final ProjectExplorerView view = (ProjectExplorerView) ViewUtils.getViewByID(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), ProjectExplorerView.ID);
                    view.refresh();
                }
            });
            
            return Status.OK_STATUS;
        }
        finally
        {
            monitor.done();
            SemaphoreManager.getBackgroundJobSemaphore().release();
        }
    }

}
