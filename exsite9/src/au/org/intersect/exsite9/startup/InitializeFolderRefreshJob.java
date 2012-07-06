/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.startup;

import org.eclipse.ui.IStartup;

import au.org.intersect.exsite9.Activator;
import au.org.intersect.exsite9.initialization.ConfigurationInitializer;
import au.org.intersect.exsite9.jobs.IdentifyAllNewFilesForProjectRepeatingJob;

/**
 * Initializes the folder refresh job.
 */
public class InitializeFolderRefreshJob implements IStartup
{

    /**
     * @{inheritDoc}
     */
    @Override
    public void earlyStartup()
    {
        // Start the scheduled find files job
        final int refreshTimeMins = Activator.getDefault().getPreferenceStore().getInt(ConfigurationInitializer.FOLDER_RELOAD_INTERVAL_KEY);
        final long delayMillis = refreshTimeMins * 60 * 1000;
        final IdentifyAllNewFilesForProjectRepeatingJob findFilesJob = new IdentifyAllNewFilesForProjectRepeatingJob(delayMillis);

        if (refreshTimeMins != 0)
        {
            findFilesJob.schedule(delayMillis);
        }
    }
}