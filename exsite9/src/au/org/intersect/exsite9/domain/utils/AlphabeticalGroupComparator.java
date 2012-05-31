/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import java.util.Comparator;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.NewFilesGroup;

/**
 * Compares {@link Group}s alphabetically ascending.
 */
public final class AlphabeticalGroupComparator implements Comparator<Group>
{

    @Override
    public int compare(final Group group1, final Group group2)
    {
        if (group1 instanceof NewFilesGroup)
        {
            return 1;
        }
        if (group2 instanceof NewFilesGroup)
        {
            return -1;
        }

        final String group1Name = group1.getName();
        final String group2Name = group2.getName();
        return group1Name.compareToIgnoreCase(group2Name);
    }
}
