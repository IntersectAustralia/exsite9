package au.org.intersect.exsite9.jobs;

import java.util.concurrent.Semaphore;

/**
 * Manages the semaphore that co-ordinates the running of background jobs
 * that affect files and folders so that only one such job can run at
 * a time.
 */
public class SemaphoreManager
{
    
    private static final Semaphore backgroundJobSemaphore = new Semaphore(1);
    
    public static Semaphore getBackgroundJobSemaphore()
    {
        return backgroundJobSemaphore;
    }
}
