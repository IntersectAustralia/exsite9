package au.org.intersect.exsite9.jobs;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class IdentifyAllNewFilesForProjectRepeatingJob extends AbstractRepeatingJob
{
    private static final Logger LOG = Logger.getLogger(IdentifyAllNewFilesForProjectRepeatingJob.class);
    
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
            LOG.debug("Starting scheduled job to find files.");
            Job identifyAllNewFilesForProject = new IdentifyAllNewFilesForProjectJob();
            identifyAllNewFilesForProject.schedule();
            return Status.OK_STATUS;
        }
        finally
        {
            if(shouldSchedule())
            {
                this.schedule(this.repeatDelayMillis);
            }
            LOG.debug("End of scheduled job to find files.");
        }
    }

}
