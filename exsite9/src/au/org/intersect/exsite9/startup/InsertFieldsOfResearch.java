/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.startup;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;

import au.org.intersect.exsite9.jobs.InsertFieldsOfResearchJob;

/**
 * Inserts all of the Fields of Research on startup.
 */
public final class InsertFieldsOfResearch implements IStartup
{
    public InsertFieldsOfResearch()
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void earlyStartup()
    {
        final Job insertFieldsOfResearchJob = new InsertFieldsOfResearchJob();
        insertFieldsOfResearchJob.schedule();
    }
}
