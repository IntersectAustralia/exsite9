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
 * Compares them by their name ignoring case alphabetically ascending.
 */
public final class AlphabeticalResearchFileComparator implements Comparator<ResearchFile>
{
    public AlphabeticalResearchFileComparator()
    {
    }

    @Override
    public int compare(final ResearchFile rf1, final ResearchFile rf2)
    {
        final String name1 = rf1.getName();
        final String name2 = rf2.getName();

        final int compareIgnoreCase = name1.compareToIgnoreCase(name2);

        if (compareIgnoreCase != 0)
        {
            return compareIgnoreCase;
        }

        if (rf1.getId() < rf2.getId())
        {
            return -1;
        }
        else if (rf1.getId() > rf2.getId())
        {
            return 1;
        }
        return 0;
    }
}
