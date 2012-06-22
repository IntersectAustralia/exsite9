package au.org.intersect.exsite9.jobs;

import org.eclipse.core.runtime.jobs.Job;

public abstract class AbstractRepeatingJob extends Job
{
    /**
     *  Flag to indicate that the job should continue to run to schedule
     */
    private boolean running = true;

    /**
     * The frequency the job runs at
     */
    protected long repeatDelayMillis = 0L;
    
    /**
     * A repeating job.
     * @param name The Job Name
     * @param repeatDelayMillis The schedule interval in milliseconds
     */
    public AbstractRepeatingJob(String name, long repeatDelayMillis)
    {
        super(name);
        this.repeatDelayMillis = repeatDelayMillis;
    }
    
    @Override
    public boolean shouldSchedule() {
        return running;
    }
    
    public void start() {
        running = true;
    }
    
    public void stop() {
        running = false;
    }
     
}
