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
 * Compares research files by their size.
 */
public final class ResearchFileSizeComparator implements Comparator<ResearchFile>
{
    private final boolean ascending;

    public ResearchFileSizeComparator(final boolean ascending)
    {
        this.ascending = ascending;
    }
    
    /**
     * @{inheritDoc}
     */
    @Override
    public int compare(final ResearchFile rf1, final ResearchFile rf2)
    {
        final long size1 = rf1.getFile().length();
        final long size2 = rf2.getFile().length();

        if (size1 < size2)
        {
            return ascending ? -1 : 1;
        }
        if (size1 > size2)
        {
            return ascending ? 1 : -1;
        }
        return 0;
    }

}
