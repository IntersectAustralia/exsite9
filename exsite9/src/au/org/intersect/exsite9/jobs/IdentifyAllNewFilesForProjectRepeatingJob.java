package au.org.intersect.exsite9.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class IdentifyAllNewFilesForProjectRepeatingJob extends AbstractRepeatingJob
{
    private static final String jobName = "IdentifyAllNewFilesForProjectRepeatingJob";
    
    public IdentifyAllNewFilesForProjectRepeatingJob(long repeatDelayMillis)
    {
        super(jobName, repeatDelayMillis);
    }
    
    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
        try
        {
            Job identifyAllNewFilesForProject = new IdentifyAllNewFilesForProjectJob();
            identifyAllNewFilesForProject.schedule();
            return Status.OK_STATUS;
        }
        finally
        {
            this.schedule(this.repeatDelayMillis);
        }
    }

}
