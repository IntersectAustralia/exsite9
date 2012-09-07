/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import java.util.Comparator;

import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Compares research files by their modified date.
 */
public final class ResearchFileModifiedDataComparator implements Comparator<ResearchFile>
{
    private final boolean ascending;

    public ResearchFileModifiedDataComparator(final boolean ascending)
    {
        this.ascending = ascending;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public int compare(final ResearchFile rf1, final ResearchFile rf2)
    {
        final long date1 = rf1.getFile().lastModified();
        final long date2 = rf2.getFile().lastModified();

        if (date1 < date2)
        {
            return ascending ? -1 : 1;
        }
        if (date1 > date2)
        {
            return ascending ? 1 : -1;
        }
        return 0;
    }
}
