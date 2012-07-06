package au.org.intersect.exsite9.jobs;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import au.org.intersect.exsite9.Activator;
import au.org.intersect.exsite9.initialization.ConfigurationInitializer;

public class IdentifyAllNewFilesForProjectRepeatingJob extends AbstractRepeatingJob implements IPropertyChangeListener
{
    private static final Logger LOG = Logger.getLogger(IdentifyAllNewFilesForProjectRepeatingJob.class);
    
    private static final String jobName = "IdentifyAllNewFilesForProjectRepeatingJob";
    
    public IdentifyAllNewFilesForProjectRepeatingJob(long repeatDelayMillis)
    {
        super(jobName, repeatDelayMillis);
        Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);
    }
    
    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
        try
        {
            LOG.debug("Starting scheduled job to find files. Current delay = " + repeatDelayMillis + "ms");
            final Job identifyAllNewFilesForProject = new IdentifyAllNewFilesForProjectJob();
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

    @Override
    public void propertyChange(final PropertyChangeEvent event)
    {
        if (event.getProperty() == ConfigurationInitializer.FOLDER_RELOAD_INTERVAL_KEY)
        {
            final int refreshTimeMins = Activator.getDefault().getPreferenceStore().getInt(ConfigurationInitializer.FOLDER_RELOAD_INTERVAL_KEY);
            if (refreshTimeMins > 0)
            {
                final long delayMillis = refreshTimeMins * 60 * 1000;
                stop();
                setDelay(delayMillis);
                start();
                LOG.info("Configured interval on scheduled job to find files to " + delayMillis + "ms");
            }
            else
            {
                stop();
                setDelay(0);
                LOG.info("Disabled scheduled job to find files.");
            }
        }
    }
}
