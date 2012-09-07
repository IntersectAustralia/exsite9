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
 * A Comparator for {@link ResearchFile}s.
 * Compares them by their name ignoring case alphabetically ascending or descending.
 */
public class ResearchFileNameComparator implements Comparator<ResearchFile>
{
    protected final int factor;

    /**
     * @param ascending {@code true} to compare ascending. {@code false} to compare descending.
     */
    public ResearchFileNameComparator(final boolean ascending)
    {
        this.factor = ascending ? 1 : -1;
    }

    @Override
    public int compare(final ResearchFile rf1, final ResearchFile rf2)
    {
        final String name1 = rf1.getFile().getName();
        final String name2 = rf2.getFile().getName();

        final int compareIgnoreCase = name1.compareToIgnoreCase(name2);

        if (compareIgnoreCase != 0)
        {
            return compareIgnoreCase * this.factor;
        }
        return 0;
    }
}
